package lang.ast.decl;

import java.util.ArrayList;

import lang.ast.Node;
import lang.ast.types.*;
import lang.ast.NodeVisitor;

public class FunDef extends Node {

    private String fname;
    private ArrayList<Param> params;
    private Block body;
    private ArrayList<LType> retrn;

    public FunDef(int l, int c, String fname, ArrayList<Param> params, ArrayList<LType> retrn, Block body) {
        super(l, c);
        this.fname = fname;
        this.params = params;
        this.retrn = retrn;
        this.body = body;
    }

    public String getFname() {
        return fname;
    }

    public ArrayList<Param> getParams() {
        return params;
    }

    public Block getBody() {
        return body;
    }

    public ArrayList<LType> getRetrn() {
        return retrn;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }
}