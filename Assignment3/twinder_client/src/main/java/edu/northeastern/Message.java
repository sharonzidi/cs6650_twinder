package edu.northeastern;

import io.swagger.client.api.MatchesApi;
import io.swagger.client.api.StatsApi;
import io.swagger.client.api.SwipeApi;

public class Message {
    private final boolean isPill;
    private final SwipeApi swipeApi;
    private final MatchesApi matchesApi;
    private final StatsApi statsApi;

    public Message(boolean isPill) {
        this.isPill = isPill;
        swipeApi = null;
        matchesApi = null;
        statsApi = null;
    }

    public Message(SwipeApi swipeApi, MatchesApi matchesApi, StatsApi statsApi) {
        isPill = false;
        this.swipeApi = swipeApi;
        this.matchesApi = matchesApi;
        this.statsApi = statsApi;
    }

    public boolean isPill() {
        return isPill;
    }

    public SwipeApi getSwipeApi() {
        return swipeApi;
    }

    public MatchesApi getMatchesApi() {
        return matchesApi;
    }

    public StatsApi getStatsApi() {
        return statsApi;
    }
}
