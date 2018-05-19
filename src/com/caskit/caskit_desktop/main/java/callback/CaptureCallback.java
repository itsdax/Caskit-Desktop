package callback;

import structures.CaptureStatus;


public interface CaptureCallback {
    void trigger(CaptureStatus captureStatus);
}
