package com.example.web_back.utils;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Data
@Component
public class ValidateCodeUtil {
    private final Logger logger = LoggerFactory.getLogger(ValidateCodeUtil.class);

    private int width = 200; //验证码的宽
    private int height = 50; //验证码的高
    private int lineSize = 10; //验证码中夹杂的干扰线数量
    private int codeSize = 4; //验证码字符个数
    private String code;

    //字体的设置
    private Font getFont() {
        return new Font("Times New Roman", Font.PLAIN, 40);
    }

    //颜色的设置
    private Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + RandomUtil.randomInt(bc - fc - 16);
        int g = fc + RandomUtil.randomInt(bc - fc - 14);
        int b = fc + RandomUtil.randomInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    //干扰线的绘制
    private void drawLine(Graphics g) {
        int x = RandomUtil.randomInt(width);
        int y = RandomUtil.randomInt(height);
        int xl = RandomUtil.randomInt(20);
        int yl = RandomUtil.randomInt(10);
        g.drawLine(x, y, x + xl, y + yl);

    }

    //字符串的绘制
    private void drawString(Graphics g, String str, int i) {
        g.setFont(getFont());
        g.setColor(getRandomColor(108, 190));
        //System.out.println(random.nextInt(randomString.length()));
        g.translate(RandomUtil.randomInt(3), RandomUtil.randomInt(6));
        g.drawString(str, 40 * i + 10, 25);
    }


    public String getRandomCodeBase64() {
        code = RandomUtil.randomString(codeSize);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.fillRect(0, 0, width, height);
        g.setColor(getRandomColor(105, 189));
        g.setFont(getFont());
        //干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }


        //随机字符
        for (int i = 0; i < codeSize; i++) {
            drawString(g, String.valueOf(code.charAt(i)), i);
        }

        g.dispose();
        String base64String = "";
        try {
            //  直接返回图片
            //  ImageIO.write(image, "PNG", response.getOutputStream());
            //返回 base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", bos);

            byte[] bytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            base64String = encoder.encodeToString(bytes);

        } catch (Exception e) {
            logger.error("验证码生成错误");
        }

        return base64String;
    }

}



