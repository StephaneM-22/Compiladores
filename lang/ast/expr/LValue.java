package lang.ast.expr;

import lang.ast.Node;
import lang.ast.NodeVisitor;
import lang.ast.expr.Exp;
import lang.ast.types.TyId;

public class LValue extends Exp {
    private final String id;

    public LValue(int line, int column, String id) {
        super(line, column);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    
    @Override
    public String toString() {
        return "LValue(" + id + ")";
    }
}