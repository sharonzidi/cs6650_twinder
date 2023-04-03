package edu.northeastern.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SharedStatus {

    private boolean isRunning;
    @NonNull
    private ConcurrentHashMap<String, Integer> countMap;


    public SharedStatus() {
        this.isRunning = true;
        this.countMap = new ConcurrentHashMap<>();
    }
}
