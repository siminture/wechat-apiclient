package tech.simmy.wechat.apiclient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WechatResult {

    @JsonProperty("errcode")
    private int errorCode;

    @JsonProperty("errmsg")
    private String errorMessage;

    @JsonIgnore
    public boolean isSuccess() {
        return errorCode == 0;
    }

    @JsonIgnore
    public void assertIsSuccess() {
        if (!isSuccess()) throw new IllegalStateException("微信后端API请求结果错误: %s".formatted(errorMessage));
    }

}
