package com.clp.repository;

import com.clp.entity.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {

    Optional<Writer> findByUser_Username(String username);
    Boolean existsByUser_Username(String username);
}
