package com.caskit.desktop_app.executors;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncTaskHandler {

    private static final int ASYNC_TASK_LIMIT = 5;

    private static AsyncTaskHandler asyncTaskHandler;
    private static AsyncTaskHandler getInstance() {
        return asyncTaskHandler != null ? asyncTaskHandler : (asyncTaskHandler = new AsyncTaskHandler());
    }

    private ExecutorService executorService;

    private AsyncTaskHandler() {
        executorService = Executors.newFixedThreadPool(ASYNC_TASK_LIMIT);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return getInstance().executorService.submit(callable);
    }

    public static Future submit(Runnable runnable) {
        return getInstance().executorService.submit(runnable);
    }

}
