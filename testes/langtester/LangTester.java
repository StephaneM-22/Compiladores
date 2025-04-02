package langtester;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Random;

public class LangTester{
   // Recupera o nome base (sem extensão) de um arquivo.

   public static File[] list_files(String path){
        File fs = new File(path);
        return fs.listFiles();
   }

   private static File[] list_files(String path, String ext){
        File fs = new File(path);
        FileFilter ff = new FileFilter(){
              public boolean accept(File f){
                   return f.isFile() && f. getName().endsWith("."+ext);
              }
        };
        return fs.listFiles(ff);
   }

   public static byte[] dotedArr(int n, char c){
        byte[] b = new byte[n];
        for(int i = 0; i <n; i++){
             b[i] = (byte)c;
        }
        return b;
   }

   private static String mkPath(String[] spath){
        String pth = "";
        for(String s : spath){
           pth = pth + s + File.separator;
        }
        return pth;
   }

   public static int maxNameLen(int min, File[] fs){
        int mx = min;
        if(fs!=null){
            for(File f : fs ){
               if(f.getName().length() > mx){
                   mx =  f.getName().length();
                }
            }
        }
        return mx;
   }

    public static void testRun(String cmd){
       //String cmd = "./lang.sh -syn";
       try{
          Runtime rn = Runtime.getRuntime();
          Random  rnd = new Random();
          String accpth = mkPath(new String[]{"sintaxe","certo"});
          File[] accfs = list_files(accpth);

          Process p;
          String out;
          BufferedReader procr;
          if(accfs == null){
             System.out.println("Nao ha arquivos para processar em " + accpth);
          }else{
               System.out.println("Selecionando um arquivo aleatório em: " + accpth);
               File fsrc = accfs[rnd.nextInt(accfs.length)];
               System.out.println("Executando comando:");
               System.out.println(cmd + " " + fsrc.getPath());
               p = rn.exec(cmd +" " + fsrc.getPath());
               System.out.println("---------- Saida detectada ----------");

               procr = new BufferedReader(new InputStreamReader(p.getInputStream()));
               out  = procr.readLine();
               int c = 0;
               while(out != null){
                  System.out.println(c+"]" + out);
                  out  = procr.readLine();
                  c++;
               }
               System.out.println("--------Fim da Saida detectada ----------");
          }
      }catch(Exception e){
          e.printStackTrace();
      }
   }



   public static void synTests(String cmd){
       //String cmd = "./lang.sh -syn";
       List<String> accfails = new LinkedList<String>();
       List<String> rejfails = new LinkedList<String>();
       try{
          int passed = 0, failed = 0, maxfname;
          Runtime rn = Runtime.getRuntime();
          String accpth = mkPath(new String[]{"sintaxe","certo"});
          String rejpth = mkPath(new String[]{"sintaxe","errado"});
          File[] accfs = list_files(accpth);
          File[] rejfs = list_files(rejpth);
          maxfname = maxNameLen(5,accfs);
          byte[] sepbuff  = dotedArr(maxfname,'.');
          Process p;
          String out;
          BufferedReader procr;

          if(accfs == null){
             System.out.println("Nao ha arquivos para processar em " + accpth);
          }else{
               System.out.println("Processando casos de teste em testes/sintaxe/certo/");
               for(File fsrc : accfs){
                  System.out.print(fsrc.getName());
                  System.out.write(sepbuff,0,maxfname - fsrc.getName().length());
                  System.out.print("[");
                  p = rn.exec(cmd +" " + fsrc.getPath());
                  procr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                  out = procr.readLine();
                  boolean accpt = false;
                  while(out != null && !accpt){
                    accpt = out.equals("accepted");
                    out = procr.readLine();
                  }
                  if(accpt){
                      System.out.println(" OK ]");
                      passed++;
                  }else{
                      System.out.println("FAIL]");
                      accfails.add(fsrc.getName());
                      failed++;
                  }
               }
          }
          if(rejfs == null){
             System.out.println("Nao ha arquivos para processar em " + rejpth);
          }else{
              System.out.println("Processando casos de teste em " + rejpth);
              for(File fsrc : rejfs){
                  System.out.print(fsrc.getName());
                  System.out.write(sepbuff,0,maxfname - fsrc.getName().length());
                  System.out.print("[");
                  p = rn.exec(cmd +" " +fsrc.getPath());
                  procr = new BufferedReader(new InputStreamReader(p.getInputStream()));
                  out = procr.readLine();
                  boolean rej = false;
                  while(out != null && !rej){
                    rej = out.equals("rejected");
                    out = procr.readLine();
                  }
                  if(rej){
                      System.out.println(" OK ]");
                      passed++;
                  }else{
                      System.out.println("FAIL]");
                      rejfails.add(fsrc.getName());
                      failed++;
                  }
              }
              reportSyn(passed, failed, accfails, rejfails);
          }
      }catch(Exception e){
          e.printStackTrace();
      }
   }

