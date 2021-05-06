package com.example.n8engine.repository;

import com.example.n8engine.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository  extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r WHERE r.uri = :uri")
    Optional<Resource> findByUri(@Param("uri") String uri);

    @Query("SELECT r FROM Resource r WHERE r.indexed = false ORDER BY r.Id ASC")
    List<Resource> findAllOrderedById();
}
