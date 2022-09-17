package com.example.demo.Service;

import com.example.demo.Api.ApiResponse;
import com.example.demo.Entity.Comment;
import com.example.demo.Repository.CommentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;


@Transactional
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public Long addComment(Comment comment){
        commentRepository.save(comment);
        return comment.getId();
    }
}
