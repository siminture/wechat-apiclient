package tech.simmy.wechat.apiclient;

public interface WechatApiClient {

    AccessToken getAccessToken();

    CodeToSessionResult codeToSession(String code);

    GetPhoneNumberResult getPhoneNumber(String code);

}
