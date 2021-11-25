package com.lang.oauth.test;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

public class CreateJwtTest {
    /***
     * 创建令牌测试
     */
    public void testCreateToken() {
        //证书文件路径(放在resources目录下)
        String key_location = "dongyimai.jks";
        //秘钥库密码
        String key_password = "dongyimai";
        //秘钥密码
        String keypwd = "dongyimai";
        //秘钥别名
        String alias = "dongyimai";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        //创建秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());
        //读取秘钥对(公钥+私钥)
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypwd.toCharArray());
        //  获取公钥
        //  RSAPublicKey rsaPublicKey= (RSAPublicKey) keyPair.getPublic();
        //  Base64Encoder base64Encoder = new Base64Encoder();
        //  String encode = base64Encoder.encode(rsaPublicKey.getEncoded());
        //  System.out.println("公钥:-----BEGIN PUBLIC KEY-----"+encode+"-----END PUBLIC KEY-----");

        //获取私钥

        RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();

        //定义Payload，自定义一些参数
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "1");
        tokenMap.put("name", "lang");
        tokenMap.put("roles", "ROLE_VIP,ROLE_USER");
        //生成Jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivate));
        //取出令牌
        String enCode = jwt.getEncoded();
        System.out.println(enCode);
    }
}
