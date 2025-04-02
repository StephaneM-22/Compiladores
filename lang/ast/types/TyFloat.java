package lang.ast.types;

import lang.ast.NodeVisitor;

public class TyFloat extends LType {
    public TyFloat(int line, int col) {
        super(line, col);
    }

    public String getTypeName() {
        return "Float";
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }
    
}