   public static void reportSyn(int pass, int fails, List<String> accf, List<String> rejf ){
        if(accf.size() > 0){
            System.out.println("Os seguintes arquivos deviam ter sido aceitos:");
            for(String s : accf){
                 System.out.println("    " + s);
            }
        }
        if(rejf.size() > 0){
            System.out.println("Os seguintes arquivos deviam ter sido rejeitados:");
            for(String s : rejf){
                 System.out.println("    " + s);
            }
        }
        if(pass > 1){
            System.out.println(pass + " casos de testes passaram");
        }else if(pass == 1){
            System.out.println("1 caso de teste passou");
        }else{
            System.out.println("Nenhum caso de teste passou :-(");
        }

        if(fails > 1){
            System.out.println(fails + " casos de testes falharam");
        }else if(fails == 1){
            System.out.println("1 caso de teste falhou");
        }else{
            System.out.println("Nenhum caso de teste falhou :-)");
        }
   }




   private static LinkedList<TestInstance> readInstance(String fn) throws IOException {
        LinkedList<TestInstance> inst = new LinkedList<TestInstance>();
        TestInstance ti;
        LinkedList<String> di;
        BufferedReader r = null;
        try{
            r = new BufferedReader(new InputStreamReader(new FileInputStream(fn) ));
            String s = r.readLine();
            while(s != null){
               ti = new TestInstance();
               if(s.equals("---in----")){
                  s = r.readLine();
                  di = new LinkedList<String>();
                  while(s != null && !s.equals("---out---")){
                      di.add(s);
                      s = r.readLine();
                  }
                  if(di.size() > 0){ ti.inData = di;}
               }
               if(s.equals("---out---")){
                  s = r.readLine();
                  di = new LinkedList<String>();
                  while(s != null && !s.equals("---in----")){
                      di.add(s);
                      s = r.readLine();
                  }
                  if(di.size() > 0){ ti.outData = di;}
               }
               inst.add(ti);
            }
            r.close();
        }catch(FileNotFoundException e){
             System.err.println(e.getMessage());
             System.err.println("Arquivo correspondente de instancia nao encontrado");
             r.close();
             inst = null;
        }catch(IOException e){
             System.err.println(e.getMessage());
             r.close();
             inst = null;
        }

        return inst;
   }


