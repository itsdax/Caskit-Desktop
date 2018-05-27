package com.caskit.desktop_app.app_data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.caskit.desktop_app.recording.audio_capture.InputDevices;
import com.caskit.desktop_app.structures.Macro;
import com.caskit.desktop_app.utils.Jsonable;


public class CaskitPreferences implements Jsonable {


    public static final CaskitPreferences DEFAULT = new CaskitPreferences(
            Macro.ALT_C,
            Macro.ALT_V,
            true,
            InputDevices.getDefaultMixerName(),
            18,
            0,
            AppData.getDefaultCaptureDirectory().getAbsolutePath(),
            true,
            true,
            true,
            true
    );

    // Keybindings

    @JsonProperty("screenshotMacro")
    private Macro screenshotMacro;

    @JsonProperty("recordMacro")
    private Macro recordMacro;

    // Video
    @JsonProperty("includeAudio")
    private boolean includeAudio;

    @JsonProperty("audioInput")
    private String audioInput;

    @JsonProperty("fps")
    private int fps;

    @JsonProperty("recordingDelay")
    private int recordingDelay;

    // General
    @JsonProperty("workingDirectory")
    private String workingDirectory;

    @JsonProperty("openAfterComplete")
    private boolean openAfterComplete;

    @JsonProperty("useDirectURL")
    private boolean useDirectURL;

    @JsonProperty("visualizeCursor")
    private boolean visualizeCursor;

    @JsonProperty("uploadToCaskit")
    private boolean uploadToCaskit;

    public CaskitPreferences() {}

    public CaskitPreferences(Macro screenshotMacro, Macro recordMacro, boolean includeAudio, String audioInput, int fps, int recordingDelay, String workingDirectory, boolean openAfterComplete, boolean useDirectURL, boolean visualizeCursor, boolean uploadToCaskit) {
        this.screenshotMacro = screenshotMacro;
        this.recordMacro = recordMacro;
        this.includeAudio = includeAudio;
        this.audioInput = audioInput;
        this.fps = fps;
        this.recordingDelay = recordingDelay;
        this.workingDirectory = workingDirectory;
        this.openAfterComplete = openAfterComplete;
        this.useDirectURL = useDirectURL;
        this.visualizeCursor = visualizeCursor;
        this.uploadToCaskit = uploadToCaskit;
    }

    public Macro getScreenshotMacro() {
        return screenshotMacro;
    }

    public void setScreenshotMacro(Macro screenshotMacro) {
        this.screenshotMacro = screenshotMacro;
    }

    public Macro getRecordMacro() {
        return recordMacro;
    }

    public void setRecordMacro(Macro recordMacro) {
        this.recordMacro = recordMacro;
    }

    public boolean isIncludeAudio() {
        return includeAudio;
    }

    public void setIncludeAudio(boolean includeAudio) {
        this.includeAudio = includeAudio;
    }

    public String getAudioInput() {
        return audioInput;
    }

    public void setAudioInput(String audioInput) {
        this.audioInput = audioInput;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getRecordingDelay() {
        return recordingDelay;
    }

    public void setRecordingDelay(int recordingDelay) {
        this.recordingDelay = recordingDelay;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public boolean isOpenAfterComplete() {
        return openAfterComplete;
    }

    public void setOpenAfterComplete(boolean openAfterComplete) {
        this.openAfterComplete = openAfterComplete;
    }

    public boolean isUseDirectURL() {
        return useDirectURL;
    }

    public void setUseDirectURL(boolean useDirectURL) {
        this.useDirectURL = useDirectURL;
    }

    public boolean isVisualizeCursor() {
        return visualizeCursor;
    }

    public void setVisualizeCursor(boolean visualizeCursor) {
        this.visualizeCursor = visualizeCursor;
    }

    public boolean isUploadToCaskit() {
        return uploadToCaskit;
    }

    public void setUploadToCaskit(boolean uploadToCaskit) {
        this.uploadToCaskit = uploadToCaskit;
    }

    @Override
    public String toString() {
        return "CaskitPreferences{" +
                "screenshotMacro=" + screenshotMacro +
                ", recordMacro=" + recordMacro +
                ", includeAudio=" + includeAudio +
                ", audioInput='" + audioInput + '\'' +
                ", fps=" + fps +
                ", recordingDelay=" + recordingDelay +
                ", openAfterComplete=" + openAfterComplete +
                ", useDirectURL=" + useDirectURL +
                ", visualizeCursor=" + visualizeCursor +
                '}';
    }
}
