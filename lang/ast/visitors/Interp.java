package lang.ast.visitors;

import lang.ast.expr.*;
import lang.ast.command.*;
import lang.ast.decl.*;
import lang.ast.types.*;
import java.util.*;
import java.util.concurrent.Callable;

import lang.ast.NodeVisitor;
import lang.ast.Program;

public class Interp extends NodeVisitor {

    public Stack<Object> stk;
    private final Map<String, Object> varTable;

    public Interp() {
        stk = new Stack<Object>();
        varTable = new HashMap<>();
    }

    public Object getStackTop() {
        return stk.peek();
    }

    @Override
    public void visit(Assign c) {
        c.getExp().accept(this);
        Object value = stk.pop();

        if (c.getLValue() != null) {
            LValue var = (LValue) c.getLValue();
            String varName = var.getId();

            varTable.put(varName, value);
        } else {
            throw new RuntimeException("Atribuição inválida! O lado esquerdo não é uma variável.");
        }
    }

    // Método para obter o valor de uma variável
    public Object getVarValue(String id) {
        return varTable.get(id);
    }

    // Método para definir o valor de uma variável
    public void setVarValue(String id, Object value) {
        varTable.put(id, value);
    }



    // exp && exp
    @Override
    public void visit(And e) {
        e.getLeft().accept(this);
        Object leftObj = stk.pop();

        e.getRight().accept(this);
        Object rightObj = stk.pop();

        // Verifica e converte para Boolean
        boolean left = (leftObj instanceof Boolean) ? (Boolean) leftObj : false;
        boolean right = (rightObj instanceof Boolean) ? (Boolean) rightObj : false;

        stk.push(left && right);
    }

    // lvalue ‘[’ exp ‘]’
    @Override
    public void visit(ArrayAccess e) {
        e.getBase().accept(this);
        Object arrayObj = stk.pop();
        e.getIndex().accept(this);
        Object indexObj = stk.pop();

        Object[] array = (Object[]) arrayObj;
        int index = (Integer) indexObj;

        stk.push(array[index]);
    }

    // true, false
    @Override
    public void visit(Bool e) {
        stk.push(e.getValue());
    }

    // CHAR
    @Override
    public void visit(Char e) {
        stk.push(e.getValue());
    }

    // rexp != aexp
    @Override
    public void visit(Different e) {
        e.getLeft().accept(this);
        Object leftObj = stk.pop();

        e.getRight().accept(this);
        Object rightObj = stk.pop();

        boolean r = !leftObj.equals(rightObj);

        stk.push(r);
    }

    // mexp / sexp
    @Override
    public void visit(Div e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        Integer right = (Integer) stk.pop();
        Integer left = (Integer) stk.pop();

        stk.push(left / right);
    }

    // rexp == aexp
    @Override
    public void visit(Equal e) {
        e.getLeft().accept(this);
        Object leftObj = stk.pop();

        e.getRight().accept(this);
        Object rightObj = stk.pop();

        boolean r = leftObj.equals(rightObj);

        stk.push(r);
    }


    // lvalue . ID
    @Override
    public void visit(FieldAccess e) {
        e.accept(this);
        Object objectObj = stk.pop();

        Map<String, Object> object = (Map<String, Object>) objectObj;
        String fieldName = e.toString();

        stk.push(object.get(fieldName));
    }

    // aexp > aexp
    @Override
    public void visit(Greater e) {
        e.getLeft().accept(this);
        Object leftObj = stk.pop();

        e.getRight().accept(this);
        Object rightObj = stk.pop();

        // Converte para Float se necessário
        float left = (leftObj instanceof Integer) ? ((Integer) leftObj).floatValue() : (Float) leftObj;
        float right = (rightObj instanceof Integer) ? ((Integer) rightObj).floatValue() : (Float) rightObj;

        stk.push(left > right);
    }

    // aexp < aexp
    @Override
    public void visit(Less e) {
        e.getLeft().accept(this);
        Object leftObj = stk.pop();

        e.getRight().accept(this);
        Object rightObj = stk.pop();

        // Converte para Float caso seja Integer
        float left = (leftObj instanceof Integer) ? ((Integer) leftObj).floatValue() : (Float) leftObj;
        float right = (rightObj instanceof Integer) ? ((Integer) rightObj).floatValue() : (Float) rightObj;

        stk.push(left < right);
    }

    // INT
    @Override
    public void visit(Int e) {
        stk.push(e.getValue());
    }

    @Override
    public void visit(Null e) {

    }

    // lvalue
    @Override
    public void visit(LValue v) {
        Object value = varTable.get(v.getId());
        stk.push(value);
    }

    // mexp % sexp
    @Override
    public void visit(Mod e) {
        e.getLeft().accept(this);
        Integer left = (Integer) stk.pop();

        e.getRight().accept(this);
        Integer right = (Integer) stk.pop();

        Integer r = left % right;
        stk.push(r);
    }

    // new type [ ‘[’ exp ‘]’ ]
    @Override
    public void visit(NewArray e) {
        e.getSizeExp().accept(this);
        Object sizeObj = stk.pop();

        int size = (Integer) sizeObj;
        Object[] array = new Object[size];

        stk.push(array);
    }

