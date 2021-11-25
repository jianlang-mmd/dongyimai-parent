package com.lang.oauth.test;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {
    /***
     * 校验令牌
     */
    public void testParseToken() {
        //令牌
        String token = "MIIDXTCCAkWgAwIBAgIEU0Fp2DANBgkqhkiG9w0BAQsFADBfMQ4wDAYDVQQGEwVs" +
                "aXl1ZTEOMAwGA1UECBMFbGl5dWUxDjAMBgNVBAcTBWxpeXVlMQ4wDAYDVQQKEwVs" +
                "aXl1ZTEOMAwGA1UECxMFbGl5dWUxDTALBgNVBAMTBGxhbmcwHhcNMjExMTE3MDcy" +
                "NjUwWhcNMjIwMjE1MDcyNjUwWjBfMQ4wDAYDVQQGEwVsaXl1ZTEOMAwGA1UECBMF" +
                "bGl5dWUxDjAMBgNVBAcTBWxpeXVlMQ4wDAYDVQQKEwVsaXl1ZTEOMAwGA1UECxMF" +
                "bGl5dWUxDTALBgNVBAMTBGxhbmcwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK" +
                "AoIBAQCmcQdf4AZdh8hZ62n3DRlgLxDbq9/yT6o8C41zXoScfqQvPR5ekpnxug7t" +
                "yqAsEh1Ci8fPfecEg40bfnrPSsSQONCkGV5+sdgfGci24jCQvc6Sn65o4/1CROYF" +
                "+P10SRdMtGTxK/6CEJG1BQzki75fvtdA/w5F8cL1h1ztiFdrazkGiixINx0OJ+l9" +
                "kJl0z7Wm81kERlkMsz4b85hxT6LgQNZ2wLPyHNJwxda1CvpkztmTt08g6ZYrBUbD" +
                "uPfTyt++rw4wrxYbokKsUrqj+yzo3AF8GROGWitu+0McsYUShsWynH+Cwhp8ng1g" +
                "3F1GnAtk9QlWGRZsvtRA+qYYtp3HAgMBAAGjITAfMB0GA1UdDgQWBBQcYgV7PNiV" +
                "t/HDBzlIhz6X6RjDSTANBgkqhkiG9w0BAQsFAAOCAQEAC/j4883VLMHC67snOSiO" +
                "/9obPrgsmeaEzpX0+5GOSwFwlC8hmV2fZva8ixWYYxDMy88UFIkIn8E03sncd57v" +
                "o8ekKODxwqtzC1rRkVi9qg+XyUo76kKhQ/cET+VXFc79ub80wTcy4CIx0wnA2l8I" +
                "R3VRpTzbU3VkjqPY9U6ADphIrCzZcmQLgvR+e9RHknf9cfAwwrdGlh+WFEW3uCjc" +
                "gl+uAxpVrvW3jutQPiRW5GrjnKSvUBusNO7e6+DKUWy2SUtbTlVUoh8Z9Ylh4OsH" +
                "SsnlLr9RMJkSpqJamqg+kAk+L5kW0UZo+nqU6RQdIYkNptnCbcZGOuXre2jOLP+j" +
                "Ow==";
        String publicKey = "-----BEGIN PUBLIC KEY-----" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApnEHX+AGXYfIWetp9w0Z" +
                "YC8Q26vf8k+qPAuNc16EnH6kLz0eXpKZ8boO7cqgLBIdQovHz33nBIONG356z0rE" +
                "kDjQpBlefrHYHxnItuIwkL3Okp+uaOP9QkTmBfj9dEkXTLRk8Sv+ghCRtQUM5Iu+" +
                "X77XQP8ORfHC9Ydc7YhXa2s5BoosSDcdDifpfZCZdM+1pvNZBEZZDLM+G/OYcU+i" +
                "4EDWdsCz8hzScMXWtQr6ZM7Zk7dPIOmWKwVGw7j308rfvq8OMK8WG6JCrFK6o/ss" +
                "6NwBfBkThlorbvtDHLGFEobFspx/gsIafJ4NYNxdRpwLZPUJVhkWbL7UQPqmGLad" +
                "xwIDAQAB" +
                "-----END PUBLIC KEY-----";
        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String enCoded = jwt.getEncoded();
        System.out.println(enCoded);

    }


}
