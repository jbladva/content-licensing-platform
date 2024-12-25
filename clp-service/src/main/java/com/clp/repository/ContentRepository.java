package com.clp.repository;

import com.clp.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findAllByWriter_User_Id(Long id);
    Optional<Content> findByIdAndWriter_User_Id(Long id, Long writerId);
}
