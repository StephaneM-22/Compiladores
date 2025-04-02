package lang.ast.expr;

import lang.ast.NodeVisitor;

public class FloatLit extends Exp{
    private float value;
    public FloatLit(int line, int col, float value){
        super(line, col);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "FloatLit{" + "value=" + value + "}";
    }
}