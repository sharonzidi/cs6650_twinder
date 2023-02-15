package edu.northeastern.utils;

import io.swagger.client.api.SwipeApi;
import io.swagger.client.model.SwipeDetails;

import java.util.Arrays;
import java.util.Random;

public class Utils {
    private static final Random random = new Random(System.currentTimeMillis());

    private Utils() {
    }

    public static String getLeftOrRight() {
        return Arrays.asList("left", "right").get(random.nextInt(2));
    }

    public static SwipeDetails generateSwipeDetails() {
        final int swiper = random.nextInt(5000) + 1;
        final int swipee = random.nextInt(1000000) + 1;
        final String comment = generateRandomString(256);

        final SwipeDetails swipeDetails = new SwipeDetails();
        swipeDetails.setSwiper(String.valueOf(swiper));
        swipeDetails.setSwipee(String.valueOf(swipee));
        swipeDetails.setComment(comment);

        return swipeDetails;
    }

    private static String generateRandomString(final int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            sb.append((char) randomLimitedInt);
        }
        return sb.toString();
    }
}
