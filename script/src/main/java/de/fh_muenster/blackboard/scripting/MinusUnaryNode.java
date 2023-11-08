/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

/**
 * implements the unary minus
 */
public class MinusUnaryNode extends UnaryOperationNode {

    /**
     * Constructor for a unary minus node.
     *
     * @param child Node
     */
    MinusUnaryNode(AST<?> child) {
        super(UnaryOperation.MINUS, child);
    }

    /**
     * (non-Javadoc)
     *
     * @see de.fh_muenster.blackboard.scripting.AST#accept(de.fh_muenster.blackboard.scripting.AstVisitor)
     */
    @Override
    public <V> V accept(AstVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
