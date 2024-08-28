package com.example.web_back.interceptor;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.annotation.RepeatSubmit;
import com.example.web_back.filter.RepeatedlyRequestWrapper;
import com.example.web_back.utils.RedisUtils;
import com.example.web_back.utils.http.HttpHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("SameUrlDataInterceptor")
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    @Resource
    RedisUtils redisUtils;

    @Override
    protected boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) throws Exception {
        String nowParams = "";

        if (request instanceof RepeatedlyRequestWrapper requestWrapper) {
            nowParams = HttpHelper.getBodyString(requestWrapper);
        }

        if (nowParams.isEmpty()) {
            nowParams = JSON.toJSONString(request.getParameterMap());
        }

        Map<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // 请求地址（作为存放cache的key值）
        String url = request.getRequestURI();
        // TODO
        // 唯一值（没有消息头则使用请求地址）
        //String submitKey = StringUtils.trimToEmpty(request.getHeader(header));
        // 唯一标识（指定key + url + 消息头）
        //String cacheRepeatKey = "REPEAT_SUBMIT:" + url + submitKey;

        String cacheRepeatKey = "REPEAT_SUBMIT:" + url;

        Object obj = redisUtils.get(cacheRepeatKey);
        if (obj != null) {
            Map<String, Object> sessionMap = (Map<String, Object>) obj;
            if (sessionMap.containsKey(url)) {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, annotation.interval() * 1000)) {
                    return true;
                }
            }
        }

        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put(url, nowDataMap);
        redisUtils.set(cacheRepeatKey, cacheMap, annotation.interval());

        return false;
    }

    /**
     * 判断参数是否相同
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 判断两次间隔时间
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        return (time1 - time2) < interval;
    }
}
