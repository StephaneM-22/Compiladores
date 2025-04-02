package lang.ast.visitors.tyCheckVisitor;

import lang.ast.decl.*;
import lang.ast.expr.*;
import lang.ast.command.*;
import lang.ast.types.*;
import lang.ast.*;
import lang.ast.NodeVisitor;
import java.util.Hashtable;
import java.util.Stack;
import java.util.LinkedList;
import java.util.ArrayList;

public  class TyChecker extends NodeVisitor{

    private final VTyInt typeInt = VTyInt.newInt();
    private final VTyFloat typeFloat = VTyFloat.newFloat();
    private final VTyBool typeBoolean = VTyBool.newBool();
    private final VTyChar typeChar = VTyChar.newChar();
    private final VTyError typeError = VTyError.newError();

    private LinkedList<String> logError;
    private Stack<VType> stk;
    private Hashtable<String,TypeEntry> ctx;
    private Hashtable<String, VType> localCtx;
    private Hashtable<Object, VType> typeNode;
    private VType returnType; // Tipo esperado de retorno
    private boolean bodyRetun; // Algum comando de retorno ?

    //construtor da classe TyChecker
    public TyChecker(){
        logError = new LinkedList<String>();
        stk = new Stack<VType>();
        ctx = new Hashtable<String,TypeEntry>();
        typeNode = new Hashtable<Object,VType>();
    }

    public Hashtable<String,TypeEntry> getTypeCtx(){
        return ctx;
    }

    public TypeEntry getFunctionType(String s){
        return ctx.get(s);
    }

    public VType typeOf(Object node){
        return typeNode.get(node);
    }

    public void visit(Program p){
        collectType(p.getFuncs());
        for(FunDef f : p.getFuncs()){
            localCtx = ctx.get(f.getFname()).localCtx;
            f.accept(this);
        }
    }

    private void collectType(ArrayList<FunDef> lf){
        for(FunDef f : lf){

            TypeEntry e = new TypeEntry();
            e.sym = f.getFname();
            e.localCtx = new Hashtable<String,VType>();

            int typeln = f.getParams().size() + 1;
            for(Param b : f.getParams()){ // for(Bind b : f.getParams()){
                b.getType().accept(this);
                e.localCtx.put( b.getId(), stk.peek()); // e.localCtx.put( b.getVar().getName(), stk.peek());
            }

            // f.getRet().accept(this);
            for(LType r : f.getRetrn()) {
                r.accept(this);
            }

            VType[] v = new VType[typeln];
            for(int i = typeln - 1; i >=0; i--){
                v[i] = stk.pop();
            }

            e.ty = new VTyFunc(v);

            ctx.put(f.getFname(),e);
            typeNode.put(f,e.ty);
        }
    }
    




    // Visita um nó CSeq na AST (Comando Sequencial)
    public void visit(CSeq d) {
    d.getLeft().accept(this);
    d.getRight().accept(this);
    }

    // Visita um nó CAttr na AST (Atribuição de variável)
    public void visit(CAttr d){
        d.getExp().accept(this);
        if(localCtx.get(d.getVar().getName()) == null){
            localCtx.put(d.getVar().getName(),stk.pop());
        }else{
            VType ty = localCtx.get(d.getVar().getName());
            if(!ty.match(stk.pop())){
                throw new RuntimeException(
                        "Erro de tipo (" + d.getLine() + ", " + d.getCol() + ") tipo da var " +d.getVar().getName() + " incompativel"
                );
            }
        }
    }

    
    //COMMAND

    @Override
    public void visit(Assign cmd) {
        cmd.getExp().accept(this);
        VType ty = stk.pop();
    
        if (localCtx.get(cmd.getVar().getName()) == null) {
            localCtx.put(cmd.getVar().getName(), ty);
        } else {
            VType existingType = localCtx.get(cmd.getVar().getName());
            if (!existingType.match(ty)) {
                String errorMsg = String.format("Erro de tipo (%d, %d): variável '%s' incompatível.",
                                                cmd.getLine(), cmd.getCol(), cmd.getVar().getName());
                logError.add(errorMsg);
                throw new RuntimeException(errorMsg);
            }
        }
    }
    

