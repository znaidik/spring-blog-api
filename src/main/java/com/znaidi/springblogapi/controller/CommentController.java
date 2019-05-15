package com.znaidi.springblogapi.controller;

import com.znaidi.springblogapi.exception.ResourceNotFoundException;
import com.znaidi.springblogapi.model.Comment;
import com.znaidi.springblogapi.model.Post;
import com.znaidi.springblogapi.repository.CommentRepository;
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
@Api(value = "Blog REST API", description = "Operations pertaining to comment in the Blog REST API")
public class CommentController {

    private static final Logger logger = LogManager.getLogger(CommentController.class);

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @ApiOperation(value = "View a list of available comments by post Id", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping("/post/{id}/comments")
    public ResponseEntity<List<Comment>> getAllComments(@ApiParam(value = "Post Id for which comments will be retrieved", required = true) @PathVariable(value = "id") Long postId) throws ResourceNotFoundException {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found for id :: "+ postId));

        return ResponseEntity.ok(post.getComments());
    }

    @ApiOperation(value = "Create a new comment in the post by Id", response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created comment"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Error when creating the comment")
    })
    @PostMapping("/post/{id}/comment")
    public ResponseEntity<Comment>  createComment(@ApiParam(value = "Post Id for which the comment object will be added", required = true) @PathVariable(value = "id") Long postId,
                                                  @ApiParam(value = "Comment object store in database table", required = true) @Valid @RequestBody Comment comment) throws ResourceNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found for id :: "+ postId));
        comment.setPost(post);
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    @ApiOperation(value = "Update a comment by Id", response = Comment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated comment by Id"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Error when updating the comment")
    })
    @PutMapping("/post/{id}/comments")
    public ResponseEntity<Comment> updateComment(
            @ApiParam(value = "Comment Id to update the specific comment object", required = true) @PathVariable(value = "id") Long commentId,
            @ApiParam(value = "Update comment object", required = true) @Valid @RequestBody Comment commentDetails) throws ResourceNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    logger.error("Comment with id (" + commentId + ") is not existed");
                    return new ResourceNotFoundException("Comment not found for id :: "+ commentId);
                });

        comment.setName(commentDetails.getName());
        comment.setEmail(commentDetails.getEmail());
        comment.setBody(commentDetails.getBody());
        final Comment updatedComment = commentRepository.save(comment);
        return ResponseEntity.ok(updatedComment);
    }

    @ApiOperation(value = "Delete a comment by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted comment by Id"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping("/post/{id}/comments")
    public ResponseEntity deleteComment(
            @ApiParam(value = "Comment Id to delete the specific comment object", required = true) @PathVariable(value = "id") Long commentId) throws ResourceNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found for id :: "+ commentId));

        commentRepository.delete(comment);
        return new ResponseEntity<>(new RestApiResponse(true, "You successfully deleted comment"), HttpStatus.OK);
    }

}
