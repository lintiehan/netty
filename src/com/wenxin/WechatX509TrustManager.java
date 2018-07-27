package com.wenxin;

public class WechatX509TrustManager implements X509TrustManager {

    // 检查客户端证书
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    // 检查服务器端证书
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