    @Override
    public void visit(Cmd c) {
        // Método de visita para comandos genéricos.
    }



    //Verificar essa FunCall
    // verificar se a função chamada existe e se os argumentos são compatíveis com os parâmetros da função declarada.
    @Override
    public void visit(FunCall c) {
        // Verifica se a função foi declarada no contexto global
        if (!globalCtx.containsKey(c.getFuncName())) {
            throw new RuntimeException(
                "Erro de tipo (" + c.getLine() + ", " + c.getCol() + "): Função '" + c.getFuncName() + "' não declarada."
            );
        }
    
        // Obtém a assinatura da função do contexto
        FunctionType funcType = globalCtx.get(c.getFuncName());
        ArrayList<VType> paramTypes = funcType.getParamTypes();
        ArrayList<VType> returnTypes = funcType.getReturnTypes();
    
        // Verifica a compatibilidade dos argumentos
        if (c.getArgs().size() != paramTypes.size()) {
            throw new RuntimeException(
                "Erro de tipo (" + c.getLine() + ", " + c.getCol() + "): Número incorreto de argumentos para '" + c.getFuncName() + "'."
            );
        }
    
        // Verifica o tipo de cada argumento
        for (int i = 0; i < c.getArgs().size(); i++) {
            c.getArgs().get(i).accept(this);
            VType argType = stk.pop();
            if (!paramTypes.get(i).match(argType)) {
                throw new RuntimeException(
                    "Erro de tipo (" + c.getLine() + ", " + c.getCol() + "): Argumento " + (i + 1) + " incompatível para '" + c.getFuncName() + "'."
                );
            }
        }
    
        // Se houver valores de retorno, verifica se há variáveis para recebê-los
        if (!returnTypes.isEmpty()) {
            if (c.getLvalues() == null || c.getLvalues().size() != returnTypes.size()) {
                throw new RuntimeException(
                    "Erro de tipo (" + c.getLine() + ", " + c.getCol() + "): Número incorreto de variáveis de retorno para '" + c.getFuncName() + "'."
                );
            }
    
            // Verifica compatibilidade dos valores retornados
            for (int i = 0; i < c.getLvalues().size(); i++) {
                c.getLvalues().get(i).accept(this);
                VType varType = stk.pop();
                if (!returnTypes.get(i).match(varType)) {
                    throw new RuntimeException(
                        "Erro de tipo (" + c.getLine() + ", " + c.getCol() + "): Retorno incompatível para '" + c.getFuncName() + "'."
                    );
                }
            }
        }
    
        // Empilha o tipo do retorno para uso posterior
        if (!returnTypes.isEmpty()) {
            for (VType retType : returnTypes) {
                stk.push(retType);
            }
        }
    }    


    @Override
public void visit(If d){
    // Verifica a condição do if
    d.getCond().accept(this);
    VType tyc = stk.pop();

    if (!(tyc.getTypeValue() == CLTypes.BOOL)) {
        throw new RuntimeException(
                "Erro de tipo (" + d.getLine() + ", " +
                        d.getCol() +
                        ") condição do teste deve ser bool"
        );
    }

    // Faz um backup do contexto local antes de avaliar o bloco THEN
    Hashtable<String, VType> localBackup = (Hashtable<String, VType>) localCtx.clone();

    // Avalia o bloco THEN
    d.getThn().accept(this);

    if (d.getEls() != null) {
        // Faz backup do estado após o bloco THEN
        Hashtable<String, VType> afterThen = (Hashtable<String, VType>) localCtx.clone();
        
        // Restaura o contexto original antes de avaliar o bloco ELSE
        localCtx = localBackup;
        d.getEls().accept(this);

        // Remove variáveis criadas apenas no bloco ELSE
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, VType> entry : localCtx.entrySet()) {
            if (!afterThen.containsKey(entry.getKey())) {
                keysToRemove.add(entry.getKey());
            }
        }
        for (String key : keysToRemove) {
            localCtx.remove(key);
        }
    } else {
        // Se não houver ELSE, restauramos apenas o estado após o THEN
        localCtx = localBackup;
    }
}


