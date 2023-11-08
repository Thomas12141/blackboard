/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

/**
 * An implementation for cos, which extends unary operation node.
 */
public class CosNode extends UnaryOperationNode {

    /**
     * Constructor for cos nodes.
     *
     * @param child Node
     */
    CosNode(AST<?> child) {
        super(UnaryOperation.COS, child);
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
