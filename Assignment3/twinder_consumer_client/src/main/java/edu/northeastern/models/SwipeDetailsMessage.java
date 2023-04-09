package edu.northeastern.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwipeDetailsMessage {
    private String swiper;
    private String swipee;
    private String comment;
    private String leftOrRight;
}