   private static boolean runSemTest(File f, String cmd, InstanceResult fails) throws IOException{
        boolean res = true;
        LinkedList<TestInstance> inst = new LinkedList<TestInstance>();
        LinkedList<TestInstance> ltest = readInstance(f.getAbsolutePath().replaceFirst("\\.[^\\.]+$",".inst"));
        if(ltest != null){
            Runtime rn = Runtime.getRuntime();
            Process p;
            BufferedReader procr;
            BufferedWriter procw;
            fails.fails = new LinkedList<TestInstance>();
            for(TestInstance t : ltest){
                 p = rn.exec(cmd +" " + f.getPath());
                 procr = new BufferedReader(new InputStreamReader(p.getInputStream())); // the process outputs
                 procw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream())); //the process inputs
                 if(t.inData != null){
                     for(String din : t.inData){
                         procw.write(din, 0, din.length());
                         procw.newLine();
                         procw.flush();
                     }
                 }if(t.outData != null){
                     LinkedList<String> dout = new LinkedList<String>();
                     String x =  procr.readLine();
                     while(x != null && (dout.size() < t.outData.size()) ){
                         dout.add(x);
                         x =  procr.readLine();
                     }
                     if(t.testOutput(dout)){
                        res = res && true;
                     }else{
                        fails.fails.add(t);
                        fails.fname = f.getName();
                        res = res && false;
                     }
                 }
            }
        }
        return res;
}



   public static void semTests(String cmd) throws IOException {
       // String cmd = "./lang.sh -i";
       String pthSimple = mkPath(new String[]{"semantica","certo","simple"});
       String pthFunc   = mkPath(new String[]{"semantica","certo","function"});
       String pthFull   = mkPath(new String[]{"semantica","certo","full"});
       String[] pths    = new String[]{pthSimple,pthFunc,pthFull};
       LinkedList<InstanceResult> tr = new LinkedList<InstanceResult>();
       try{
          int passed = 0, failed = 0, maxfname;
          for(String spth : pths){
              File[] accfs = list_files(spth,"lan");
              if(accfs == null){
                 System.out.println("Nao ha arquivos para processar em " + spth);
                 System.exit(0);//
              }
              maxfname = maxNameLen(5,accfs);
              byte[] sepbuff  = dotedArr(maxfname,'-');

              System.out.println("Processando casos de teste semanticos em " + spth);
              for(File fsrc : accfs){
                 InstanceResult ir = new InstanceResult();
                 System.out.print(fsrc.getName());
                 System.out.write(sepbuff,0,maxfname - fsrc.getName().length());
                 if(runSemTest(fsrc,cmd,ir)){
                     System.out.println("[ OK ]");
                     passed++;
                 }else{
                     System.out.println("[FAIL]");
                     tr.add(ir);
                     failed++;
                 }
              }
          }
          reportSem(passed, failed, tr);
      }catch(Exception e){
          e.printStackTrace();
      }
   }

   public static void reportSem(int pass, int fails, LinkedList<InstanceResult> sfail){
        if(sfail.size() > 0){
            System.out.println("Os seguintes arquivos produziram resultados incosistentes com o esperado:");
            int k = 0;
            for(InstanceResult s : sfail){
                k = 1;
                System.out.println(s.fname + " falhou em " +  s.fails.size() + " instancias.");
                for(TestInstance tif : s.fails){
                    System.out.println("==== [Caso " + k + " ] ====");
                    System.out.println("Entrada:");
                    if(tif.inData == null){
                        System.out.println("N/A");
                    }else{
                         for(String s1 : tif.inData){
                             System.out.println(s1);
                         }
                    }
                    System.out.println("Saida esperada:");
                    if(tif.outData == null || tif.outData.size() == 0){
                        System.out.println("N/A");
                    }else{
                        for(String s1 : tif.outData){
                             System.out.println("\""+s1+"\"");
                        }
                    }
                    System.out.println("Saida obtida:");
                    if(tif.out == null || tif.out.size() == 0){
                        System.out.println("N/A");
                    }else{
                         for(String s1 : tif.out){
                             System.out.println("\"" + s1 + "\"");
                         }
                    }
                    System.out.println("==== [Fim do caso " + k + " ] ====");
                    k++;
                }
                System.out.println(" ----------x-x-x----------");
            }
        }
        if(pass > 1){
            System.out.println(pass + " casos de testes passaram");
        }else if(pass == 1){
            System.out.println("1 caso de teste passou");
        }else{
            System.out.println("Nenhum caso de teste passou :-(");
        }

        if(fails > 1){
            System.out.println(fails + " casos de testes falharam");
        }else if(fails == 1){
            System.out.println("1 caso de teste falhou");
        }else{
            System.out.println("Nenhum caso de teste falhou :-)");
        }
   }

   public static void main(String[] args) throws IOException {
       BufferedReader confs = new BufferedReader( new FileReader("conf.txt"));
       String cmd1 = confs.readLine();
       String cmd2 = confs.readLine();
       confs.close();
       //System.out.println(cmd1);
       //System.out.println(cmd2);
       //String cmd1 = "java -cp ..:../lang/tools/java-cup-11b-runtime.jar lang/LangCompiler -syn";
       //String cmd2 = "java -cp ..:../lang/tools/java-cup-11b-runtime.jar lang/LangCompiler -i";
       if(args.length  == 0){
         synTests(cmd1);
         semTests(cmd2);
       }else if(args[0].equals("-syn")){
         synTests(cmd1);
       }else if(args[0].equals("-sem")){
         semTests(cmd2);
       }else if(args[0].equals("-test")){
         testRun(cmd1);
       }else{
          System.out.println("Chame com -syn ou -sem ou sem parâmetros.");
       }
   }

}
 
