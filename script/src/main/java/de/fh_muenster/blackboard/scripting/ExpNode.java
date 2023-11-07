package de.fh_muenster.blackboard.scripting;


/**
 * Exp operation node
 */
public class ExpNode extends UnaryOperationNode{
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
