package de.fh_muenster.blackboard.scripting;

public class CosNode extends UnaryOperationNode{
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
