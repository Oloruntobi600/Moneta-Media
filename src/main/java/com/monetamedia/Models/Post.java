package com.monetamedia.Models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {

    private Long id;
    private String content;
    private LocalDateTime creationdate;
    private int likesCount;
    private Long userId;

    public Post() {
        this.creationdate = LocalDateTime.now();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationdate = creationDate;
    }
    public LocalDateTime getCreationdate() {
        return creationdate;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
