package edu.northeastern.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SwipeData {

    private String userId;
    private int numLikes;
    private int numDislikes;
    private List<String> likedSwipeeIds;

    public SwipeData(String userId) {
        this.userId = userId;
        this.numLikes = 0;
        this.numDislikes = 0;
        this.likedSwipeeIds = new ArrayList<>();
    }

    public void handleMessage(final SwipeDetailsMessage message) {
        if ("left".equals(message.getLeftOrRight())) {
            numDislikes++;
        } else {
            numLikes++;
            likedSwipeeIds.add(message.getSwipee());
        }
    }
}
