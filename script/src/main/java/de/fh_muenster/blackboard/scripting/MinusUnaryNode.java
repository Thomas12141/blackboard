package de.fh_muenster.blackboard.scripting;

public class MinusUnaryNode extends UnaryOperationNode{
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
