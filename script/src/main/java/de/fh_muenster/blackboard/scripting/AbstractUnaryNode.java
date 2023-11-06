package de.fh_muenster.blackboard.scripting;

import java.util.Collections;
import java.util.List;

public abstract class AbstractUnaryNode<D> extends AstNode<D>{
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
