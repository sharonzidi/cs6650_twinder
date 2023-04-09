package edu.northeastern.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class SwipeDetailsRequest {
    private String swiper;
    private String swipee;
    private String comment;
}
