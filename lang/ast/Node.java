package lang.ast;

public abstract class Node{
     private int l,c;

      public Node(int line, int col){
           l = line;
           c = col;
      }

      public int getLine(){return l;}
      public int getCol(){return c;}

      public abstract void accept(NodeVisitor v);

}
