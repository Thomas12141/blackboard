package de.fh_muenster.blackboard.scripting;

public class Log2Node extends UnaryOperationNode{
    Log2Node(AST<?> child) {
        super(UnaryOperation.LB, child);
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
