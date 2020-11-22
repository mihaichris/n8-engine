package com.example.n8engine.controller;

import com.example.n8engine.dto.StatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
final public class StatusController {

    @GetMapping()
    public StatusResponse status() {
        return new StatusResponse();
    }
}
