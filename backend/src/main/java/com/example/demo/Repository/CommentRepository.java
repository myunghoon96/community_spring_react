package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Comment;
import com.example.demo.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAll();
    List<Comment> findByBoard(Board board);
    List<Comment> findByMember(Member member);
}
