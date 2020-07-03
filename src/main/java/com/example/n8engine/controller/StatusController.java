package com.example.n8engine.controller;

import com.example.n8engine.reponse.StatusResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping()
    public StatusResponse status() {
        StatusResponse statusResponse = new StatusResponse();
        return statusResponse;
    }
}
