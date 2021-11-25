package com.lang.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {

    //��Ч��Ϊ
     public static final Long JWT_TTL = 3600000L;//60 *60 *1000 һ��Сʱ

    //Jwt������Ϣ
     public static final String JWT_KEY = "ujiuye";

     public static String createJWT(String id, String subject, Long ttlMillis){
    //ָ���㷨
     SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    //��ǰϵͳʱ��
     long nowMillis = System.currentTimeMillis();
    //����ǩ��ʱ��
     Date now = new Date(nowMillis);

    //���������Ч��Ϊnull����Ĭ��������Ч��1Сʱ
     if(ttlMillis==null){
     ttlMillis=JwtUtil.JWT_TTL;
    }

    //���ƹ���ʱ������
     long expMillis = nowMillis + ttlMillis;
     Date expDate = new Date(expMillis);

    //������Կ
     SecretKey secretKey = generalKey();

    //��װJwt������Ϣ
     JwtBuilder builder = Jwts.builder()
        .setId(id)//Ψһ��ID
        .setSubject(subject)//���������JSON����
        .setIssuer("admin")//ǩ����
        .setIssuedAt(now)//ǩ��ʱ��
        .signWith(signatureAlgorithm, secretKey)//ǩ���㷨�Լ��ܳ�
        .setExpiration(expDate); //���ù���ʱ��
         return builder.compact();
    }

    /**
    *���ɼ��� secretKey
    *@return
    */
     public static SecretKey generalKey(){
         byte[] encodedKey = Base64.getEncoder().encode(JwtUtil.JWT_KEY.getBytes());
         SecretKey key = new SecretKeySpec(encodedKey,0, encodedKey.length,"AES");
         return key;
    }


    /**
    *������������
    *@param jwt
    *@return
    *@throws Exception
    */
     public static Claims parseJWT(String jwt) throws Exception {
         SecretKey secretKey = generalKey();
         return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(jwt)
        .getBody();
    }
}