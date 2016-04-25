package com.ortosoft.remember.common;

import android.content.Context;

/**
 * Created by dima on 23.04.2016.
 */
public final class RoboErrorReporter {

    private RoboErrorReporter() {}

    /**
     * Apply error reporting to a specified application context
     * @param context context for which errors are reported (used to get package name)
     */
    public static void bindReporter(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.inContext(context));
    }

    public static void reportError(Context context, Throwable error) {
        ExceptionHandler.reportOnlyHandler(context).uncaughtException(Thread.currentThread(), error);
    }

}
