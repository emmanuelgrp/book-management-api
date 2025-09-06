package com.codemainlabs.book_management_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HeaderCheckController {

    @GetMapping("/headers")
    public Map<String, String> getHeaders(@RequestHeader Map<String, String> headers) {
        return headers;
    }
}