package lang.ast.decl;

import java.util.ArrayList;
import lang.ast.Node;
import lang.ast.NodeVisitor;

public class Data extends Node {
    private String id;
    private ArrayList<Decl> declList;

    public Data(int line, int column, String id, ArrayList<Decl> declList) {
        super(line, column);
        this.id = id;
        this.declList = declList;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Decl> getDeclList() {
        return declList;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        return "Data(" + id + ", " + declList + ")";
    }
}