package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findById(Long id);
    List<Board> findAll();
    List<Board> findByMember(Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Board b SET b.view = b.view + 1 WHERE b.id = :id")
    int updateView(@Param("id") Long id);

}