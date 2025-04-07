package br.com.gabxdev.model.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum MessageStatus {
    SENT(0),

    RECEIVED(1),

    READ(2);

    public int status;
}
