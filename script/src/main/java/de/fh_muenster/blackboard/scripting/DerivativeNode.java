package de.fh_muenster.blackboard.scripting;

public class DerivativeNode extends FunctionNode{
    int grade;

    DerivativeNode(String function, AST<?> variables, int grade) {
        super(function, variables);
        this.grade = grade;
    }
}
