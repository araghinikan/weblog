package com.nikan.weblog.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Tag {
    private int id;
    private String name;
    private String slug;
    private List<Post> posts;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    public List<Post> getPost() {
        return posts;
    }

    public void setPost(List<Post> posts) {
        this.posts = posts;
    }

    @PrePersist
    @PreUpdate
    public void generateSlug() {
        if (this.slug == null || this.slug.isBlank()) {
            String base = this.name + "-" + this.id;
            this.slug = slugify(base);
        }
    }

    private String slugify(String input) {
        if (input == null) return null;
        return input.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}
