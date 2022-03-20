package com.example.aurora.Model;

public class Post {
    String statusText,statusImg,postId,creatorId,createOn;

    public Post(String statusText, String statusImg, String postId, String creatorId, String createOn) {
        this.statusText = statusText;
        this.statusImg = statusImg;
        this.postId = postId;
        this.creatorId = creatorId;
        this.createOn = createOn;
    }

    public Post() {
    }

    public String getStatusText() {
        return statusText;
    }

    public String getStatusImg() {
        return statusImg;
    }

    public String getPostId() {
        return postId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getCreateOn() {
        return createOn;
    }
}
