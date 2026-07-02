package com.example.therapynow.utils;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors instancia;
    private final Executor fondo;
    private final Executor hiloPrincipal;

    private AppExecutors(Executor fondo, Executor hiloPrincipal) {
        this.fondo = fondo;
        this.hiloPrincipal = hiloPrincipal;
    }

    public static synchronized AppExecutors getInstance() {
        if (instancia == null) {
            instancia = new AppExecutors(
                    Executors.newSingleThreadExecutor(),
                    new MainThreadExecutor()
            );
        }
        return instancia;
    }

    public Executor deFondo() {
        return fondo;
    }

    public Executor deUI() {
        return hiloPrincipal;
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }
}