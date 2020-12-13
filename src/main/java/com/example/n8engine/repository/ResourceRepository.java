package com.example.n8engine.repository;

import com.example.n8engine.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository  extends JpaRepository<Resource, Long> {
}
