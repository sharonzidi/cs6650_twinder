package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusResponse<T> {
    private final T data;
}
