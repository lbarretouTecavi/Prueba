package com.example.pruebafastadapter.data.models;

import com.example.pruebafastadapter.utils.UUIDGenerator;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String idUnique = UUIDGenerator.generateIdUnique();
    @NotNull
    private String content;
    private String created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private String updated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private boolean isCheck;
    private boolean isSincronized;
    @Generated(hash = 1489878373)
    public Task(Long id, String idUnique, @NotNull String content, String created,
            String updated, boolean isCheck, boolean isSincronized) {
        this.id = id;
        this.idUnique = idUnique;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.isCheck = isCheck;
        this.isSincronized = isSincronized;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getIdUnique() {
        return this.idUnique;
    }
    public void setIdUnique(String idUnique) {
        this.idUnique = idUnique;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreated() {
        return this.created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
    public String getUpdated() {
        return this.updated;
    }
    public void setUpdated(String updated) {
        this.updated = updated;
    }
    public boolean getIsCheck() {
        return this.isCheck;
    }
    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }
    public boolean getIsSincronized() {
        return this.isSincronized;
    }
    public void setIsSincronized(boolean isSincronized) {
        this.isSincronized = isSincronized;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", idUnique='" + idUnique + '\'' +
                ", content='" + content + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", isCheck=" + isCheck +
                ", isSincronized=" + isSincronized +
                '}';
    }
}