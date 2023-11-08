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
public class SemiNode extends AbstractBinaryNode<Operation> {

    /**
     * The constructor for the semi node ";".
     *
     * @param ls left child
     * @param rs right child
     */
    public SemiNode(AST<?> ls, AST<?> rs) {
        super(ls, Operation.SEMI, rs);
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

    /**
     * (non-Javadoc)
     *
     * @see de.fh_muenster.blackboard.scripting.AST#accept(de.fh_muenster.blackboard.scripting.AstVisitor)
     */
    @Override
    public boolean isRoot() {
        return super.isRoot();
    }

    /**
     * (non-Javadoc)
     *
     * @see de.fh_muenster.blackboard.scripting.AST#accept(de.fh_muenster.blackboard.scripting.AstVisitor)
     */
    @Override
    public boolean isLeaf() {
        return super.isLeaf();
    }
}
