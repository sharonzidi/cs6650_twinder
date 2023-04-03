package edu.northeastern.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetMatchesResponse {
    private List<String> matchList;
}
