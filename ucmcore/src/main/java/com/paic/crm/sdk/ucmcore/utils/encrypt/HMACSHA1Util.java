package com.paic.crm.sdk.ucmcore.utils.encrypt;


import org.apache.commons.codec.binary.Base64;

import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by hanyh on 16/11/10.
 * 加密类
 */
public class HMACSHA1Util {

    public static String getHmacSHA1(String message, String key) {

        String hmacSha1 = null;
        try {
            message = URLEncoder.encode(message, "UTF-8");
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(spec);
            byte[] byteHmac = mac.doFinal(message.getBytes());
            byte[] encodeBase64 = Base64.encodeBase64(byteHmac);
            hmacSha1 = new String(encodeBase64, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hmacSha1;
    }
}
