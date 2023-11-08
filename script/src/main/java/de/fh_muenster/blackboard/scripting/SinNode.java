/*
 * Project: Blackboard
 *
 * @author: Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;


/**
 * Sin operation node
 */
public class SinNode extends UnaryOperationNode{

    /**
     * The constructor for sinus nodes "sin(...)"
     *
     * @param child child node
     */
    SinNode(AST<?> child) {
        super(UnaryOperation.SIN, child);
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
