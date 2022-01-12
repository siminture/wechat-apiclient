package tech.simmy.wechat.support;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.Validate;
import tech.simmy.wechat.apiclient.*;
import tech.simmy.wechat.util.JsonSerializer;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DefaultWechatApiClient implements WechatApiClient {

    private static final String CODE_TO_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    private static final String GET_PHONE_NUMBER = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";

    private final OkHttpClient httpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final WechatApiClientProperties properties;
    private final JsonSerializer jsonSerializer;
    private final AccessTokenHolder accessTokenHolder;

    public DefaultWechatApiClient(WechatApiClientProperties properties, JsonSerializer jsonSerializer) {
        this.properties = properties;
        this.jsonSerializer = jsonSerializer;
        this.accessTokenHolder = new AccessTokenHolder(this::accessTokenSupplier);
    }

    private GetAccessTokenResult doGetAccessToken() {
        final HttpUrl url = HttpUrl.get(GET_ACCESS_TOKEN_URL).newBuilder()
                .addQueryParameter("grant_type", "client_credential")
                .addQueryParameter("appid", properties.getAppId())
                .addQueryParameter("secret", properties.getSecret())
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        logger.debug("Request getAccessToken: {}", request);

        try (Response response = httpClient.newCall(request).execute()) {
            logger.debug("Response getAccessToken: {}", response);

            ResponseBody responseBody = response.body();
            Validate.notNull(responseBody, "ResponseBody is null");

            return jsonSerializer.deserialize(responseBody.charStream(), GetAccessTokenResult.class);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private AccessToken accessTokenSupplier() {
        GetAccessTokenResult getAccessTokenResult = doGetAccessToken();
        if (getAccessTokenResult == null) {
            throw new IllegalStateException("GetAccessTokenResult is null");
        }

        getAccessTokenResult.assertIsSuccess();

        return new AccessToken(getAccessTokenResult.getAccessToken(), Instant.now().plusSeconds(getAccessTokenResult.getExpiresIn()));
    }

    @Override
    public AccessToken getAccessToken() {
        return accessTokenHolder.getValue();
    }

    @Override
    public CodeToSessionResult codeToSession(String code) {

        final HttpUrl url = HttpUrl.get(CODE_TO_SESSION_URL).newBuilder()
                .addQueryParameter("grant_type", "authorization_code")
                .addQueryParameter("appid", properties.getAppId())
                .addQueryParameter("secret", properties.getSecret())
                .addQueryParameter("js_code", code)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        logger.debug("Request codeToSession: {}", request);

        try (Response response = httpClient.newCall(request).execute()) {
            logger.debug("Response codeToSession: {}", response);

            ResponseBody responseBody = response.body();
            Validate.notNull(responseBody, "ResponseBody is null");

            return jsonSerializer.deserialize(responseBody.charStream(), CodeToSessionResult.class);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public GetPhoneNumberResult getPhoneNumber(String code) {
        final HttpUrl url = HttpUrl.get(GET_PHONE_NUMBER).newBuilder()
                .addQueryParameter("access_token", accessTokenHolder.getValue().accessToken())
//                .addQueryParameter("js_code", code)
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("code", code);

        String jsonData = jsonSerializer.serializeToString(data);
        final RequestBody requestBody = RequestBody.create(JSON, jsonData);

        final Request request = new Request.Builder()
                .url(url)
                .method("POST", requestBody)
                .build();

        logger.debug("Request getPhoneNumber: {}", request);

        try (Response response = httpClient.newCall(request).execute()) {
            logger.debug("Response getPhoneNumber: {}", response);

            ResponseBody responseBody = response.body();
            Validate.notNull(responseBody, "ResponseBody is null");

            return jsonSerializer.deserialize(responseBody.charStream(), GetPhoneNumberResult.class);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
