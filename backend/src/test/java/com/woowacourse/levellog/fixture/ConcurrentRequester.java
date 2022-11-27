package com.woowacourse.levellog.fixture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentRequester {

    private final ExecutorService executorService;
    private final CountDownLatch countDownLatch;
    private final AtomicInteger successRequest;
    private final AtomicInteger failRequest;

    public ConcurrentRequester(final int threadPoolSize) {
        this.executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.countDownLatch = new CountDownLatch(threadPoolSize);
        this.successRequest = new AtomicInteger(0);
        this.failRequest = new AtomicInteger(0);
    }

    public void execute(final RequestExecutor executor) {
        executorService.execute(() -> {
                    try {
                        final RestAssuredResponse response = executor.execute();
                        if (response.isSuccess()) {
                            successRequest.incrementAndGet();
                        } else {
                            failRequest.incrementAndGet();
                        }
                        countDownLatch.countDown();
                    } catch (final Exception e) {
                        failRequest.incrementAndGet();
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }

    public int getSuccessRequest() {
        return successRequest.get();
    }

    public int getFailRequest() {
        return failRequest.get();
    }
}
