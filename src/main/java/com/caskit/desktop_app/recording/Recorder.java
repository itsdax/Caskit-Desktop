package com.caskit.desktop_app.recording;


import com.caskit.desktop_app.callback.FileCallback;

import java.util.concurrent.Future;

public abstract class Recorder {

    private boolean started;
    private boolean end;

    protected Recorder() {
        this.started = false;
        this.end = false;
    }

    /**
     * Async method
     */
    public void start() {
        if (end) {
            throw new IllegalStateException("Recorder instance has been already used.");
        }
        if (started) {
            throw new IllegalStateException("Recorder has already started");
        }
        this.started = true;
    }


    protected synchronized void stop() {
        if (end) {
            throw new IllegalStateException("Recorder is already stopped.");
        }
        if (!started) {
            throw new IllegalStateException("Can not stop a recorder that had not started.");
        }
        this.started = false;
        this.end = true;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isEnd() {
        return end;
    }

    public abstract Future stop(FileCallback callback);
}
