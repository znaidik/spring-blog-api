package com.znaidi.springblogapi.controller;


import com.google.gson.reflect.TypeToken;
import com.znaidi.springblogapi.model.Comment;
import com.znaidi.springblogapi.model.Post;
import com.znaidi.springblogapi.repository.CommentRepository;
import com.znaidi.springblogapi.repository.PostRepository;
import com.znaidi.springblogapi.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CommentRepository commentRepository;
    @MockBean
    PostRepository postRepository;

    private final String URL = "/post/";

    @Test
    public void testCreateComment() throws Exception {

        // prepare data and mock's behaviour
        Post post = new Post(1l, "title 1", "Body Text 1");
        when(postRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(post));

        Comment comment = new Comment(2l, "Name 1", "Email1@email.tn", "Updated Body Comment Text 1");
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL+"{id}/comment", new Long(1)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(TestUtils.objectToJson(comment))).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(commentRepository).save(any(Comment.class));

        Comment resultComment = TestUtils.jsonToObject(result.getResponse().getContentAsString(), Comment.class);
        assertNotNull(resultComment);
        assertEquals(2l, resultComment.getId().longValue());

    }

    @Test
    public void testGetAllCommentsByPost() throws Exception {

        // prepare data and mock's behaviour
        List<Comment> commentList = buildComments();
        Post post = new Post(1l, "title 1", "Body Text 1", commentList);
        when(postRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(post));
        when(commentRepository.findAll()).thenReturn(commentList);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL+"{id}/comments", new Long(1)).accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(postRepository).findById(any(Long.class));

        // get the List<Comment> from the Json response
        TypeToken<List<Comment>> token = new TypeToken<List<Comment>>() {
        };
        @SuppressWarnings("unchecked")
        List<Comment> commentListResult = TestUtils.jsonToList(result.getResponse().getContentAsString(), token);

        assertNotNull("Comments not found", commentListResult);
        assertEquals("Incorrect Comment List", commentList.size(), commentListResult.size());

    }

    @Test
    public void testDeleteComment() throws Exception {
        // prepare data and mock's behaviour
        Comment comment = new Comment(1l);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(comment));

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "{id}/comments", new Long(1))).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(commentRepository).delete(any(Comment.class));

    }

    @Test
    public void testUpdateComment() throws Exception {
        // prepare data and mock's behaviour
        // here the comment is the Comment object with ID equal to ID
        // comment need to be updated
        Comment comment = new Comment(1l, "Name 1", "Email1@test.tn", "Updated Body Comment Text 1");
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(comment));

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(URL + "{id}/comments", new Long(1)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(TestUtils.objectToJson(comment))).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(commentRepository).save(any(Comment.class));

    }

    private List<Comment> buildComments() {
        Comment c1 = new Comment(1l, "Name 1", "Email1@test.tn", "Body Comment Text 1");
        Comment c2 = new Comment(2l, "Name 2", "Email2@test.tn", "Body Comment Text 2");
        List<Comment> commentList = Arrays.asList(c1, c2);
        return commentList;
    }

}
