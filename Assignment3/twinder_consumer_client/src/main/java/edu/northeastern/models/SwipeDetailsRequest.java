package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SwipeDetailsRequest {
    private String swiper;
    private String swipee;
    private String comment;
}
