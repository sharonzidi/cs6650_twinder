package com.northeastern.twinder;

import com.northeastern.twinder.models.SwipeDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@RestController
@RequestMapping("/swipe")
public class SwipeController {
    private static final Set<String> DIRECTION_SET = new HashSet<>(Arrays.asList("left", "right"));

    @PostMapping("/{direction}/")
    public ResponseEntity<?> swipePost(@RequestBody SwipeDetails swipeDetails,
                                       @PathVariable("direction") String direction) {

        System.out.println("Direction: " + direction);
        System.out.println("SwipeDetails: " + swipeDetails);

        // validate SwipeDetails payload body object
        if (Objects.isNull(swipeDetails)
                || Objects.isNull(swipeDetails.getSwiper())
                || Objects.isNull(swipeDetails.getSwipee())
                || Objects.isNull(swipeDetails.getComment())) {
            System.out.println("Bad swipeDetails payload body: " + swipeDetails);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // validate direction query parameter
        if (Objects.isNull(direction) || !DIRECTION_SET.contains(direction)) {
            System.out.println("Bad direction parameter: " + direction);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // return dummy payload as response
        return new ResponseEntity<>(swipeDetails, HttpStatus.CREATED);
    }
}
