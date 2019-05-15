package com.znaidi.springblogapi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.znaidi.springblogapi.SpringBlogApiApplication;
import com.znaidi.springblogapi.model.Comment;
import com.znaidi.springblogapi.model.Post;
import com.znaidi.springblogapi.repository.CommentRepository;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CommentControllerITest {

    @LocalServerPort
    private int port;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    TestRestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddComment() {

        Comment comment = new Comment(4l, "Name 2", "Email2@email.tn", "Body Comment Text 2");
        HttpEntity<Comment> entity = new HttpEntity<Comment>(comment, headers);

        ResponseEntity<Comment> response = restTemplate.exchange(
                createURLWithPort("/post/3/comment"),
                HttpMethod.POST, entity, Comment.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Name 2", response.getBody().getName());
        assertEquals("Email2@email.tn", response.getBody().getEmail());
        assertEquals("Body Comment Text 2", response.getBody().getBody());
    }

    @Test
    public void testUpdateComment() {

        Comment comment = new Comment(4l, "Name 1", "Email1@email.tn", "Updated Body Comment Text 1");

        HttpEntity<Comment> entity = new HttpEntity<Comment>(comment, headers);

        ResponseEntity<Comment> response = restTemplate.exchange(
                createURLWithPort("/post/4/comments"),
                HttpMethod.PUT, entity, Comment.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals("Name 1", response.getBody().getName());
        assertEquals("Email1@email.tn", response.getBody().getEmail());
        assertEquals("Updated Body Comment Text 1", response.getBody().getBody());
    }


    @Test
    public void testRetrieveComments() throws JSONException, JsonProcessingException {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/post/3/comments"),
                HttpMethod.GET, entity, String.class);

        Post post = new Post(3l);
        List<Comment> comments = commentRepository.findAllByPost(post);

        String expected = mapper.writeValueAsString(comments);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void testDeleteComment() {

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/post/5/comments"),
                HttpMethod.DELETE, null, String.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
