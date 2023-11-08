/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

/**
 * Implements logarithmus naturalis
 */
public class LogNode extends UnaryOperationNode {

    /**
     * Constructor for a log node.
     *
     * @param child Node
     */
    LogNode(AST<?> child) {
        super(UnaryOperation.LN, child);
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
