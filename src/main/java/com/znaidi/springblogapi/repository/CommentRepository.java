package com.znaidi.springblogapi.repository;

import com.znaidi.springblogapi.model.Comment;
import com.znaidi.springblogapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
