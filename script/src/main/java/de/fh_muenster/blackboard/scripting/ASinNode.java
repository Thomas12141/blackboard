/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

/**
 * An implementation for arcsine, which extends unary operation node.
 */
public class ASinNode extends UnaryOperationNode {

    /**
     * Contructor for asin nodes.
     *
     * @param child Node
     */
    ASinNode(AST<?> child) {
        super(UnaryOperation.ASIN, child);
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
