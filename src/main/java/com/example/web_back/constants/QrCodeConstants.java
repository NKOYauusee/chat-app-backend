package com.example.web_back.constants;

public class QrCodeConstants {
    // 0 未扫码  1 已扫码未确认  2 过期  3 确认
    public static Integer QR_CODE_STATE_NO_SCAN = 0;
    public static Integer QR_CODE_STATE_HAS_SCAN = 1;
    public static Integer QR_CODE_STATE_EXPIRED = 2;
    public static Integer QR_CODE_STATE_HAS_ENSURED = 3;
    // 过期状态
    public static Long QR_KEY_EXPIRED_STATE = 5L;
    // 过期时间
    public static Long QR_KEY_EXPIRED_TIME = 60L;
    public static String QR_CODE_PREFIX = "qrCode:";
    public static String QR_CODE_TOKEN = "qrCodeToken:";
}
