package SourceCodeTokenizer.javatokenizer;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import org.terrier.indexing.tokenisation.TokenStream;
import org.terrier.indexing.tokenisation.UTFTokeniser;

import java.io.*;

public class FileTokenizer {
    public void parseJavaFileWithComment(String path, String outputPath){
        System.out.println("File Read %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        File f = new File(path);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            System.out.println();
            String str;
            while ((str = reader.readLine()) != null) {
                System.out.println(str);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("UTF Tokenizer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        UTFTokeniser terrier = new UTFTokeniser();
        try {
            TokenStream ts = terrier.tokenise(new FileReader(f));
            while(ts.hasNext()){
                String token = "";
                if((token = ts.next()) != null)
                    System.out.println(token);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Java Grammar Tokenizer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        CompilationUnit cu = StaticJavaParser.parse("class DummyCompilationUnit{}");
        try {
            ParserConfiguration pc = new ParserConfiguration().setAttributeComments(true);
            StaticJavaParser.setConfiguration(pc);
            cu = null;
            cu = StaticJavaParser.parse(f);
            if(cu.getTokenRange().isPresent()){
                cu.getTokenRange().get().forEach(t -> {
                    if(!t.getText().isBlank())
                        System.out.println(t.getText());
                });
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void parseJavaFileWithoutComment(String path, String outputPath){
        ParserConfiguration pc = new ParserConfiguration().setAttributeComments(false);
        StaticJavaParser.setConfiguration(pc);
        File f = new File(path);
        try {
            CompilationUnit cu = StaticJavaParser.parse(f);
            System.out.println("UTF w/o comments Tokenizer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            UTFTokeniser terrier = new UTFTokeniser();
            TokenStream ts = terrier.tokenise(new StringReader(cu.toString()));
            while(ts.hasNext()){
                String token = "";
                if((token = ts.next()) != null)
                    System.out.println(token);
            }

            System.out.println("Java Grammar w/o comments Tokenizer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            CompilationUnit _cu = StaticJavaParser.parse(cu.toString());
            if(_cu.getTokenRange().isPresent()){
                _cu.getTokenRange().get().forEach(t -> {
                    if(!t.getText().isBlank())
                        System.out.println(t.getText());
                });
            }

            System.out.println("BFS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            if(_cu == null){
                System.err.println("Compilation unit is null");
                System.exit(-1);
            }
            Node.BreadthFirstIterator bfs = new Node.BreadthFirstIterator(_cu);
            while(bfs.hasNext()){
                Node n = bfs.next();
                // BFS
                System.out.println(n.getClass().getSimpleName() + " $in code$ " + n.toString());
                // PBFS
                if(n.getChildNodes().size() == 1){
                    if(n.getChildNodes().get(0).toString().equals(n.toString())){
                        System.out.println("pruned");
                    }
                }
            }

            System.out.println("DFS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            Node.PreOrderIterator dfs = new Node.PreOrderIterator(_cu);
            while(dfs.hasNext()){
                Node n = dfs.next();
                // DFS
                System.out.println(n.getClass().getSimpleName() + " $in code$ " + n.toString());
                // PDFS
                if(n.getChildNodes().size() == 1){
                    if(n.getChildNodes().get(0).toString().equals(n.toString())){
                        System.out.println("pruned");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
