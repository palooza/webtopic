/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hoshun
 */
public class ProfileWords {

    public static void main(String[] args) {
        File infile = new File("/home/hoshun/dmoz/dmoz-doc-sorted-content");
        File wordprofile = new File("/home/hoshun/dmoz/word-profile");
        File trimwordfile = new File("/home/hoshun/dmoz/word-trim-profile");
        
        //profileWords(infile, wordprofile);
        
        trimWord(wordprofile, trimwordfile, 1);

    }

    public static void trimWord(File infile, File outfile, int th) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    String word = tokens[0].toLowerCase();
                    int c = Integer.parseInt(tokens[1]);
                    if (c >= th && !isBadFormat(word)) {
                        out.println(word + "\t" + c);
                    }
                }

            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void profileWords(File infile, File outfile) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                Map<String, Integer> map = new TreeMap<String, Integer>();

                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    for (int i = 2; i < tokens.length; i = i + 2) {
                        String word = tokens[i];
                        int c = Integer.parseInt(tokens[i + 1]);
                        /*
                         * count number of document a word appears in. 
                         * (not the total number of appearance)
                         */
                        count(map, word, 1);
                    }
                }

                for (Map.Entry<String, Integer> en : map.entrySet()) {
                    String word = en.getKey();
                    int c = en.getValue();
                    out.println(word + "\t" + c);
                }


            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void count(Map<String, Integer> map, String s, int c) {
        if (map.containsKey(s)) {
            map.put(s, map.get(s) + c);
        } else {
            map.put(s, c);
        }
    }
    
    
    private static Pattern numPattern = Pattern.compile("[0-9]");
    private static boolean isWithNum(String word) {
        Matcher matcher = numPattern.matcher(word);
        return matcher.find();
    }
    
    private static Pattern dotPattern = Pattern.compile("\\.");
    private static boolean isWithDot(String word) {
        Matcher matcher = dotPattern.matcher(word);
        return matcher.find();
    }
    
    private static Pattern underscorePattern = Pattern.compile("_");
    private static boolean isWithUnderscore(String word) {
        Matcher matcher = underscorePattern.matcher(word);
        return matcher.find();
    }
    
    private static boolean isBadFormat(String word){
        return isWithNum(word) || isWithDot(word) || isWithUnderscore(word);
    }
    
}
