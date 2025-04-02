package lang.ast.expr;

import lang.ast.NodeVisitor;
import lang.ast.types.LType;

public class NewArray extends Exp {
    private LType type;
    private Exp sizeExp;

    public NewArray(int line, int col, LType type, Exp sizeExp) {
        super(line, col);
        this.type = type;
        this.sizeExp = sizeExp;
    }

    public LType getType() {
        return type;
    }

    public Exp getSizeExp() {
        return sizeExp;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + type + ", " + sizeExp + ")";
    }
}
