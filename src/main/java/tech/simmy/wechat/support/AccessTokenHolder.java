package tech.simmy.wechat.support;

import tech.simmy.wechat.apiclient.AccessToken;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class AccessTokenHolder {

    private static final int THREAD_POOL_SIZE = 2;
    private static final int UPDATE_BEFORE_SECONDS = 300;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Supplier<AccessToken> tokenSupplier;
    private final ScheduledExecutorService scheduledExecutorService;

    private AccessToken value;

    public AccessTokenHolder(Supplier<AccessToken> tokenSupplier, ScheduledExecutorService scheduledExecutorService) {
        this.tokenSupplier = Objects.requireNonNull(tokenSupplier, "Token supplier is required");
        this.scheduledExecutorService = Objects.requireNonNull(scheduledExecutorService, "ScheduledExecutorService is required");
    }

    public AccessTokenHolder(Supplier<AccessToken> tokenSupplier) {
        this(tokenSupplier, new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE));
    }

    public AccessToken getValue() {

        AccessToken currentValue = readCurrent();

        if (currentValue == null) {
            currentValue = requestNew();
        }

        return currentValue;
    }

    private AccessToken readCurrent() {
        AccessToken currentValue;
        lock.readLock().lock();

        try {
            currentValue = this.value;
        } finally {
            lock.readLock().unlock();
        }
        return currentValue;
    }


    private AccessToken requestNew() {
        AccessToken newValue;
        lock.writeLock().lock();
        try {
            newValue = tokenSupplier.get();
            this.value = newValue;
        } finally {
            lock.writeLock().unlock();
        }
        setUpdater(newValue);
        return newValue;
    }

    private void setUpdater(AccessToken value) {
        long delaySecond = value.expiredTime().getEpochSecond() - Instant.now().getEpochSecond();

        if (delaySecond > UPDATE_BEFORE_SECONDS) {
            delaySecond = delaySecond - UPDATE_BEFORE_SECONDS;
        }

        scheduledExecutorService.schedule(this::requestNew, delaySecond, TimeUnit.SECONDS);
    }

}