    // ! sexp
    @Override
    public void visit(Not e) {
        e.getExp().accept(this);
        boolean value = (Boolean) stk.pop();
        stk.push(!value);
    }

    // aexp - mexp
    @Override
    public void visit(Sub e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        Integer right = (Integer) stk.pop();
        Integer left = (Integer) stk.pop();

        stk.push(left - right);
    }

    // aexp + mexp
    @Override
    public void visit(Plus e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        Integer right = (Integer) stk.pop();
        Integer left = (Integer) stk.pop();

        stk.push(left + right);
    }

    // mexp * sexp
    @Override
    public void visit(Times e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        Integer right = (Integer) stk.pop();
        Integer left = (Integer) stk.pop();

        stk.push(left * right);
    }

    // - sexp
    @Override
    public void visit(UnaryMinus e) {
        e.getExp().accept(this);
        Object valueObj = stk.pop();

        float value = (valueObj instanceof Integer) ? ((Integer) valueObj).floatValue() : (Float) valueObj;

        stk.push(-value);
    }

    @Override
    public void visit(Var e) {
        if (!varTable.containsKey(e.getName())) {
            throw new RuntimeException("Variável não definida: " + e.getName());
        }
        Object value = getVarValue(e.getName());
        stk.push(value);
    }

    /* EXPR Visitors - F */

    /* Type Visitors - I */
    @Override
    public void visit(TyBool t) {
        stk.push(t.getTypeName());
    }

    @Override
    public void visit(TyInt t) {
        stk.push(t.getTypeName());
    }

    @Override
    public void visit(TyChar t) {
        stk.push(t.getTypeName());
    }

    @Override
    public void visit(TyFloat t) {
        stk.push(t.getTypeName());
    }

    @Override
    public void visit(TyId t) {
        stk.push(t.getTypeName());
    }

    /* Type Visitors - F */

    // Superclasse
    @Override
    public void visit(Cmd c) {

    }

    @Override
    public void visit(If c) {
        c.getCondition().accept(this);
        Boolean condition = (Boolean) stk.pop();

        if (condition) {
            c.getThenBlock().accept(this);
        } else if (c.getElseBlock() != null) {
            c.getElseBlock().accept(this);
        }
    }

    @Override
    public void visit(Iterate c) {
        LoopCond loopCond = c.getLoopCond();

        // Visita o LoopCond para obter o número de iterações
        loopCond.accept(this);
        int times = (Integer) stk.pop();

        for (int i = 0; i < times; i++) {
            if (loopCond.hasVar()) {
                varTable.put(loopCond.getVarName(), i); // Atualiza a variável do loop
            }
            c.getBody().accept(this);
        }
    }

    
    @Override
    public void visit(Print c) {
        Exp exp = c.getExp();
        exp.accept(this);

        Object value = stk.pop();
        System.out.println(value);
    }

   
    @Override
    public void visit(Read c) {
        LValue lvalue = c.getLValue();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite um valor para " + lvalue + ": ");
        String input = scanner.nextLine();

        stk.push(input); // Ajuste conforme necessário para converter e definir o valor corretamente

    }

    @Override
    public void visit(Return c) {

    }

    @Override
    public void visit(Bind dec) {

    }

    @Override
    public void visit(Block b) {
        // Iteramos sobre os comandos do bloco e os visitamos
        for (Cmd cmd : b.getCommands()) {
            cmd.accept(this);
        }
    }

    @Override
    public void visit(Decl dec) {

    }

    @Override
    public void visit(FunDef dec) {

    }

    @Override
    public void visit(Param dec) {

    }

    @Override
    public void visit(StmtBlock sb) {
        if (sb.isBlock()) {
            // Se contém um bloco, visitamos o bloco
            sb.getBlock().accept(this);
        } else {
            // Se contém um único comando, visitamos o comando
            sb.getSingleCmd().accept(this);
        }
    }

    // Superclasse
    @Override
    public void visit(BinOp e) {
    }

    // Superclasse
    @Override
    public void visit(LType t) {
    }

    @Override
    public void visit(Program p) {

    }

    // Superclasse
    @Override
    public void visit(Exp e) {

    }

    @Override
    public void visit(FloatLit e) {
        stk.push(e.getValue());
    }

    @Override
    public void visit(LoopCond loopCond) {
        // Avalia a expressão dentro do LoopCond
        loopCond.getExpression().accept(this);
        Object conditionValue = stk.pop();

        // Garante que o valor da condição seja um número inteiro
        if (!(conditionValue instanceof Integer)) {
            throw new RuntimeException("A expressão do 'iterate' deve ser um número inteiro.");
        }

        // Se houver uma variável (ID), armazenamos ela na varTable
        if (loopCond.hasVar()) {
            varTable.put(loopCond.getVarName(), 0); // Inicializa o contador
        }

        stk.push(conditionValue);
    }

    @Override
    public void visit(Data data) {
        stk.push(data.getDeclList());
        stk.push(data.getId()); 
    }

    public void visit(FunCall funCall){

    }

    public void visit(TyArray t) {
    }   

    public void visit(Instanciate i) {

    }

    @Override
    public void visit(FCall e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visit'");
    }
}