package lang.ast;

import lang.ast.expr.*;
import lang.ast.decl.*;
import lang.ast.command.*;
import lang.ast.types.*;


public abstract class NodeVisitor{
    //Visitors para Commands
    public abstract void visit(Assign c);
    public abstract void visit(Cmd c);
    public abstract void visit(If c);
    public abstract void visit(Iterate c);
    public abstract void visit(Print c);
    public abstract void visit(Read c);
    public abstract void visit(Return c);
    public abstract void visit(FunCall c);
    
    //Visitors para os Decls
    public abstract void visit(ArrayAccess dec);
    public abstract void visit(Bind dec);
    public abstract void visit(Block dec);
    public abstract void visit(Decl dec);
    public abstract void visit(FunDef dec);
    public abstract void visit(Param dec);
    public abstract void visit(StmtBlock dec);
    public abstract void visit(Data data);

    //Visitors para os Expr
    public abstract void visit(And e);
    public abstract void visit(BinOp e);
    public abstract void visit(Bool e);
    public abstract void visit(Char e);
    public abstract void visit(Div e);
    public abstract void visit(Different e);
    public abstract void visit(Equal e);
    public abstract void visit(Exp e);
    public abstract void visit(FCall e);
    public abstract void visit(FloatLit e);
    public abstract void visit(Greater e);
    public abstract void visit(Int e);
    public abstract void visit(Null e);
    public abstract void visit(Less e);
    public abstract void visit(LValue e);
    public abstract void visit(Mod e);
    public abstract void visit(Not e);
    public abstract void visit(Plus e);
    public abstract void visit(Sub  e);
    public abstract void visit(Times e);
    public abstract void visit(Var e);
    public abstract void visit(LoopCond e);
    public abstract void visit(UnaryMinus e);
    public abstract void visit(NewArray e);
    public abstract void visit(FieldAccess dec);
    public abstract void visit(Instanciate e);

    //Visitors para os Types
    public abstract void visit(LType t);
    public abstract void visit(TyBool t);
    public abstract void visit(TyChar t);
    public abstract void visit(TyId t);
    public abstract void visit(TyInt t);
    public abstract void visit(TyFloat t);
    public abstract void visit(TyArray t);

    //Visitors para os RootPrograms
    public abstract void visit(Program p);
    
}
