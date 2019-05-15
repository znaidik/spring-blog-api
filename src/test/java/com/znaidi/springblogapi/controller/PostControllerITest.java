package com.znaidi.springblogapi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.znaidi.springblogapi.SpringBlogApiApplication;
import com.znaidi.springblogapi.model.Post;
import com.znaidi.springblogapi.repository.PostRepository;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBlogApiApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerITest {

    @LocalServerPort
    private int port;

    @Autowired
    PostRepository postRepository;

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddPost() {

        Post post = new Post(1l, "Title 1", "Body Text Description 1");

        HttpEntity<Post> entity = new HttpEntity<Post>(post, headers);

        ResponseEntity<Post> response = restTemplate.exchange(
                createURLWithPort("/posts"),
                HttpMethod.POST, entity, Post.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Title 1", response.getBody().getTitle());
        assertEquals("Body Text Description 1", response.getBody().getBody());
    }

    @Test
    public void testUpdatePost() {

        Post post = new Post(2l, "Title 2", "Updated Body Text Description 2");

        HttpEntity<Post> entity = new HttpEntity<Post>(post, headers);

        ResponseEntity<Post> response = restTemplate.exchange(
                createURLWithPort("/posts/2"),
                HttpMethod.PUT, entity, Post.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Title 2", response.getBody().getTitle());
        assertEquals("Updated Body Text Description 2", response.getBody().getBody());
    }


    @Test
    public void testRetrievePosts() throws JSONException, JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/posts"),
                HttpMethod.GET, entity, String.class);

        List<Post> postList = postRepository.findAll();

        String expected = mapper.writeValueAsString(postList);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void testDeletePost() {

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/posts/2"),
                HttpMethod.DELETE, null, String.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
