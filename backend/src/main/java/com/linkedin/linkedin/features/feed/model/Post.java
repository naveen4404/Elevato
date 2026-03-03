package com.linkedin.linkedin.features.feed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkedin.linkedin.features.authentication.model.AuthenticationUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String picture = null;

    @ManyToOne
    @JoinColumn(nullable = false)
    private AuthenticationUser author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt = null;

    @ManyToMany
    @JoinTable(
        name = "posts_likes",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AuthenticationUser> likes;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;

    @PreUpdate
    public void changeUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public Post() {
    }
    public Post(String content, AuthenticationUser author) {
        this.content = content;
        this.author = author;
    }
    public Post(String content, String picture, AuthenticationUser author) {
        this.content = content;
        this.picture = picture;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public AuthenticationUser getAuthor() {
        return author;
    }

    public void setAuthor(AuthenticationUser author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<AuthenticationUser> getLikes() {
        return likes;
    }

    public void setLikes(Set<AuthenticationUser> likes) {
        this.likes = likes;
    }
}
