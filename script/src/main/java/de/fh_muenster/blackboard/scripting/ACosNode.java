/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

/**
 * An implementation for acos, which extends unary operation node.
 */
public class ACosNode extends UnaryOperationNode {

    /**
     * Constructor for acos nodes.
     *
     * @param child Node
     */
    ACosNode(AST<?> child) {
        super(UnaryOperation.ACOS, child);
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
