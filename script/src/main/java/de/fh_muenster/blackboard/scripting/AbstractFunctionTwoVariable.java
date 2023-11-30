package de.fh_muenster.blackboard.scripting;

public abstract class AbstractFunctionTwoVariable extends AbstractFunction {

    AbstractFunction right;
    AbstractFunction left;

    public AbstractFunction getRight() {
        return right;
    }

    public AbstractFunction getLeft() {
        return left;
    }

    public void setRight(AbstractFunction right) {
        this.right = right;
    }

    public void setLeft(AbstractFunction left) {
        this.left = left;
    }
}
