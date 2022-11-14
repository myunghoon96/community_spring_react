package com.example.demo.Repository;

import com.example.demo.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findById(Long aLong);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
    List<Member> findAllByOrderByCreatedDateDesc();
}
