package com.demo.reactive.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum DataValidation {
    NATIONALITY("National ID", "Passport", "Stamped Letter");

    final List<String> validValues;

    DataValidation(String... validValues) {
        this.validValues = Arrays.asList(validValues);
    }

    public static boolean isValid(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        for (DataValidation value : values()) {
            if (!value.validValues.contains(name)) {
                return true;
            }
        }
        return false;
    }
}
