package lang.ast.expr;

import lang.ast.NodeVisitor;

public class UnaryMinus extends Exp {
    private Exp exp;

    public UnaryMinus(int line, int col, Exp exp) {
        super(line, col);
        this.exp = exp;
    }

    public Exp getExp() {
        return exp;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + exp + ")";
    }
}