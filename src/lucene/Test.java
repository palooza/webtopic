/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene;

import java.util.*;
import java.io.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ReusableAnalyzerBase;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import org.apache.lucene.analysis.snowball.SnowballAnalyzer;

import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.SnowballProgram;


import org.apache.lucene.util.*;

/**
 *
 * @author hoshun
 */
public class Test {

    public static void main(String[] args) {


        Reader in = new StringReader("Hi, this is michael. Do you want to put it off? Take off");
        String ins = "Hi, he she her this is michael. Do you want to put it off? Take off";
        
        StandardTokenizer tokenizer = new StandardTokenizer(Version.LUCENE_36, in);

        StandardAnalyzer stda = new StandardAnalyzer(Version.LUCENE_36);


        ArrayList<String> ls = tokenizeString(stda, ins);

        for (String str : ls) {
            System.out.println(str);
        }
        System.out.println("-------------------------------");

        SnowballAnalyzer sana = new SnowballAnalyzer(Version.LUCENE_36, "English", StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        ArrayList<String> ls2 = tokenizeString(sana, ins);
        for (String str : ls2) {
            System.out.println(str);
        }

        EnglishStemmer s = new EnglishStemmer();
        s.setCurrent("this is community communism");
        System.out.println(s.getCurrent());

        
        
        
        System.out.println("---------------------------------------------");
        
        String [] arr = {"hi","he","she","her"};
        Set<String> mystopwords = new TreeSet<String>(Arrays.asList(arr));
        
        Analyzer a = new EnglishAnalyzer(Version.LUCENE_36, mystopwords);
        

        ArrayList<String> ls3 = tokenizeString(a, ins);
        for (String str : ls3) {
            System.out.println(str);
        }

        
    }

    public static ArrayList<String> tokenizeString(Analyzer analyzer, String input) {
        ArrayList<String> rs = new ArrayList<String>();

        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(input));
            while (stream.incrementToken()) {
                rs.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}
