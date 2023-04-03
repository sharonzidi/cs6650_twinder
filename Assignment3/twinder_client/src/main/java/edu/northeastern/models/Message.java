package edu.northeastern.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Message {
    @NonNull
    private final MessageType messageType;
}
