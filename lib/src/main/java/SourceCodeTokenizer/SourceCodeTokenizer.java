package SourceCodeTokenizer;

import SourceCodeTokenizer.javatokenizer.FileTokenizer;
import SourceCodeTokenizer.javatokenizer.MethodTokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SourceCodeTokenizer {
    private Iterator<String> fileList;
    private int bpeSample = -1;
    private int numParseError = 0;
    private int numEmptyMethod = 0;

    public SourceCodeTokenizer(String fileListPath){
        ArrayList<String> list = new ArrayList<>();
        BufferedReader r = null;
        try {
            String l;
            r = new BufferedReader(new FileReader(fileListPath));
            while ((l = r.readLine()) != null) {
                if(l.equals("")) continue;
                list.add(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (r != null)
                    r.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Number of files: " + list.size());
        this.bpeSample = list.size() / 2;
        this.fileList = list.iterator();
    }

    public void tokenizeAll(String outputPath){
        while(fileList.hasNext()){
            FileTokenizer t = new FileTokenizer();
            String f = fileList.next();
            t.parseJavaFileWithComment(f, outputPath);
            t.parseJavaFileWithoutComment(f, outputPath);
            System.exit(0);
        }
    }

    public void tokenizeMethods(String outputPath){
        MethodTokenizer t = new MethodTokenizer();
        while(fileList.hasNext()){
            String f = fileList.next();
            numParseError += t.collectMethods(f);
            numEmptyMethod += t.parseJavaMethodWithoutComment(outputPath);
//            System.exit(0);
        }
    }

    public int getNumParseError(){
        return numParseError;
    }
}
