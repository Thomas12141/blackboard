/*
 * Project: Blackboard
 *
 * @author Thomas Fidorin and Djordy v. Rönn
 *
 * Class: SemiNode
 *
 */
package de.fh_muenster.blackboard.scripting;

import java.util.Objects;

/**
 * An enumeration for unary operations.
 */
public enum UnaryOperation {



    SIN("sin"), COS("cos"), PLUS("+"), MINUS("-"), EXP("exp"), ASIN("asin"), LN("ln"), ACOS("acos"), POW("pow"), LB("lb");

    private String op;

    /**
     * checks whether the unary operation is not null.
     * @param s
     */
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

    /**
     * Checks whether the operation actually is a unary operation.
     *
     * @param op the operation to check
     * @return return the operation
     */
    public static UnaryOperation of(String op) {
        for (UnaryOperation o : values()) {
            if (o.op.equals(op))
                return o;
        }
        throw new IllegalArgumentException("no operation: " + op);
    }

}


