package com.northeastern.twinder;

import com.northeastern.twinder.models.SwipeDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/swipe")
public class Controller {

    @PostMapping("/")
    public ResponseEntity<?> signup(@RequestBody SwipeDetails swipeDetails) {

        return new ResponseEntity<>(swipeDetails, HttpStatus.OK);
    }
}
