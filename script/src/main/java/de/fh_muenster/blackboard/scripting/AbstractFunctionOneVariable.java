package de.fh_muenster.blackboard.scripting;

public abstract class AbstractFunctionOneVariable extends AbstractFunction{
    AbstractFunction child;

    public AbstractFunction getChild() {
        return child;
    }

    public void setChild(AbstractFunction child) {
        this.child = child;
    }
}
