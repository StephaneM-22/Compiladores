package lang.ast.command;

import lang.ast.NodeVisitor;
import lang.ast.command.Cmd;
import java.util.ArrayList;
import lang.ast.expr.LValue;
import lang.ast.expr.Exp;

public class FunCall extends Cmd {
    private String funcName;
    private ArrayList<Exp> args;
    private ArrayList<LValue> lvalues; 


    public FunCall(int line, int col, String funcName, ArrayList<Exp> args, ArrayList<LValue> lvalues) {
        super(line, col);
        this.funcName = funcName;
        this.args = args;
        this.lvalues = lvalues;
    }

    // Construtor para chamada sem os lvalues opcionais.
    public FunCall(int line, int col, String funcName, ArrayList<Exp> args) {
        this(line, col, funcName, args, null);
    }

    public String getFuncName() {
        return funcName;
    }

    public ArrayList<Exp> getArgs() {
        return args;
    }

    public ArrayList<LValue> getLvalues() {
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
