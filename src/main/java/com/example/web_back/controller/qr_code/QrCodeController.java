package com.example.web_back.controller.qr_code;

import cn.hutool.core.util.RandomUtil;
import com.example.web_back.constants.QrCodeConstants;
import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/qrcode")
public class QrCodeController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(QrCodeController.class);

    @Resource
    RedisUtils redisUtils;


    @RequestMapping("/generate")
    public ResponseVo getQrCodeState(HttpServletRequest request, String id) {
        if (id == null) {
            id = request.getSession().getId();
        }
        Map<String, String> qrcodeState = qrCodeStateChecked(id);
        qrcodeState.put("from", id);

        return resSuccess("", qrcodeState);
    }

    // 扫描成功则返回一个用于临时确认的token
    @RequestMapping("/scan")
    public ResponseVo scanQrCode(@RequestParam String id) throws BusinessException {
        String token = qrCodeScanned(id);
        return resSuccess("", token);
    }

    @RequestMapping("/validate")
    public ResponseVo validateQrCode(String id, String token) throws BusinessException {
        if (validate(id, token)) {
            // TODO 生成jwt和新的跳转链接
            return resSuccess("登录成功", null);
        }

        return resFail("登录失败", null);
    }

    private Map<String, String> qrCodeStateChecked(String id) {
        Map<String, String> codeState = new HashMap<>();

        String code;
        String qrCodeKey = QrCodeConstants.QR_CODE_PREFIX + id;
        String tempCode = (String) redisUtils.get(qrCodeKey);
        // 初次扫描登录
        if (tempCode == null) {
            code = RandomUtil.randomString(20);
            redisUtils.set(qrCodeKey,
                    code + ":" + QrCodeConstants.QR_CODE_STATE_NO_SCAN,
                    QrCodeConstants.QR_KEY_EXPIRED_TIME +
                            QrCodeConstants.QR_KEY_EXPIRED_STATE
            );
            codeState.put("qrcode", code);
            codeState.put("state", String.valueOf(QrCodeConstants.QR_CODE_STATE_NO_SCAN));
        }
        //
        else {
            String[] state = tempCode.split(":");
            codeState.put("qrcode", state[0]);
            codeState.put("state", state[1]);

            // 二维码过期
            if (redisUtils.getExpire(qrCodeKey) < QrCodeConstants.QR_KEY_EXPIRED_STATE && !state[1].equals(String.valueOf(QrCodeConstants.QR_CODE_STATE_HAS_ENSURED))) {
                codeState.put("qrcode", null);
                // 返回过期状态
                codeState.put("state", String.valueOf(QrCodeConstants.QR_CODE_STATE_EXPIRED));
            }
        }

        return codeState;
    }

    private String qrCodeScanned(String id) throws BusinessException {
        String qrCodeKey = QrCodeConstants.QR_CODE_PREFIX + id;
        String tempCode = (String) redisUtils.get(qrCodeKey);
        if (tempCode != null && redisUtils.getExpire(qrCodeKey) > QrCodeConstants.QR_KEY_EXPIRED_STATE) {
            // 更新二维码状态
            tempCode = tempCode.split(":")[0] + ":" + QrCodeConstants.QR_CODE_STATE_HAS_SCAN;
            // 60s确认时间
            redisUtils.set(qrCodeKey, tempCode, QrCodeConstants.QR_KEY_EXPIRED_TIME);
            // 扫描成功,生成新的token并返回
            String token = (String) redisUtils.get(QrCodeConstants.QR_CODE_TOKEN + id);
            if (token == null) {
                token = RandomUtil.randomString(20);
                redisUtils.set(
                        QrCodeConstants.QR_CODE_TOKEN + id,
                        token,
                        QrCodeConstants.QR_KEY_EXPIRED_TIME
                );
            }

            return token;
        }

        throw new BusinessException("二维码已失效，请重新扫描");
    }

    private boolean validate(String id, String token) throws BusinessException {
        String qrCodeToken = QrCodeConstants.QR_CODE_TOKEN + id;
        String qrCodeKey = QrCodeConstants.QR_CODE_PREFIX + id;

        String tempCode = (String) redisUtils.get(qrCodeToken);

        if (tempCode == null || !tempCode.equals(token)) {
            throw new BusinessException("二维码错误，请重新扫描");
        }

        redisUtils.del(qrCodeToken);
        // 更新二维码状态
        tempCode = ((String) redisUtils.get(qrCodeKey)).split(":")[0];
        redisUtils.set(qrCodeKey,
                tempCode + ":" + QrCodeConstants.QR_CODE_STATE_HAS_ENSURED,
                QrCodeConstants.QR_KEY_EXPIRED_STATE
        );

        return true;
    }
}
