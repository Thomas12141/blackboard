package de.fh_muenster.blackboard.scripting;

import java.util.Objects;
public enum UnaryOperation {



    SIN("sin"), COS("cos"), MINUS("-"), EXP("exp");
    UnaryOperation(String s) {
        op = Objects.requireNonNull(s, "op is null");
    }

    /**
     * (non-Javadoc)
     *
     * @see @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return op;
    }

    public static UnaryOperation of(String op) {
        for (UnaryOperation o : values()) {
            if (o.op.equals(op))
                return o;
        }
        throw new IllegalArgumentException("no operation: " + op);
    }

    private String op;
}
