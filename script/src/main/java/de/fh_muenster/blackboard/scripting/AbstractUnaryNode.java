/*
 * Project: Blackboard
 *
 * @author Thomas Fidorin and Djordy v. Rönn
 */
package de.fh_muenster.blackboard.scripting;

import java.util.Collections;
import java.util.List;

/**
 * A class, which AstNode to provide abstract unary nodes for unary operations.
 *
 * @param <D> Type of data to be processed.
 */
public abstract class AbstractUnaryNode<D> extends AstNode<D> {

    /**
     * Constructor for unary nodes
     *
     * @param child Node
     * @param data Data
     */
    AbstractUnaryNode(AST<?> child, D data) {
        super(null, data);
        super.childs().add(child);
        child.setParent(this);
    }


    /**
     * (non-Javadoc)
     *
     * @see de.fh_muenster.blackboard.scripting.AstNode#childs()
     */
    @Override
    public final List<AST<?>> childs() {
        return Collections.unmodifiableList(super.childs());
    }

    /**
     * get the child AST
     *
     * @return child AST
     */
    public final AST<?> child() {
        return childs().get(0);
    }
}
