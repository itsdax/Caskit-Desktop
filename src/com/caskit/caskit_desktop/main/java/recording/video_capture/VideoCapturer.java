package recording.video_capture;

import callback.FileCallback;
import exceptions.RecorderException;
import executors.AsyncTaskHandler;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.commons.io.FileUtils;
import recording.Recorder;
import recording.audio_capture.AudioRecorder;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import recording.screen_capture.ScreenRecorder;
import utils.FfmpegLocator;

import javax.sound.sampled.Line;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

public class VideoCapturer extends Recorder {

    private boolean showMouse;
    private String outputPath;
    private Line.Info lineInfo;
    private int fps;
    private int x;
    private int y;
    private int width;
    private int height;

    private AudioRecorder audioRecorder;
    private ScreenRecorder screenRecorder;

    private ProgressListener progressListener;

    /**
     *
     * @param showMouse show cursor in recording
     * @param outputPath output path of the final output result
     * @param lineInfo audio input line info
     * @param fps
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public VideoCapturer(boolean showMouse, String outputPath, Line.Info lineInfo, int fps, int x, int y, int width, int height, ProgressListener progressListener) throws RecorderException {
        this.showMouse = showMouse;
        this.outputPath = outputPath;
        this.lineInfo = lineInfo;
        this.fps = fps;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.progressListener = progressListener;

        if (this.width % 2 == 1) {
            this.width--;
        }

        if (this.height % 2 == 1) {
            this.height--;
        }

        if (this.width * this.height <= 1) {
            throw new RecorderException("Video dimensions too small!");
        }


        if (lineInfo != null) {
            try {
                audioRecorder = new AudioRecorder(lineInfo, File.createTempFile("audio",".wav").getAbsolutePath());
            } catch (IOException e) {
                throw new RecorderException("Could not create audio recorder. Root cause: " + e.getMessage());
            }
        }

        try {
            screenRecorder = new ScreenRecorder(showMouse, File.createTempFile("capture",".mp4").getAbsolutePath(), this.fps, this.x, this.y, this.width, this.height, progressListener);
        } catch (IOException e) {
            throw new RecorderException("Could not create screen recorder. Root cause: " + e.getMessage());
        }

        System.out.println("Created video capturer with " + this.fps + " and " + (this.lineInfo != null ? "" : "no") + " audio [" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + "]");
    }

    public void start() {
        super.start();
        if (lineInfo != null) {
            audioRecorder.start();
        }
        screenRecorder.start();
    }


    public Future stop(FileCallback fileCallback) {
        super.stop();

        return AsyncTaskHandler.submit(() -> {
            if (lineInfo == null) {
                screenRecorder.stop(file -> {
                    try {
                        File outputFile = new File(outputPath);
                        FileUtils.copyFile(file, outputFile);
                        if (fileCallback != null) {
                            fileCallback.trigger(outputFile);
                        }
                    } catch (IOException e) {
                        throw new RecorderException("Could not generate capture.");
                    }
                });
                return;
            }


            final File[] data = new File[2];
            Future audioFuture = audioRecorder.stop(file -> data[0] = file);
            Future screenFuture = screenRecorder.stop(file -> data[1] = file);

            System.out.println("Waiting for audio and video to be done...");

            while (!audioFuture.isDone() || !screenFuture.isDone()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Audio and video is done.");

            File audioFile = data[0], videoFile = data[1];

            System.out.println("Audio: " + audioFile.getAbsolutePath());
            System.out.println("Video: " + videoFile.getAbsolutePath());

            combineAudioAndVideo(audioFile, videoFile, outputPath, progressListener);

            if (fileCallback != null) {
                fileCallback.trigger(new File(outputPath));
            }
        });
    }

    private static void combineAudioAndVideo(File audio, File video, String outputPath, ProgressListener progressListener) {
        try {
            FFmpeg ffmpeg = new FFmpeg(FfmpegLocator.getFfmpeg());
            FFprobe ffprobe = new FFprobe(FfmpegLocator.getFfprobe());

            FFmpegBuilder builder = new FFmpegBuilder()
                    .addInput(video.getAbsolutePath())
                    .addInput(audio.getAbsolutePath())
                    .addOutput(outputPath)
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            if (progressListener != null) {
                executor.createJob(builder, progressListener).run();
            } else {
                executor.createJob(builder).run();
            }

        } catch (Exception e) {

        }
    }

}
