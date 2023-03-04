package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SwipeDetailsRequest {
    private final String swiper;
    private final String swipee;
    private final String comment;
}
