package de.fh_muenster.blackboard.scripting;

public class ExpertenKoordinator extends AbstractAstVisitor{

    ValueVisitor valueVisitor = new ValueVisitor();

    FunctionVisitor functionVisitor = new FunctionVisitor();
    @Override
    public Double visit(LongValue n) {
        return null;
    }

    @Override
    public Double visit(DoubleValue n) {
        return null;
    }

    @Override
    public Object visit(Label n) {
        return null;
    }

    @Override
    public Object visit(OperationNode n) {
        return null;
    }

    @Override
    public Object visit(AssignNode n) {
        return null;
    }

    @Override
    public Object visit(UnaryOperationNode n) {
        return null;
    }

    @Override
    public Object visit(SemiNode n) {
        return null;
    }

    @Override
    public Object visit(FunctionNode n) {
        return null;
    }

    @Override
    public Object visit(FunctionAssignNode functionAssignNode) {
        return null;
    }

    @Override
    public Object visit(VariableNode variableNode) {
        return null;
    }

    @Override
    public Object visit(MasterNode masterNode) {
        return null;
    }
}
