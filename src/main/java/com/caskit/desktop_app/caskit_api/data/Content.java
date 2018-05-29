package com.caskit.desktop_app.caskit_api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.caskit.desktop_app.utils.Jsonable;

import java.util.List;

public class Content implements Jsonable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("creationTime")
    private String creationTime;

    @JsonProperty("uploaderName")
    private String uploaderName;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("contentType")
    private ContentType contentType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("searchable")
    private boolean searchable;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("moderation")
    private Number moderation;

    public Content(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Number getModeration() {
        return moderation;
    }

    public void setModeration(Double moderation) {
        this.moderation = moderation;
    }

    @Override
    public String toString() {
        return toJSON();
    }
}
