package com.wenxin;

public class WechatX509TrustManager implements X509TrustManager {

    // ���ͻ���֤��
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    // ����������֤��
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    // ���������ε�X509֤������
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
