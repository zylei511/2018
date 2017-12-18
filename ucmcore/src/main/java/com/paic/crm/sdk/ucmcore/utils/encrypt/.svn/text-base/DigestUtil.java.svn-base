/*****************************************************************************
 *
 *                      HUSONG PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to HUSONG
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from HUSONG.
 *
 *            Copyright (c) 2015 by HUSONG.  All rights reserved.
 *
 *****************************************************************************/
package com.paic.crm.sdk.ucmcore.utils.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DigestUtil {
    public static String digest(String key, String content) {
	   try {
	       Mac mac = Mac.getInstance("HmacSHA1");
	       byte[] secretByte = key.getBytes("utf-8");
	       byte[] dataBytes = content.getBytes("utf-8");
	
	       SecretKey secret = new SecretKeySpec(secretByte, "HmacSHA1");
	       mac.init(secret);
	
	       byte[] doFinal = mac.doFinal(dataBytes);
	       byte[] encodeBase64 = Base64.encodeBase64(doFinal);  
	//       byte[] hexB = new Hex().encode(doFinal);
	       return new String(encodeBase64, "utf-8");
	   } catch (Exception e) {
	       throw new RuntimeException(e);
	   }
	}

   @SuppressWarnings("unchecked")
   public static String digest(String key, Map<String, ?> map) {
	   StringBuilder s = new StringBuilder();
	   List<String> list = new ArrayList<String>();
	   for (Map.Entry<String, ?> entry : map.entrySet()) {
	               list.add(entry.getKey());
	   }
	   
	   Collections.sort(list);
	   
	   for (String str : list) {
         Object values=map.get(str);
         if(values==null){
            continue;
         }else{
             if(values.getClass().isArray()) {
	           continue;
	         } else if(values instanceof List) {
	           continue;
	         }else if(values instanceof Map){
	           continue;
	         } else if(values instanceof Set){
	           continue;
	         } else {
	           s.append((String)map.get(str));
	         }
          }
	   }
	   return digest(key, s.toString());
  }
}
