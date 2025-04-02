package lang.ast.expr;

import lang.ast.expr.Exp;
import lang.ast.types.LType;
import lang.ast.NodeVisitor;

public class Instanciate extends Exp {
    private LType ty;
    private Exp e;

    public Instanciate(int l, int c, LType ty, Exp e) {
        super(l, c);
        this.ty = ty;
        this.e = e;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    public LType getTy() {
        return ty;
    }

    public Exp getE() {
        return e;
    }

}
