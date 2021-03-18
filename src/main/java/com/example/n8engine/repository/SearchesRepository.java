package com.example.n8engine.repository;

import com.example.n8engine.domain.Searches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchesRepository extends JpaRepository<Searches, Long> {

    @Query("SELECT s FROM Searches s WHERE s.search=:search")
    public Optional<Searches> findBySearch(@Param("search") String search);
}
