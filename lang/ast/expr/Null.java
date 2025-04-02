package lang.ast.expr;

import lang.ast.NodeVisitor;

public class Null extends Exp {
    private Object value;

    public Null(int line, int col, Object value) {
        super(line, col);
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}