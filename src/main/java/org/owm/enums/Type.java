package org.owm.enums;

import lombok.Getter;

@Getter
public enum Type {
    ACCURATE("accurate"),
    LIKE("like");

    private String value;

    Type(String value) {
        this.value = value;
    }
}
