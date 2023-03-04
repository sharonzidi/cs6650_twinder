package edu.northeastern;

import io.swagger.client.api.SwipeApi;

public class Message {
    private final boolean isPill;
    private final SwipeApi swipeApi;

    public Message(boolean isPill) {
        this.isPill = isPill;
        swipeApi = null;
    }

    public Message(SwipeApi swipeApi) {
        isPill = false;
        this.swipeApi = swipeApi;
    }

    public boolean isPill() {
        return isPill;
    }

    public SwipeApi getSwipeApi() {
        return swipeApi;
    }
}
