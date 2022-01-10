package tech.simmy.wechat.apiclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetPhoneNumberResult extends WechatResult {

    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo {
        private String phoneNumber;
        private String purePhoneNumber;
        private int countryCode;
        private Watermark watermark;
    }

    @Data
    public static class Watermark {
        private long timestamp;
        @JsonProperty("appid")
        private String appId;
    }
}
