package de.fh_muenster.blackboard.linear_algebra;

import java.util.Objects;

public enum Operationen {
    PLUS("+"), Times("*");
    private String operation;

    Operationen(String s) {
        operation = Objects.requireNonNull(s, "op is null");
    }

    @Override
    public String toString() {
        return operation;
    }
}
