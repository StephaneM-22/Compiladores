package lang.ast.decl;

import lang.ast.Node;
import lang.ast.types.LType;
import lang.ast.NodeVisitor;

public class Param extends Node {
    private String id;
    private LType type;

    public Param(int line, int column, String id, LType type) {
        super(line, column);
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public LType getType() {
        return type;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}