@Override
public void visit(Iterate c) {
    // Verifica a condição do laço
    // verificação de tipo seja aplicada corretamente
    c.getLoopCond().accept(this);
    VType condType = stk.pop();

    if (!(condType.getTypeValue() == CLTypes.BOOL)) {
        throw new RuntimeException(
                "Erro de tipo (" + c.getLine() + ", " +
                        c.getCol() +
                        ") condição do laço deve ser bool"
        );
    }
    // Faz um backup do contexto antes de processar o corpo do laço
    Hashtable<String, VType> localBackup = (Hashtable<String, VType>) localCtx.clone();

    // Avalia o corpo do laço
    c.getBody().accept(this);

    // Restaura o contexto original após sair do laço
    localCtx = localBackup;
}



    public void visit(Print c){
        d.getExp().accept(this);
        VType td = stk.pop();
        if(td.getTypeValue() == CLTypes.INT ||
                td.getTypeValue() == CLTypes.FLOAT ||
                td.getTypeValue() == CLTypes.BOOL){
        }else{
            throw new RuntimeException("Erro de tipo (" + d.getLine() + ", " + d.getCol() + ") Operandos incompatíveis");
        }

    }

    @Override
    public void visit(Read c) {
        // Aceita a visita ao LValue (variável que recebe a entrada)
        c.getLValue().accept(this);
    
        // Verifica se a variável já foi declarada no contexto local
        VType varType = localCtx.get(c.getLValue().toString());
    
        if (varType == null) {
            throw new RuntimeException(
                "Erro de tipo (" + c.getLine() + ", " + c.getCol() + 
                ") variável '" + c.getLValue().toString() + "' não declarada"
            );
        }
        // Adiciona a variável ao mapa de tipos do nó
        typeNode.put(c, varType);
    }
    

    public void visit(Return c){
        c.getExp().accept(this);
        // Verifica se o tipo do retorno é compatível com o tipo esperado
        if(!stk.peek().match(returnType)){
            throw new RuntimeException("Erro de tipo (" + d.getLine() + ", " + d.getCol() + ") Tipo de retorno incompatível.");
        }
        typeNode.put(d,stk.pop()); // Armazena o tipo da expressão retornada
        bodyRetun = true;
    }


    //EXPR
    
    @Override
    public void visit(And e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        VType tyr = stk.pop();
        VType tyl = stk.pop();

        if(tyr.match(typeBoolean) && tyl.match(typeBoolean)) {
            stk.push(typeBoolean);
        } else {
            logError.add(
                    e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                    + " não compatíveis com operador &(AND)."
            );

            stk.push(typeError);

            throw new RuntimeException(e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                    + " não compatíveis com operador &(AND).");
        }
    }

    @Override
    public void visit(BinOp e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);
        VType td = stk.pop();
        VType te = stk.pop();
        if(td.getTypeValue() == CLTypes.INT &&
            te.getTypeValue() == CLTypes.INT){
            stk.push(VTyInt.newInt());
            typeNode.put(e,stk.peek());
        }else if(td.getTypeValue() == CLTypes.FLOAT &&
                te.getTypeValue() == CLTypes.FLOAT){
            stk.push(VTyFloat.newFloat());
            typeNode.put(e,stk.peek());
        }else{
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") Operandos incompatíveis");
        }



    @Override
    public void visit(Bool e){
        stk.push(typeBoolean);
        typeNode.put(e,stk.peek());
    }

    @Override
    public void visit(Char e) {
        stk.push(typeChar);
        typeNode.put(e,stk.peek());
    }

    
    // =! => [a, a] → Bool, em que a ∈ {Int, Float, Char, Bool}
    @Override
    public void visit(Different e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        VType tyr = stk.pop();
        VType tyl = stk.pop();

        if(tyr.match(typeInt) && tyl.match(typeInt)) {
            stk.push(typeInt);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeFloat) && tyl.match(typeFloat)) {
            stk.push(typeFloat);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeChar) && tyl.match(typeChar)) {
            stk.push(typeChar);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeBoolean) && tyl.match(typeBoolean)) {
            stk.push(typeBoolean);
            typeNode.put(e,stk.peek());
        } else{
            logError.add(
                    e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                            + " não compatíveis com operador =!(Different)."
            );

            stk.push(typeError);

            throw new RuntimeException(e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                    + " não compatíveis com operador =!(Different).");
        }
    }

    @Override
    public void visit(Div e) {
        // Avalia os operandos da divisão
        e.getLeft().accept(this);
        e.getRight().accept(this);
    
        // Obtém os tipos dos operandos da pilha
        VType rightType = stk.pop();
        VType leftType = stk.pop();
    
        // Verifica se os tipos são compatíveis com a operação de divisão
        if (rightType.match(typeInt) && leftType.match(typeInt)) {
            stk.push(typeInt);
        } else if (rightType.match(typeFloat) && leftType.match(typeFloat)) {
            stk.push(typeFloat);
        } else {
            String errorMsg = String.format(
                "%d, %d: Tipos %s e %s não são compatíveis com o operador /(DIV).",
                e.getLine(), e.getCol(), leftType, rightType
            );
    
            logError.add(errorMsg);
            stk.push(typeError);
    
            throw new RuntimeException(errorMsg);
        }
    
        // Associa o tipo inferido ao nó da árvore sintática
        typeNode.put(e, stk.peek());
    }

    
    // = => [a, a] → Bool, em que a ∈ {Int, Float, Char, Bool}
    public void visit(Equal e){
        e.getLeft().accept(this);
        e.getRight().accept(this);

        VType tyr = stk.pop();
        VType tyl = stk.pop();

        if(tyr.match(typeInt) && tyl.match(typeInt)) {
            stk.push(typeInt);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeFloat) && tyl.match(typeFloat)) {
            stk.push(typeFloat);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeChar) && tyl.match(typeChar)) {
            stk.push(typeChar);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeBoolean) && tyl.match(typeBoolean)) {
            stk.push(typeBoolean);
            typeNode.put(e,stk.peek());
        } else{
            logError.add(
                    e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                            + " não compatíveis com operador =!(EQUAL)."
            );

            stk.push(typeError);

            throw new RuntimeException(e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                    + " não compatíveis com operador =!(EQUAL).");
        }
    }


    @Override
    public void visit(Exp e) { 
        /* Super class */ 
    }

    @Override
    public void visit(FCall e){

        for(Exp ex : e.getArgs()){
            ex.accept(this);
        }
        VType vt[] = new VType[e.getArgs().size()];
        for(int j = e.getArgs().size() -1; j >=0;j--){
            vt[j] = stk.pop();
        }
        TypeEntry tyd = ctx.get(e.getID() );
        if(tyd != null ){
            if(!((VTyFunc)tyd.ty).matchArgs(vt)){
                throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") chamada de função incompatível ");
            }
            stk.push(((VTyFunc)tyd.ty).getReturnType() );
            typeNode.put(e,stk.peek());
        }else{
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") chamada a função não declarada " + e.getID());
        }
    }

    @Override
    public void visit(FloatLit e){
        stk.push(typeFloat);
        typeNode.put(e,stk.peek());
    }

    @Override
    public void visit(Greater e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);

        VType tyr = stk.pop();
        VType tyl = stk.pop();

        if(tyr.match(typeInt) && tyl.match(typeInt)) {
            stk.push(typeInt);
            typeNode.put(e,stk.peek());
        } else if(tyr.match(typeFloat) && tyl.match(typeFloat)) {
            stk.push(typeFloat);
            typeNode.put(e,stk.peek());
        } else {
            logError.add(
                    e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                            + " não compatíveis com operador >(GREATER)."
            );

            stk.push(typeError);

            throw new RuntimeException(e.getLine() + ", " + e.getCol() + ": Tipos " + tyl + " e " + tyr
                    + " não compatíveis com operador >(GREATER).");
        }
    }


    //Instanciate - ?
    @Override
    public void visit(Instanciate e) {

    }

    @Override
    public void visit(Int e){
        stk.push(typeInt);
        typeNode.put(e,stk.peek());
    }

    @Override
    public void visit(Less e){
        e.getLeft().accept(this);
        e.getRight().accept(this);
        VType td = stk.pop();
        VType te = stk.pop();
        if(td.getTypeValue() == CLTypes.INT &&
                te.getTypeValue() == CLTypes.INT){
            stk.push(VTyBool.newBool());
            typeNode.put(e,stk.peek());
        }else if(td.getTypeValue() == CLTypes.FLOAT &&
                te.getTypeValue() == CLTypes.FLOAT){
            stk.push(VTyBool.newBool());
            typeNode.put(e,stk.peek());
        }else{
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") Operandos incompatíveis");
        }
    }

    @Override
    public void visit(LoopCond d){

        d.getCond().accept(this);
        VType tyc = stk.pop();
        if(! (tyc.getTypeValue() == CLTypes.BOOL)){
            throw new RuntimeException(
                    "Erro de tipo (" + d.getLine() + ", " +
                            d.getCol() +
                            ") condição do laço deve ser bool"
            );
        }
        d.getBody().accept(this);
    }


    @Override
    public void visit(LValue e) {

    }

    @Override
    public void visit(Mod e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);
    
        VType rightType = stk.pop();
        VType leftType = stk.pop();
    
        if (leftType.getTypeValue() == CLTypes.INT && rightType.getTypeValue() == CLTypes.INT) {
            stk.push(VTyInt.newInt());
            typeNode.put(e, stk.peek());
        } else {
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + 
                "): Operação módulo (%) requer operandos do tipo INT.");
        }
    }

    @Override
    public void visit(NewArray e) {

    }

    @Override
    public void visit(Not e) {
        e.getExp().accept(this);

        VType tyExp = stk.pop();

        if(tyExp.match(typeBoolean)) {
            stk.push(typeBoolean);
        } else {
            logError.add(
                    e.getLine() + ", " + e.getCol() + ": Tipo " + tyExp
                    + " não compatível com operador !(NOT)."
            );

            stk.push(typeError);

            throw new RuntimeException(e.getLine() + ", " + e.getCol() + ": Tipo " + tyExp
                    + " não compatível com operador !(NOT).");
        }
    }


    @Override
    public void visit(Null e) {

    }

    public void visit(Plus e){
        e.getLeft().accept(this);
        e.getRight().accept(this);
        VType td = stk.pop();
        VType te = stk.pop();
        if(td.getTypeValue() == CLTypes.INT &&
                te.getTypeValue() == CLTypes.INT){
            stk.push(VTyInt.newInt());
            typeNode.put(e,stk.peek());
        }else if(td.getTypeValue() == CLTypes.FLOAT &&
                te.getTypeValue() == CLTypes.FLOAT){
            stk.push(VTyFloat.newFloat());
            typeNode.put(e,stk.peek());
        }else{
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") Operandos incompatíveis");
        }
    }


    public void visit(Sub  e){
        e.getLeft().accept(this);
        e.getRight().accept(this);
        VType td = stk.pop();
        VType te = stk.pop();
        if(td.getTypeValue() == CLTypes.INT &&
                te.getTypeValue() == CLTypes.INT){
            stk.push(VTyInt.newInt());
            typeNode.put(e,stk.peek());
        }else if(td.getTypeValue() == CLTypes.FLOAT &&
                te.getTypeValue() == CLTypes.FLOAT){
            stk.push(VTyFloat.newFloat());
            typeNode.put(e,stk.peek());
        }else{
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") Operandos incompatíveis");
        }
    }

    

    public void visit(Times e){
        e.getLeft().accept(this);
        e.getRight().accept(this);
        VType td = stk.pop();
        VType te = stk.pop();
        if(td.getTypeValue() == CLTypes.INT &&
                te.getTypeValue() == CLTypes.INT){
            stk.push(VTyInt.newInt());
            typeNode.put(e,stk.peek());
        }else if(td.getTypeValue() == CLTypes.FLOAT &&
                te.getTypeValue() == CLTypes.FLOAT){
            stk.push(VTyFloat.newFloat());
            typeNode.put(e,stk.peek());
        }else{
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") Operandos incompatíveis");
        }
    }


    @Override
    public void visit(UnaryMinus e) {

    }


    public void visit(Var e){
        VType ty = localCtx.get(e.getName());
        if(ty == null){
            throw new RuntimeException("Erro de tipo (" + e.getLine() + ", " + e.getCol() + ") variavel não declarada: " + e.getName());
        }else{
            stk.push(ty);
            typeNode.put(e,stk.peek());
        }
    }
   

   


    //DECL

    @Override
    public void visit(ArrayAccess dec) {
    // Verifica se a base do acesso ao array é válida
    dec.getBase().accept(this);
    VType baseType = stk.pop();
    // Verifica se a base é realmente um array
    if (!(baseType instanceof VTyArray)) {
        throw new RuntimeException(
            "Erro de tipo (" + dec.getLine() + ", " + dec.getCol() + 
            ") acesso a índice em uma variável que não é um array."
        );
    }
    // Verifica o índice do array
    dec.getIndex().accept(this);
    VType indexType = stk.pop();
    // O índice deve ser do tipo inteiro
    if (indexType.getTypeValue() != CLTypes.INT) {
        throw new RuntimeException(
            "Erro de tipo (" + dec.getLine() + ", " + dec.getCol() + 
            ") índice do array deve ser um inteiro."
        );
    }
    // O tipo do acesso ao array é o tipo dos elementos do array base
    stk.push(((VTyArray) baseType).getElementType());
    typeNode.put(dec, stk.peek());
    }

    
    @Override
    public void visit(Bind d) {
    // Obtém a variável e o tipo associado
    String varName = d.getVar().toString();
    Ty varType = d.getType();

    // Se a variável já existir no contexto, verifica se os tipos coincidem
    if (localCtx.containsKey(varName)) {
        if (!localCtx.get(varName).match(varType)) {
            throw new RuntimeException(
                "Erro de tipo (" + d.getLine() + ", " + d.getCol() + 
                ") variável '" + varName + "' já foi declarada com um tipo diferente."
            );
        }
    } else {
        // Adiciona ao contexto local
        localCtx.put(varName, varType);
    }

    // Registra no mapa de tipos
    typeNode.put(d, varType);
    }


    @Override
    public void visit(Block dec) {

    }

    @Override
    public void visit(Data data) {

    }

    @Override
    public void visit(Decl dec) {

    }

    @Override
    public void visit(FieldAccess dec) {

    }

    public void visit(FunDef d){

        d.getRet().accept(this);
        returnType = stk.pop();
        for(Bind b: d.getParams()){
            b.accept(this);
        }
        bodyRetun = false;
        d.getBody().accept(this);
        if(!bodyRetun){
            throw new RuntimeException(
                    "Erro de tipo (" + d.getLine() + ", " + d.getCol() + ")  função " + d.getFname() + " não retorna valor algum");
        }
    }


    @Override
    public void visit(Param dec) {

    }

    @Override
    public void visit(StmtBlock dec) {

    }


   


    //TYPES

    @Override
    public void visit(LType t) {

    }

    public void visit(TyBool t){  
        stk.push(VTyBool.newBool() ); 
        typeNode.put(t,stk.peek());
    }

    @Override
    public void visit(TyChar t){ 
        stk.push(VTyChar.newChar()); 
        typeNode.put(t,stk.peek());
    }

    @Override
    public void visit(TyId t) {

    }

    public void visit(TyInt t){   
        stk.push(VTyInt.newInt() ); 
        typeNode.put(t,stk.peek());
    }
    public void visit(TyFloat t){
        stk.push(VTyFloat.newFloat());
        typeNode.put(t,stk.peek());}

    @Override
    public void visit(TyArray t) {

    }

    public static void printEnv(Hashtable<String,VType> t){
        for(java.util.Map.Entry<String,VType> ent : t.entrySet()){
            System.out.println(ent.getKey()+ " -> "+ent.getValue().toString());
        }
    }

}
}