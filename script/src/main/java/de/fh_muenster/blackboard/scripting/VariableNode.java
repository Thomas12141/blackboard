/*
 * Project: Blackboard
 *
 * @author Thomas Fidorin and Djordy v. Rönn
 *
 * Class: SemiNode
 *
 */
package de.fh_muenster.blackboard.scripting;

/**
 * Implements a semicolon node.
 */
public class VariableNode extends AbstractBinaryNode<Operation> {

    /**
     * The constructor for the semi node ",".
     *
     * @param ls left child
     * @param rs right child
     */
    public VariableNode(AST<?> ls, AST<?> rs) {
        super(ls, Operation.KOMMA, rs);
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

    /**
     * (non-Javadoc)
     *
     * @see AST#accept(AstVisitor)
     */
    @Override
    public boolean isRoot() {
        return super.isRoot();
    }

    /**
     * (non-Javadoc)
     *
     * @see AST#accept(AstVisitor)
     */
    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }


    /**
     * (non-Javadoc)
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format("\"%s\"{%s,%s}", data(), left(), right());
    }
}
