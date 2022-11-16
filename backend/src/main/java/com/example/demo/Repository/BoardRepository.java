package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
//    List<Board> findAll();
    Page<Board> findAll(Pageable pageable);
    List<Board> findByMember(Member member);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Board b SET b.view = :view WHERE b.id = :id")
    int updateView(@Param("id") Long id, @Param("view") int view);

    @Query(value = "SELECT view FROM board WHERE board.id = :id", nativeQuery = true)
    int findViewByBoardId(@Param("id") Long boardId);

    boolean existsByTitle(String title);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Board b SET b.view = :view WHERE b.title = :title")
    int updateViewByTitle(@Param("title") String title, @Param("view") int view);

}
