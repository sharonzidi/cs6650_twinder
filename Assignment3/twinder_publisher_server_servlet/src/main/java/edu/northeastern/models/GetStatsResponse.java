package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetStatsResponse {
    private long numLlikes;
    private long numDislikes;
}
