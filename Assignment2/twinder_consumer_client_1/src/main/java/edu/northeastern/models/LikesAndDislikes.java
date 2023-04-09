package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LikesAndDislikes {
    private final String swiperId;
    private final long likeCount;
    private final long dislikeCount;
}
