package com.example.n8engine.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Searches implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long Id;

    private String search;

    private Long queryRunningTime;

    private String type;

    public Searches(String search, Long queryRunningTime, String type) {
        this.search = search;
        this.queryRunningTime = queryRunningTime;
    }

    public Searches() {

    }
}
