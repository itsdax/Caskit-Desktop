package com.caskit.desktop_app.caskit_api.data;

import java.util.Arrays;

public enum ContentType {

    PNG ("png", "image/png"),
    JPG ("jpg", "image/jpeg"),
    GIF ("gif", "image/gif"),
    MP4 ("mp4", "video/mp4"),
    OTHER ("other", "text/plain");

    private String extension, mime;

    ContentType(String extension, String mime){
        this.extension = extension;
        this.mime = mime;
    }

    public String getExtension() {
        return extension;
    }

    public String getMime() {
        return mime;
    }

    public static ContentType getType(String extension) {
        if (extension == null){
            return OTHER;
        }
        return Arrays.stream(values()).filter(contentType -> contentType.getExtension().equals(extension)).findAny().orElse(null);
    }

}
