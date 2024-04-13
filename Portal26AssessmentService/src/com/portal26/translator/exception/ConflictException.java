package com.portal26.translator.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ConflictException extends RuntimeException {

    @NonNull
    private String code;

    public ConflictException(@NonNull String message, @NonNull Exception cause) {
        super(message, cause);
    }
}
