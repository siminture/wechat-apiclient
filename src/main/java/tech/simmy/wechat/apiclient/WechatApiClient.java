package tech.simmy.wechat.apiclient;

public interface WechatApiClient {

    GetAccessTokenResult getAccessToken();

    CodeToSessionResult codeToSession(String code);

    GetPhoneNumberResult getPhoneNumber(String code);

}
