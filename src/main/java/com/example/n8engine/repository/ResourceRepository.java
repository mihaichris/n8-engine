package com.example.n8engine.repository;

import com.example.n8engine.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository  extends JpaRepository<Resource, Long> {

    @Query("SELECT r FROM Resource r WHERE r.uri = :uri")
    Resource findByUri(@Param("uri") String uri);
}
