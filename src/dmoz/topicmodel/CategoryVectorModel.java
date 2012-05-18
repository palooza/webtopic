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


import dmoz.util.*;
/**
 *
 * @author hoshun
 */
public class CategoryVectorModel {

    public static void main(String[] args) {

        File vecfile = new File("/home/hoshun/dmoz/dmoz-doc-normalized-tfidf");
        File cat2file = new File("/home/hoshun/dmoz/dmoz-category2-normalized-tfidf");
        File cat1file = new File("/home/hoshun/dmoz/dmoz-category1-normalized-tfidf");
        
        writeTopic2TFIDF(vecfile, cat2file);
        writeTopic1TFIDF(vecfile, cat1file);
    }
    

    

    public static void writeTopic2TFIDF(File vecfile, File cat2file) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(vecfile)));
                out = new PrintWriter(cat2file);

                Map<Category,WordVector> catmap = new TreeMap<Category,WordVector>();
                while(in.hasNextLine()){
                    String [] tokens = in.nextLine().split("\t");
                    Category cat = new Category(tokens[0], tokens[1]);
                    WordVector wv = new WordVector( Arrays.copyOfRange(tokens, 2, tokens.length) );
                    if(catmap.containsKey(cat)){
                        catmap.get(cat).add(wv);
                    }else{
                        catmap.put(cat,wv);
                    }
                }
                
                for(Map.Entry<Category,WordVector> en : catmap.entrySet()){
                    Category cat = en.getKey();
                    WordVector wv = en.getValue();
                    NormalizedWordVector nwv = new NormalizedWordVector(wv);
                    //debug
                    if( Math.abs(nwv.norm() - 1) > 0.00001){
                        System.out.println("ERROR");
                    }
                    out.printf("%s\t%s\t",cat.first, cat.second);
                    out.println(nwv.toFormattedString("\t"));
                }
                
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
        public static void writeTopic1TFIDF(File vecfile, File cat1file) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(vecfile)));
                out = new PrintWriter(cat1file);

                Map<Category,WordVector> catmap = new TreeMap<Category,WordVector>();
                while(in.hasNextLine()){
                    String [] tokens = in.nextLine().split("\t");
                    Category cat = new Category(tokens[0], "");
                    WordVector wv = new WordVector( Arrays.copyOfRange(tokens, 2, tokens.length) );
                    if(catmap.containsKey(cat)){
                        catmap.get(cat).add(wv);
                    }else{
                        catmap.put(cat,wv);
                    }
                }
                
                for(Map.Entry<Category,WordVector> en : catmap.entrySet()){
                    Category cat = en.getKey();
                    WordVector wv = en.getValue();
                    NormalizedWordVector nwv = new NormalizedWordVector(wv);
                    //debug
                    if( Math.abs(nwv.norm() - 1) > 0.00001){
                        System.out.println("ERROR");
                    }
                    out.printf("%s\t%s\t",cat.first, "");
                    out.println(nwv.toFormattedString("\t"));
                }
                
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
