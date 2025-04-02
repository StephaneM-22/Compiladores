package lang.ast.command;

import java.util.ArrayList;

import lang.ast.Node;
import lang.ast.expr.Exp;
import lang.ast.NodeVisitor;

public class Return extends Cmd {

    private ArrayList<Exp> e;

    public Return(int line, int col, ArrayList<Exp> e) {
        super(line, col);
        this.e = e;
    }

    public ArrayList<Exp> getExp() {
        return e;
    }

    public void accept(NodeVisitor v) {
        v.visit(this);
    }

}
