package tech.simmy.wechat.util;

/**
 * 序列化异常
 */
public class UncheckedSerializingException extends RuntimeException {

    public UncheckedSerializingException(Throwable cause) {
        super(cause);
    }

    public UncheckedSerializingException(String message, Throwable cause) {
        super(message, cause);
    }
}
