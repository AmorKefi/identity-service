package com.example.identity.dto;

import org.springframework.http.HttpStatusCode;

public record StandardResponse(HttpStatusCode status, String message) {

}
