package com.caskit.desktop_app.callback;

import com.caskit.desktop_app.structures.CaptureStatus;


public interface CaptureCallback {
    void trigger(CaptureStatus captureStatus);
}
