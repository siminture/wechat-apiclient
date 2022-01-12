package tech.simmy.wechat.apiclient;

import org.apache.commons.lang3.Validate;

import java.time.Instant;

public record AccessToken(String accessToken, Instant expiredTime) {

    public AccessToken {
        Validate.notBlank(accessToken, "Access Token must not be null or blank");
        Validate.notNull(expiredTime, "Expired time is required");
    }

    public boolean isExpired(){
        return Instant.now().isAfter(expiredTime);
    }
}
