package lang.ast.expr;

import lang.ast.NodeVisitor;
import java.util.ArrayList;
import lang.ast.expr.LValue;

public class FCall extends Exp {
    private String funcName;
    private ArrayList<Exp> args;
    private Exp lvalues; 


    public FCall(int line, int col, String funcName, ArrayList<Exp> args, Exp lvalues) {
        super(line, col);
        this.funcName = funcName;
        this.args = args;
        this.lvalues = lvalues;
    }

    public String getFuncName() {
        return funcName;
    }

    public ArrayList<Exp> getArgs() {
        return args;
    }

    public Exp getLvalues() {
        return lvalues;
    }

    @Override
    public void accept(NodeVisitor v) {
        v.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FCall{")
          .append("funcName='").append(funcName).append('\'')
          .append(", args=").append(args);
        if (lvalues != null) {
            sb.append(", lvalues=").append(lvalues);
        }
        sb.append('}');
        return sb.toString();
    }
}
