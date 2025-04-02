package lang.ast.types;

import lang.ast.NodeVisitor;

public class TyId extends LType {

    private final String typeName;

    public TyId(int line, int col, String typeName) {
        super(line, col);
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}