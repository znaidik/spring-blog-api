package com.znaidi.springblogapi.controller;


import com.google.gson.reflect.TypeToken;
import com.znaidi.springblogapi.exception.ErrorDetails;
import com.znaidi.springblogapi.model.Post;
import com.znaidi.springblogapi.repository.PostRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;

    private final String URL = "/posts/";

    @Test
    public void testCreatePost() throws Exception {

        // prepare data and mock's behaviour
        Post post = new Post(1l, "title 1", "Body Text 1");
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(TestUtils.objectToJson(post))).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(postRepository).save(any(Post.class));

        Post resultPost = TestUtils.jsonToObject(result.getResponse().getContentAsString(), Post.class);
        assertNotNull(resultPost);
        assertEquals(1l, resultPost.getId().longValue());

    }

    @Test
    public void testGetPostById() throws Exception {

        // prepare data and mock's behaviour
        Post post = new Post(1l, "title 1", "Body Text 1");
        when(postRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(post));

        // execute
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "{id}", new Long(1))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(postRepository).findById(any(Long.class));

        Post resultPost = TestUtils.jsonToObject(result.getResponse()
                .getContentAsString(), Post.class);
        assertNotNull(resultPost);
        assertEquals(1l, resultPost.getId().longValue());
    }

    @Test
    public void testGetPostByIdNotExist() throws Exception {

        // prepare data and mock's behaviour
        // Not Required as post Not Exist scenario

        // execute
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get(URL + "{id}", new Long(1000)).accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.NOT_FOUND.value(), status);

        // verify that service method was called once
        verify(postRepository).findById(any(Long.class));

        ErrorDetails resultError = TestUtils.jsonToObject(result.getResponse().getContentAsString(), ErrorDetails.class);
        assertEquals(resultError.getMessage(), "Post not found for id :: 1000");
    }

    @Test
    public void testGetAllPosts() throws Exception {

        // prepare data and mock's behaviour
        List<Post> postList = buildPosts();
        when(postRepository.findAll()).thenReturn(postList);

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(URL).accept(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(postRepository).findAll();

        // get the List<Post> from the Json response
        TypeToken<List<Post>> token = new TypeToken<List<Post>>() {
        };
        @SuppressWarnings("unchecked")
        List<Post> postListResult = TestUtils.jsonToList(result.getResponse().getContentAsString(), token);

        assertNotNull("Posts not found", postListResult);
        assertEquals("Incorrect Post List", postList.size(), postListResult.size());

    }

    @Test
    public void testDeletePost() throws Exception {
        // prepare data and mock's behaviour
        Post post = new Post(1l);
        when(postRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(post));

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "{id}", new Long(1))).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(postRepository).delete(any(Post.class));

    }

    @Test
    public void testUpdatePost() throws Exception {
        // prepare data and mock's behaviour
        // here the post is the Post object with ID equal to ID
        // post need to be updated
        Post post = new Post(1l, "title 1", "Updated Body Text 1");
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(post));

        // execute
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(URL + "{id}", new Long(1)).contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8).content(TestUtils.objectToJson(post))).andReturn();

        // verify
        int status = result.getResponse().getStatus();
        assertEquals("Incorrect Response Status", HttpStatus.OK.value(), status);

        // verify that service method was called once
        verify(postRepository).save(any(Post.class));

    }

    private List<Post> buildPosts() {
        Post p1 = new Post(1l, "Title 1", "Body Text 1");
        Post p2 = new Post(2l, "Title 2", "Body Text 2");
        List<Post> postList = Arrays.asList(p1, p2);
        return postList;
    }

}
