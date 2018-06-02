package com.caskit.desktop_app.recording.screen_capture;


import com.caskit.desktop_app.callback.FileCallback;
import com.caskit.desktop_app.executors.AsyncTaskHandler;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.progress.ProgressListener;
import org.apache.commons.io.FileUtils;
import com.caskit.desktop_app.recording.Recorder;
import com.caskit.desktop_app.utils.FfmpegLocator;
import com.caskit.desktop_app.utils.FileHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ScreenRecorder extends Recorder {

    private static final Logger logger = Logger.getGlobal();

    private ProgressListener progressListener;
    private ExecutorService executorService;
    private boolean showMouse;
    private String outputPath;
    private File tempDir;
    private int fps, x, y, width, height;

    public ScreenRecorder(boolean showMouse, String outputPath, int fps, int x, int y, int width, int height) {
        this(showMouse, outputPath, fps, x, y, width, height, null);
    }

    public ScreenRecorder(boolean showMouse, String outputPath, int fps, int x, int y, int width, int height, ProgressListener progressListener) {
        this.showMouse = showMouse;
        this.outputPath = outputPath;
        this.tempDir = createTempDirectory();

        this.fps = fps;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.progressListener = progressListener;
    }

    @Override
    public void start() {
        super.start();

        final double frameInterval = (1D / fps) * 1000D;
        System.out.println("Recording at " + fps + " fps. Interval: " + frameInterval);

        executorService = Executors.newFixedThreadPool(4);
        AsyncTaskHandler.submit(() -> {
            int counter = 0;
            while (!isEnd()) {
                long start = System.currentTimeMillis();
                BufferedImage bufferedImage = Screenshot.create(showMouse, x, y, width, height);
                int finalI = counter++;
                executorService.submit(() -> {
                    String fileName = String.format("%04d", finalI) + ".png";
                    try {
                        FileHelper.saveImage(bufferedImage, tempDir.getAbsolutePath() + File.separator + fileName);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Unable to save image for screen capture [" + fileName + "]" + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                });

                long timeTaken = System.currentTimeMillis() - start;
                if (frameInterval > timeTaken) {
                    sleep((long) frameInterval - timeTaken);
                }
            }

            System.out.println("Finished Grabbing frames.");
        });

    }


    public synchronized Future stop(FileCallback fileCallback) {
        System.out.println("[Screen Recorder] Stopping recording");
        super.stop();

        return AsyncTaskHandler.submit(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(5, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Finished Saving frames");

            sleep(1000);

            try {
                if (!createVideo(tempDir.getAbsolutePath() + File.separator + "%04d.png")) {
                    if (fileCallback != null) {
                        fileCallback.trigger(null);
                    }
                    return;
                }

                if (fileCallback != null) {
                    fileCallback.trigger(new File(outputPath));
                }

            } finally {
                deleteTempDirectory(tempDir);
            }
        });
    }

    private boolean createVideo(String path) {
        System.out.println("Creating video: " + fps + "fps");
        long start = System.currentTimeMillis();
        try {
            System.out.println(FfmpegLocator.getFfmpeg());
            FFmpeg ffmpeg = new FFmpeg(FfmpegLocator.getFfmpeg());

            FFmpegBuilder builder = new FFmpegBuilder()
                    .addInput(path)
                    .addExtraArgs("-r", fps + "")
                    .addOutput(outputPath)
                    .setVideoCodec("libx264")
                    .setFormat("mp4")
                    .setVideoFrameRate(fps)
                    .addExtraArgs("-color_range", "2", "-pix_fmt", "yuvj420p")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

            if (progressListener != null) {
                executor.createJob(builder, progressListener).run();
            } else {
                executor.createJob(builder).run();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to generate screen capture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        System.out.println("Video generation took: " + (System.currentTimeMillis() - start) + "ms");
        return true;
    }

    private static void deleteTempDirectory(File file) {
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createTempDirectory() {
        File temp ;
        try {
            temp = File.createTempFile(UUID.randomUUID().toString(), "");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create temp file");
        }

        if (!temp.delete()) {
            throw new IllegalStateException("Failed to delete temp file");
        }

        if (!temp.mkdir()) {
            throw new IllegalStateException("Failed to create temp directory");
        }
        return temp;
    }

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
