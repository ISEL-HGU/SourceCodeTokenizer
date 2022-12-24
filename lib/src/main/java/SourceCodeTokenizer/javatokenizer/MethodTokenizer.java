package SourceCodeTokenizer.javatokenizer;

import SourceCodeTokenizer.TokenWriter;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.terrier.indexing.tokenisation.TokenStream;
import org.terrier.indexing.tokenisation.UTFTokeniser;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class MethodTokenizer {
    Iterator<MethodDeclaration> methodListIterator;
    String fileName = "";
    int cnt = 0;

    public int collectMethods(String path){
        this.methodListIterator = null;
        ParserConfiguration pc = new ParserConfiguration().setAttributeComments(false);
        StaticJavaParser.setConfiguration(pc);
        int parseErrorMethod = 0;
        File f = new File(path);
        String fileName = "";
        String[] p = path.split("/");
        int pLength = p.length;
//        if(pLength > 3 && !p[pLength - 4].equals("Java")){
//            fileName = p[pLength - 4] + "/" + p[pLength - 3] + "/" + p[pLength-2] + "/" + p[pLength-1];
//        }
//        else if (pLength > 2 && !p[pLength - 3].equals("Java")){
//            fileName = p[pLength - 3] + "/" + p[pLength-2] + "/" + p[pLength-1];
//        }
        if (pLength > 1 && !p[pLength - 2].equals("Java")){
            fileName = p[pLength-2] + "/" + p[pLength-1];
        }
        else {
            fileName = p[pLength-1];
        }
        this.fileName = fileName;
        try {
            CompilationUnit cu = StaticJavaParser.parse(f);
            List<MethodDeclaration> methodList = cu.findAll(MethodDeclaration.class);
            this.methodListIterator = methodList.iterator();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(ParseProblemException e){
            parseErrorMethod ++;
            this.fileName = "";
        }
        return parseErrorMethod;
    }

    public int parseJavaMethodWithoutComment(String outputPath){
        int numEmptyMethod = 0;
        TokenWriter tw = new TokenWriter(outputPath);
        //in parse error case, methodListIterator is null
        if(methodListIterator == null){
            return 0;
        }
        while(methodListIterator.hasNext()){
            MethodDeclaration md = methodListIterator.next();
            String methodSignature = fileName + ":" + md.getSignature().toString();
            BlockStmt methodBody;
            cnt++;
            System.out.println(cnt);
            if(md.getBody().isPresent()){
                methodBody = md.getBody().get();
//                System.out.println("UTF w/o comments Tokenizer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                UTFTokeniser terrier = new UTFTokeniser();
                TokenStream ts = terrier.tokenise(new StringReader(methodBody.toString()));
                tw.writeUTFWithoutComments(cnt, methodSignature, ts);

//                System.out.println("Java Grammar w/o comments Tokenizer %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                tw.writeJavaGrammarWithoutComments(cnt, methodSignature, methodBody);

//                System.out.println("BFS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                Node.BreadthFirstIterator bfs = new Node.BreadthFirstIterator(methodBody);
                tw.writeBFS(cnt, methodSignature, bfs);

//                System.out.println("DFS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                Node.PreOrderIterator dfs = new Node.PreOrderIterator(methodBody);
                tw.writeDFS(cnt, methodSignature, dfs);
            }
            else {
                numEmptyMethod ++;
            }
        }
        return numEmptyMethod;
    }
}
