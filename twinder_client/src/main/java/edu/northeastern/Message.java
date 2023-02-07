package edu.northeastern;

import io.swagger.client.api.SwipeApi;

public class Message {
    public boolean isPill;
    public SwipeApi swipeApi;

    public Message(boolean isPill) {
        this.isPill = isPill;
        swipeApi = null;
    }

    public Message(SwipeApi swipeApi) {
        this.isPill = false;
        this.swipeApi = swipeApi;
    }
}
