package com.znaidi.springblogapi.controller;

import com.znaidi.springblogapi.exception.ResourceNotFoundException;
import com.znaidi.springblogapi.model.Post;
import com.znaidi.springblogapi.repository.PostRepository;
import com.znaidi.springblogapi.util.RestApiResponse;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value = "Blog REST API", description = "Operations pertaining to post in the Blog REST API")
public class PostController {

    private static final Logger logger = LogManager.getLogger(PostController.class);

    @Autowired
    private PostRepository postRepository;

    @ApiOperation(value = "View a list of available posts", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @ApiOperation(value = "Get a post by Id", response = Post.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved post by Id"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(
            @ApiParam(value = "Post Id that will be retrieved", required = true) @PathVariable(value = "id") Long postId) throws ResourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    logger.error("Post with id (" + postId + ") is not existed");
                    return new ResourceNotFoundException("Post not found for id :: "+ postId);
                    });
        return ResponseEntity.ok().body(post);
    }

    @ApiOperation(value = "Create a new post", response = Post.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created post"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Error when creating the post")
    })
    @PostMapping("/posts")
    public ResponseEntity createPost(@ApiParam(value = "Post object store in database table", required = true) @Valid @RequestBody Post post) {
        return ResponseEntity.ok(postRepository.save(post));
    }

    @ApiOperation(value = "Update a post by Id", response = Post.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated post by Id"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Error when updating the post")
    })
    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> updatePost(
            @ApiParam(value = "Post Id to update the specific post object", required = true) @PathVariable(value = "id") Long postId,
            @ApiParam(value = "Update post object", required = true) @Valid @RequestBody Post postDetails) throws ResourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    logger.error("Post with id (" + postId + ") is not existed");
                    return new ResourceNotFoundException("Post not found for id :: "+ postId);
                });

        post.setTitle(postDetails.getTitle());
        post.setBody(postDetails.getBody());
        final Post updatedPost = postRepository.save(post);
        return ResponseEntity.ok(updatedPost);
    }

    @ApiOperation(value = "Delete a post by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted post by Id"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/posts/{id}")
    public ResponseEntity deletePost(
            @ApiParam(value = "Post Id to delete the specific post object", required = true) @PathVariable(value = "id") Long postId) throws ResourceNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found for id :: "+ postId));

        postRepository.delete(post);
        return new ResponseEntity<>(new RestApiResponse(true, "You successfully deleted post"), HttpStatus.OK);
    }

}
