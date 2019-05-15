package com.znaidi.springblogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "comments")
@ApiModel(description = "All details about the Comment. ")
public class Comment {
    @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The database generated comment Id")
    private Long id;

    @Column(name = "name")
    @NotBlank
    @Size(min = 4, max = 50)
    @ApiModelProperty(notes = "The comment user name")
    private String name;

    @Column(name = "email")
    @NotBlank
    @Email
    @Size(min = 4, max = 50)
    @ApiModelProperty(notes = "The comment user email")
    private String email;

    @Column(name = "body")
    @NotBlank
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    @ApiModelProperty(notes = "The comment user body")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(){

    }

    public Comment(Long id) {
        this.id = id;
    }

    public Comment(Long id, @NotBlank @Size(min = 4, max = 50) String name, @NotBlank @Email @Size(min = 4, max = 50) String email, @NotBlank @Size(min = 10, message = "Comment body must be minimum 10 characters") String body) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public Comment(@NotBlank @Size(min = 10, message = "Comment body must be minimum 10 characters") String body) {
        this.body = body;
    }

    public Comment(Long id, @NotBlank @Size(min = 4, max = 50) String name, @NotBlank @Email @Size(min = 4, max = 50) String email, @NotBlank @Size(min = 10, message = "Comment body must be minimum 10 characters") String body, Post post) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.body = body;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonIgnore
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}