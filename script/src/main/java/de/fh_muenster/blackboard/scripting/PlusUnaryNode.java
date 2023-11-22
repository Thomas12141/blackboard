/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

/**
 * implements the unary minus
 */
public class PlusUnaryNode extends UnaryOperationNode {

    /**
     * Constructor for a unary minus node.
     *
     * @param child Node
     */
    PlusUnaryNode(AST<?> child) {
        super(UnaryOperation.PLUS, child);
    }

    /**
     * (non-Javadoc)
     *
     * @see AST#accept(AstVisitor)
     */
    @Override
    public <V> V accept(AstVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
