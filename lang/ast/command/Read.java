package lang.ast.command;

import lang.ast.expr.LValue;
import lang.ast.NodeVisitor;

public class Read extends Cmd {
    private LValue lvalue;

    public Read(int line, int column, LValue lvalue) {
        super(line, column);
        this.lvalue = lvalue;
    }

    public LValue getLValue() {
        return lvalue;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    
    @Override
    public String toString() {
        return "Read(" + lvalue + ")";
    }
}