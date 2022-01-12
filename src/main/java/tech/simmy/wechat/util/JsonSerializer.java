package tech.simmy.wechat.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Json序列化器
 */
public class JsonSerializer {

    private final ObjectMapper objectMapper;

    public JsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = Validate.notNull(objectMapper, "ObjectMapper is required");
    }

    public JsonSerializer() {
        this(createObjectMapper());
    }


    /**
     * Json序列化为字符串
     *
     * @param value 待序列化的对象
     * @return JSON字符串
     */
    public String serializeToString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json序列化为字节数组
     *
     * @param value value 待序列化的对象
     * @return 字节数组
     */
    public byte[] serializeToBytes(Object value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    //region 反序列化Json 字符串

    /**
     * Json反序列化
     *
     * @param content Json字符串
     */
    public <T> T deserialize(String content, Class<T> resultType) {
        try {
            return objectMapper.readValue(content, resultType);
        } catch (JsonProcessingException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json反序列化
     *
     * @param content Json字符串
     */
    public <T> T deserialize(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json反序列化
     *
     * @param content Json字符串
     */
    public <T> T deserialize(String content, JavaType resultType) {
        try {
            return objectMapper.readValue(content, resultType);
        } catch (JsonProcessingException e) {
            throw new UncheckedSerializingException(e);
        }
    }
    //endregion

    //region 反序列化Json Reader

    /**
     * Json反序列化
     *
     * @param reader Json字符串的Reader
     */
    public <T> T deserialize(Reader reader, Class<T> resultType) {
        try {
            return objectMapper.readValue(reader, resultType);
        } catch (IOException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json反序列化
     *
     * @param reader Json字符串的Reader
     */
    public <T> T deserialize(Reader reader, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(reader, valueTypeRef);
        } catch (IOException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json反序列化
     *
     * @param reader Json字符串的Reader
     */
    public <T> T deserialize(Reader reader, JavaType resultType) {
        try {
            return objectMapper.readValue(reader, resultType);
        } catch (IOException e) {
            throw new UncheckedSerializingException(e);
        }
    }
    //endregion

    //region 反序列化Json InputStream

    /**
     * Json反序列化
     *
     * @param inputStream Json字符串的InputStream
     */
    public <T> T deserialize(InputStream inputStream, Class<T> resultType) {
        try {
            return objectMapper.readValue(inputStream, resultType);
        } catch (IOException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json反序列化
     *
     * @param inputStream Json字符串的InputStream
     */
    public <T> T deserialize(InputStream inputStream, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(inputStream, valueTypeRef);
        } catch (IOException e) {
            throw new UncheckedSerializingException(e);
        }
    }

    /**
     * Json反序列化
     *
     * @param inputStream Json字符串的InputStream
     */
    public <T> T deserialize(InputStream inputStream, JavaType resultType) {
        try {
            return objectMapper.readValue(inputStream, resultType);
        } catch (IOException e) {
            throw new UncheckedSerializingException(e);
        }
    }
    //endregion

    /**
     * 获取类型工厂
     */
    public TypeFactory getTypeFactory() {
        return objectMapper.getTypeFactory();
    }

    /**
     * 创建 Json ObjectMapper
     *
     * @return ObjectMapper
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }
}
