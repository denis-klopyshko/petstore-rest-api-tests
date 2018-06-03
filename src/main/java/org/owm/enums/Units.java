package org.owm.enums;

import lombok.Getter;

@Getter
public enum Units {
    FAHRENHEIT("imperial"),
    CELCIUM("metric");

    private String unitName;

    Units(String unitName) {
        this.unitName = unitName;
    }
}
