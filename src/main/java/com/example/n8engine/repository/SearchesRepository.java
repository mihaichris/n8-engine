package com.example.n8engine.repository;

import com.example.n8engine.domain.Searches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchesRepository extends JpaRepository<Searches, Long> {
}
