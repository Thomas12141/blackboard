/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;


/**
 * Exp operation node
 */
public class ExpNode extends UnaryOperationNode {

    /**
     * Constructor for exp nodes.
     * @param child Node
     */
    ExpNode(AST<?> child) {
        super(UnaryOperation.EXP, child);
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
