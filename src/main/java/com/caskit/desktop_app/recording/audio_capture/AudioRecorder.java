package com.caskit.desktop_app.recording.audio_capture;

import com.caskit.desktop_app.callback.FileCallback;
import com.caskit.desktop_app.executors.AsyncTaskHandler;
import com.caskit.desktop_app.recording.Recorder;

import javax.sound.sampled.*;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AudioRecorder extends Recorder {

    private TargetDataLine line;
    private Line.Info info;
    private String outputPath;

    public AudioRecorder(Line.Info info, String outputPath) {
        this.info = info;
        this.outputPath = outputPath;
    }

    @Override
    public void start() {
        super.start();
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(new AudioFormat(16000, 8, 2, true, true));
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(outputPath));
            } catch (Exception e) {
                e.printStackTrace();

                //TODO: THIS CAN CAUSE ERROR!!!!
            }
        });
    }

    public Future stop(FileCallback callback) {
        super.stop();
        return AsyncTaskHandler.submit(() -> {
            line.stop();
            line.close();
            if (callback != null) {
                callback.trigger(new File(outputPath));
            }
        });
    }

}