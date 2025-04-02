package lang.ast.visitors.tyCheckVisitor;

public abstract  class VType {
     public short type;
     protected VType(short type){ this.type = type;}
     public abstract boolean match(VType t);
     public short getTypeValue(){ return type;}
}
