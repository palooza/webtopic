/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * The representation of bag of the words model. Support operations like inner
 * product, norm, and add.
 *
 * @author hoshun
 */
public class WordVector {

    public static void main(String[] args) {
        WordVector wv1 = new WordVector(new String[]{"a", "1.5", "c", "2", "e", "5"});
        WordVector wv2 = new WordVector(new String[]{"a", "2.0", "c", "1.3", "e", "5"});
        System.out.println(wv1.innerProduct(wv2));
    }

    public WordVector(WordVector wv) {
        this.wordvector = wv.wordvector;
    }

    public WordVector(String[] vec) {
        wordvector = new TreeMap<String, Double>();
        for (int i = 0; i < vec.length; i = i + 2) {
            String word = vec[i];
            double tfidf = Double.parseDouble(vec[i + 1]);
            wordvector.put(word, tfidf);
        }
    }

    public double norm() {
        double norm2 = 0.0;
        for (double v : this.wordvector.values()) {
            norm2 += Math.pow(v, 2);
        }
        return Math.sqrt(norm2);
    }

    public double innerProduct(WordVector other) {
        double result = 0;

        SortedSet<String> keys1 = (SortedSet) this.wordvector.keySet();
        Iterator<String> iter1 = keys1.iterator();
        SortedSet<String> keys2 = (SortedSet) other.wordvector.keySet();
        Iterator<String> iter2 = keys2.iterator();

        String s1 = "";
        String s2 = "";
        if (iter1.hasNext() && iter2.hasNext()) {
            s1 = iter1.next();
            s2 = iter2.next();
        } else {
            return 0.0;
        }

        while (true) {
            //System.out.println("s1: " + s1 + "\t" + "s2:" + s2);
            if (s1.equals(s2)) {
                result += this.wordvector.get(s1) * other.wordvector.get(s2);
                if (iter1.hasNext() && iter2.hasNext()) {
                    s1 = iter1.next();
                    s2 = iter2.next();
                } else {
                    return result;
                }
            } else if (s1.compareTo(s2) > 0 && iter2.hasNext()) {// s1 > s2
                s2 = iter2.next();
            } else if (s1.compareTo(s2) < 0 && iter1.hasNext()) {
                s1 = iter1.next();
            } else {
                return result;
            }

        }
        //return result;
    }

    public void add(WordVector other) {

        SortedSet<String> keys1 = (SortedSet) this.wordvector.keySet();
        Iterator<String> iter1 = keys1.iterator();
        SortedSet<String> keys2 = (SortedSet) other.wordvector.keySet();
        Iterator<String> iter2 = keys2.iterator();

        String s1 = "";
        String s2 = "";
        if (iter1.hasNext() && iter2.hasNext()) {
            s1 = iter1.next();
            s2 = iter2.next();
        } else {
            return;
        }

        while (true) {
            //System.out.println("s1: " + s1 + "\t" + "s2:" + s2);
            if (s1.equals(s2)) {
                double sum = this.wordvector.get(s1) + other.wordvector.get(s2);
                this.wordvector.put(s1, sum);
                if (iter1.hasNext() && iter2.hasNext()) {
                    s1 = iter1.next();
                    s2 = iter2.next();
                } else {
                    return;
                }
            } else if (s1.compareTo(s2) > 0 && iter2.hasNext()) {// s1 > s2
                s2 = iter2.next();
            } else if (s1.compareTo(s2) < 0 && iter1.hasNext()) {
                s1 = iter1.next();
            } else {
                return;
            }

        }
    }

    /**
     * Read the word vectors file and normalize the word vectors.
     *
     * @param infile
     * @return
     */
    public static SortedMap<Category, WordVector> readWordVectors(File infile) {
        SortedMap<Category, WordVector> map = new TreeMap<Category, WordVector>();
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));

                while (in.hasNext()) {
                    String[] tokens = in.nextLine().split("\t");
                    Category cat = new Category(tokens[0], tokens[1]);
                    String[] wordtfidf = Arrays.copyOfRange(tokens, 2, tokens.length);
                    WordVector wv = new WordVector(wordtfidf);
                    map.put(cat, wv);
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    protected SortedMap<String, Double> wordvector;
}
