package com.example.oauth.resourceserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Simple domain object for our API to return a message.
 */
@AllArgsConstructor
@Getter
public class Message {
    private final String message;
}
