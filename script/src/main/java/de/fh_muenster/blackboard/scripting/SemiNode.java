package de.fh_muenster.blackboard.scripting;

public class SemiNode extends AbstractBinaryNode<Operation> {
    public SemiNode(AST<?> ls, AST<?> rs) {
        super(ls, Operation.SEMI, rs);
    }

    @Override
    public <V> V accept(AstVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean isRoot() {
        return super.isRoot();
    }

    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }
}
