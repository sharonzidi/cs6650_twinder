package com.northeastern.twinder;

import com.northeastern.twinder.models.SwipeDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/swipe")
public class Controller {

    @PostMapping("/{direction}/")
    public ResponseEntity<?> signup(@RequestBody SwipeDetails swipeDetails,
                                    @PathVariable("direction") String direction) {

        System.out.println("Direction: " + direction);
        System.out.println("SwipeDetails: " + swipeDetails);

        return new ResponseEntity<>(swipeDetails, HttpStatus.OK);
    }
}
