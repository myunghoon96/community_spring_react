package com.example.demo.Repository;

import com.example.demo.Entity.Board;
import com.example.demo.Entity.Image;
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
public interface ImageRepository extends JpaRepository<Image, Long>{
    Optional<Image> findById(Long id);
    List<Image> findAll();
}