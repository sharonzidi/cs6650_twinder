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
        final String comment = generateRandomString(256);
        final SwipeDetails swipeDetails = new SwipeDetails();

        swipeDetails.setSwiper(generateRandomSwiperUserId());
        swipeDetails.setSwipee(generateRandomSwipeeUserId());
        swipeDetails.setComment(comment);

        return swipeDetails;
    }

    public static String generateRandomSwiperUserId() {
        return String.valueOf(random.nextInt(5000) + 1);
    }

    public static String generateRandomSwipeeUserId() {
        return String.valueOf(random.nextInt(1000000) + 1);
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
