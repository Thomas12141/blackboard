package de.fh_muenster.blackboard.scripting;

abstract class UnaryOperationNode extends AbstractUnaryNode<UnaryOperation>{


    /**
     * @param op    operand of this node
     * @param child the right node
     */
    UnaryOperationNode(UnaryOperation op, AST<?> child) {
        super(child, op);
    }

    /**
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("\"%s\"{%s}", data(), child());
    }
}
