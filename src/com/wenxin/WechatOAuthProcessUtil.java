package com.wenxin;

/**
 * ����: WXOAuthProcess 
 * ����: ΢�ŵ�������¼��Ȩ���̹�����
 */
public class WechatOAuthProcessUtil {
	private static Logger log = LoggerFactory.getLogger(WechatOAuthProcessUtil.class);

    /**
     * 1.��ȡ��Ȩcode
     * @param req
     * @param resp
     */
    public static void getOAuthCode(HttpServletRequest req, HttpServletResponse resp){
        String appId = WechatConfigLoader.getAppId();
        String backUrl = WechatConfigLoader.getBackUrl();
        String url="https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
        url = url.replace("APPID",appId);
        url = url.replace("REDIRECT_URI",URLEncoder.encode(backUrl));
        try {
            resp.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 2.��ȡ��Ȩ����token
     * @param appId       ����ƽ̨Ӧ��Ψһ��ʶ
     * @param appSecret   ����ƽ̨Ӧ����Կ
     * @param code        ��Ȩ��ʱƱ�� ����code����ȡaccessToken
     */
    public static WechatAccessTokenVo getOauthAccessToken(String appId, String appSecret, String code){
        WechatAccessTokenVo wechatAccessTokenVo = null;
        //ƴ��΢�Ż�ȡaccessToken���������
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        url = url.replace("APPID",appId);
        url = url.replace("SECRET",appSecret);
        url = url.replace("CODE",code);
        // ��ȡ��ҳ��Ȩƾ֤ ����https����
        JSONObject jsonObject = (WechatCommonUtil.httpsRequest(url, "GET", null));
        if (null != jsonObject) {
            try {
                wechatAccessTokenVo = new WechatAccessTokenVo();
                wechatAccessTokenVo.setAccessToken(jsonObject.getString("access_token"));
                wechatAccessTokenVo.setExpiresIn(jsonObject.getInteger("expires_in"));
                wechatAccessTokenVo.setRefreshToken(jsonObject.getString("refresh_token"));
                wechatAccessTokenVo.setOpenId(jsonObject.getString("openid"));
                wechatAccessTokenVo.setScope(jsonObject.getString("scope"));
                wechatAccessTokenVo.setUnionid(jsonObject.getString("unionid"));
            } catch (Exception e) {
                wechatAccessTokenVo = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("��ȡ��ҳ��Ȩƾ֤ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wechatAccessTokenVo;
    }

    /**
     * 3.ͨ����ҳ��Ȩ��ȡ�û���Ϣ
     *
     * @param accessToken ��ҳ��Ȩ�ӿڵ���ƾ֤
     * @param openId �û���ʶ
     * @return SNSUserInfo
     */
    @SuppressWarnings( { "deprecation", "unchecked" })
    public static WechatSNSUserInfoVo getSNSUserInfo(String accessToken, String openId) {
        WechatSNSUserInfoVo snsUserInfo = null;
        // ƴ�������ַ ����https����
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        url = url.replace("ACCESS_TOKEN", accessToken);
        url = url.replace("OPENID", openId);

        // ͨ����ҳ��Ȩ��ȡ�û���Ϣ
        JSONObject jsonObject = WechatCommonUtil.httpsRequest(url, "GET", null);

        if (null != jsonObject) {
            try {
                snsUserInfo = new WechatSNSUserInfoVo();
                // �û��ı�ʶ
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // �ǳ�
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // �Ա�1�����ԣ�2��Ů�ԣ�0��δ֪��
                snsUserInfo.setSex(jsonObject.getInteger("sex"));
                // �û����ڹ���
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // �û�����ʡ��
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // �û����ڳ���
                snsUserInfo.setCity(jsonObject.getString("city"));
                // �û�ͷ��
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // �û���Ȩ��Ϣ
                snsUserInfo.setPrivilegeList(JSONArray.toJavaObject(jsonObject.getJSONArray("privilege"), List.class));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("��ȡ�û���Ϣʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return snsUserInfo;
    }

    /**
     * ˢ����Ȩ����token
     * @param appId       ����ƽ̨Ӧ��Ψһ��ʶ
     * @param refreshToken    ͨ��access_token��ȡ����refresh_token����
     */
    public static WechatAccessTokenVo refreshAccessToken(String appId, String refreshToken){
        WechatAccessTokenVo  wechatAccessTokenVo = null;
        //ƴ��΢��ˢ��accessToken���������
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
        url = url.replace("APPID",appId);
        url = url.replace("REFRESH_TOKEN",refreshToken);
        // ��ȡ��ҳ��Ȩƾ֤ ����https����
        JSONObject jsonObject = WechatCommonUtil.httpsRequest(url, "GET", null);
        if (null != jsonObject) {
            try {
                wechatAccessTokenVo = new WechatAccessTokenVo();
                wechatAccessTokenVo.setAccessToken(jsonObject.getString("access_token"));
                wechatAccessTokenVo.setExpiresIn(jsonObject.getInteger("expires_in"));
                wechatAccessTokenVo.setRefreshToken(jsonObject.getString("refresh_token"));
                wechatAccessTokenVo.setOpenId(jsonObject.getString("openid"));
                wechatAccessTokenVo.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wechatAccessTokenVo = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("��ȡ��ҳ��Ȩƾ֤ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wechatAccessTokenVo;
    }
}
