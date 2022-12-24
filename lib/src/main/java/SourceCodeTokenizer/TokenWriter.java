package SourceCodeTokenizer;

import com.github.javaparser.JavaToken;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.terrier.indexing.tokenisation.TokenStream;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TokenWriter {
    private String outputPath = "";

    public TokenWriter(String outputPath){
        if(!outputPath.endsWith("/")){
            this.outputPath = outputPath + "/";
        } else {
            this.outputPath = outputPath;
        }
        createOutputPath(this.outputPath);
    }

    public void writeUTFWithoutComments(int cnt, String methodSignature, TokenStream ts){
        BufferedWriter bw = null;
        String tmpOutputPath = outputPath + "UTFWithoutComments.txt";

        try {
            File file = new File(tmpOutputPath);
            createFile(file);
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write("$$METHOD#"+ cnt + "\n"+ "$$METHOD_SIGNATURE:" + methodSignature + "\n");
            while(ts.hasNext()){
                String token = "";
                if((token = ts.next()) != null)
                    bw.write(token + " ");
            }
            bw.write("\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex) {
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

    public void writeJavaGrammarWithoutComments(int cnt, String methodSignature, BlockStmt methodBody){
        BufferedWriter bw = null;
        String tmpOutputPath = outputPath + "JavaGrammarWithoutComments.txt";

        try {
            File file = new File(tmpOutputPath);
            createFile(file);
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write("$$METHOD#"+ cnt + "\n"+ "$$METHOD_SIGNATURE:" + methodSignature + "\n");
            StringBuilder sb = new StringBuilder();

            if(methodBody.getTokenRange().isPresent()){
                methodBody.getTokenRange().get().forEach(t -> {
                    if(!t.getText().isBlank() && !(t.getText().equals("{") || t.getText().equals("}")) && !t.getCategory().isComment()) {
                        sb.append(t.getText()).append(" ");
                    }
                });
            }
            bw.write(sb.toString());
            bw.write("\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex) {
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

    public void writeBFS(int cnt, String methodSignature, Node.BreadthFirstIterator bfs){
        BufferedWriter bbw = null;
        BufferedWriter pbw = null;
        String bfsOutputPath = outputPath + "BFS.txt";
        String pbfsOutputPath = outputPath + "PBFS.txt";

        try {
            File bfsFile = new File(bfsOutputPath);
            File pbfsFile = new File(pbfsOutputPath);
            createFile(bfsFile);
            createFile(pbfsFile);
            FileWriter bfw = new FileWriter(bfsFile, true);
            FileWriter pfw = new FileWriter(pbfsFile, true);
            bbw = new BufferedWriter(bfw);
            pbw = new BufferedWriter(pfw);
            bbw.write("$$METHOD#"+ cnt + "\n"+ "$$METHOD_SIGNATURE:" + methodSignature + "\n");
            pbw.write("$$METHOD#"+ cnt + "\n"+ "$$METHOD_SIGNATURE:" + methodSignature + "\n");
            StringBuilder bsb = new StringBuilder();
            StringBuilder psb = new StringBuilder();
            while(bfs.hasNext()){
                Node n = bfs.next();
                if(n instanceof BlockStmt) continue;
                // BFS
                bsb.append(n.getClass().getSimpleName()).append(" ");
                // PBFS
                if(n.getChildNodes().size() == 1){
                    if(n.getChildNodes().get(0).toString().equals(n.toString())){
                        continue;
                    }
                }
                psb.append(n.getClass().getSimpleName()).append(" ");
            }
            bbw.write(bsb.toString());
            pbw.write(psb.toString());
            bbw.write("\n");
            pbw.write("\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
                if(bbw!=null)
                    bbw.close();
                if(pbw!=null)
                    pbw.close();
            }catch(Exception ex) {
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

    public void writeDFS(int cnt, String methodSignature, Node.PreOrderIterator dfs){
        BufferedWriter dbw = null;
        BufferedWriter pbw = null;
        String dfsOutputPath = outputPath + "DFS.txt";
        String pdfsOutputPath = outputPath + "PDFS.txt";

        try {
            File dfsFile = new File(dfsOutputPath);
            File pdfsFile = new File(pdfsOutputPath);
            createFile(dfsFile);
            createFile(pdfsFile);
            FileWriter bfw = new FileWriter(dfsFile, true);
            FileWriter pfw = new FileWriter(pdfsFile, true);
            dbw = new BufferedWriter(bfw);
            pbw = new BufferedWriter(pfw);
            dbw.write("$$METHOD#"+ cnt + "\n"+ "$$METHOD_SIGNATURE:" + methodSignature + "\n");
            pbw.write("$$METHOD#"+ cnt + "\n"+ "$$METHOD_SIGNATURE:" + methodSignature + "\n");
            StringBuilder dsb = new StringBuilder();
            StringBuilder psb = new StringBuilder();
            while(dfs.hasNext()){
                Node n = dfs.next();
                if(n instanceof BlockStmt) continue;
                // DFS
                dsb.append(n.getClass().getSimpleName()).append(" ");
                // PDFS
                if(n.getChildNodes().size() == 1){
                    if(n.getChildNodes().get(0).toString().equals(n.toString())){
                        continue;
                    }
                }
                psb.append(n.getClass().getSimpleName()).append(" ");
            }

            dbw.write(dsb.toString());
            pbw.write(psb.toString());
            dbw.write("\n");
            pbw.write("\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try{
                if(dbw!=null)
                    dbw.close();
                if(pbw!=null)
                    pbw.close();
            }catch(Exception ex) {
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }
    public void createOutputPath(String outputPath){
        File f = new File(outputPath);
        if(!f.exists()){
            f.mkdirs();
        }
    }

    public void createFile(File file){
        try{
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e){
            e.printStackTrace();
            System.err.println("ERROR: output file is not created!");
            System.exit(-1);
        }
    }
}
