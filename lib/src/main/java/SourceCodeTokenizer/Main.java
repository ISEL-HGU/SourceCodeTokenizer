/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package SourceCodeTokenizer;

public class Main {
    /*
    input: 1) option 2) file list path, 3) output path
    Options: all | methods
    (example) all ../JavaFileExtractor/deeplearning4j_files.txt ./
    (example) methods ../JavaFileExtractor/deeplearning4j_files.txt ./
    output: tokenized files
     */
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        if(args.length != 3){
            System.err.println("input: 1) option 2) file list path, 3) output path\n" +
                    "    Options: all | methods\n" +
                    "    (example) all ../JavaFileExtractor/deeplearning4j_files.txt ./\n" +
                    "    (example) methods ../JavaFileExtractor/deeplearning4j_files.txt ./");
            System.exit(-1);
        }
        String option = args[0];
        String fileListPath = args[1];
        String outputPath = args[2];
        SourceCodeTokenizer t = new SourceCodeTokenizer(fileListPath);

        if(option.equals("all")){
            t.tokenizeAll(outputPath);
        }
        else if(option.equals("methods")){
            t.tokenizeMethods(outputPath);
            System.out.println("Number of Parse Error: " + t.getNumParseError());
        }
        else {
            System.err.println("input: 1) option 2) file list path, 3) output path\n" +
                    "    Options: all | methods\n" +
                    "    (example) all ../JavaFileExtractor/deeplearning4j_files.txt ./\n" +
                    "    (example) methods ../JavaFileExtractor/deeplearning4j_files.txt ./");
        }
        System.out.println("Running Time (sec): " + ((System.currentTimeMillis() - start) / 1000));
    }
}
