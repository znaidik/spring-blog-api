package com.znaidi.springblogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "posts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "title"
        })
})
@ApiModel(description = "All details about the Post. ")
public class Post {

    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The database generated post Id")
    private Long id;

    @Column(name = "title")
    @ApiModelProperty(notes = "The post title (Unique)")
    private String title;

    @Column(name = "body")
    @ApiModelProperty(notes = "The post body")
    private String body;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post() {
    }

    public Post(Long id) {
        this.id = id;
    }

    public Post(Long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public Post(Long id, String title, String body, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
