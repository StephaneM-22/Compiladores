package lang.ast.types;

import lang.ast.NodeVisitor;

public class TyArray extends LType {
    private LType ty;

    public TyArray(int line, int col, LType ty) {
        super(line, col);
        this.ty = ty;
    }

    public String getTypeName() {
        return "Array";
    }

    public LType getTy() {
        return ty;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }

}
