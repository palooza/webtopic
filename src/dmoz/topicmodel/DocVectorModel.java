/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.topicmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author hoshun
 */
public class DocVectorModel {

    public static void main(String[] args) {
        
        File docfile = new File("/home/hoshun/dmoz/dmoz-doc-sorted-content");
        File vecfile = new File("/home/hoshun/dmoz/dmoz-doc-normalized-tfidf");
        File wordfile = new File("/home/hoshun/dmoz/word-trim-profile");
        writeNormalizedTFIDFVectors(docfile, vecfile, wordfile);
         //System.out.println( Helper.getLineCount(vecfile));
    }

    /**
     * (1 + log( fij ) x log( N / ni )
     */
    public static void writeNormalizedTFIDFVectors(File docfile, File vecfile, File wordfile) {

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(docfile)));
                out = new PrintWriter(vecfile);

                int totalNumberOfDoc = Helper.getLineCount(docfile);
                Map<String, Integer> wordToAppearnce = Helper.getWordsMap(wordfile);

                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    Map<String,Double> wordToTFIDF = docToTFIDFVector(tokens, totalNumberOfDoc, wordToAppearnce);
                    double vecNorm = vectorNorm(wordToTFIDF.values());
                    out.printf("%s\t%s", tokens[0], tokens[1]);
                    for(Map.Entry<String,Double> en : wordToTFIDF.entrySet()){
                        String word = en.getKey();
                        double tfidf = en.getValue();
                        out.printf("\t%s\t%s", word, (tfidf / vecNorm ) );
                    }
                    out.printf("\n");
                }

            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * (1 + log( fij ) x log( N / ni )
     */
    private static Map<String, Double> docToTFIDFVector(String[] tokens, int N, Map<String, Integer> wordToAppearance) {
        Map<String, Double> wordToTFIDF = new TreeMap<String, Double>();
              
        Map<String, Double> wordToCount = parseDocToWordCount(tokens, wordToAppearance);

        for (Map.Entry<String, Double> en : wordToCount.entrySet()) {
            String word = en.getKey();
            double c = en.getValue();
            assert( c != 0);
            double TF = 1.0 + Math.log(c);
            double IDF = Math.log(N / (double) wordToAppearance.get(word));
            double TFIDF = TF * IDF;
            
            wordToTFIDF.put(word,TFIDF);
        }

        return wordToTFIDF;
    }
    
    private static Map<String, Double> parseDocToWordCount(String [] tokens, Map<String, Integer> wordToAppearance){
        Map<String, Double> wordToCount = new TreeMap<String, Double>();
        for (int i = 2; i < tokens.length; i = i + 2) {
            String word = tokens[i];
            double c = Double.parseDouble(tokens[i + 1]);
            if (wordToAppearance.containsKey(word)) {
                wordToCount.put(word, c);
            }
        }
        return wordToCount;
    }
    
    public static double vectorNorm(Collection<Double> values){
        double norm2 = 0.0;
        for(double v : values){
            norm2 += Math.pow(v,2);
        }
        return Math.sqrt(norm2);
    }
    
}
