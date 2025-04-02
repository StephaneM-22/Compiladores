package lang.ast.decl;

import lang.ast.NodeVisitor;
import lang.ast.expr.LValue;

public class FieldAccess extends LValue {
    private LValue base;
    private String field;

    public FieldAccess(int line, int column, LValue base, String field) {
        super(line, column, base.getId());
        this.base = base;
        this.field = field;
    }

    public LValue getBase() {
        return base;
    }

    public String getField() {
        return field;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    
    @Override
    public String toString() {
        return "FieldAccess(" + base + "." + field + ")";
    }
}