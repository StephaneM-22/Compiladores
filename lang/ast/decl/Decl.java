package lang.ast.decl;

import lang.ast.Node;
import lang.ast.NodeVisitor;
import lang.ast.types.LType;

public class Decl extends Node {
    private String fieldName;
    private LType fieldType;

    public Decl(int line, int col, String fieldName, LType fieldType) {
        super(line, col);
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public LType getFieldType() {
        return fieldType;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }

}