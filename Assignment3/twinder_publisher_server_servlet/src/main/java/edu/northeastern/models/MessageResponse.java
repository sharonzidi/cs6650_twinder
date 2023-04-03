package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageResponse {
    private String message;
}
