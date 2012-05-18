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
 *
 * @author hoshun
 */
public class NormalizedWordVector extends WordVector {

    public static void main(String[] args) {
        WordVector wv = new WordVector(new String[]{"a", "1.5"});
        NormalizedWordVector nwv = new NormalizedWordVector(wv);
        System.out.println(nwv.wordvector.values());
        System.out.println(nwv.toFormattedString("\t") + "END");

    }

    public NormalizedWordVector(String[] tokens) {
        super(tokens);
        this.normalized();
    }

    public NormalizedWordVector(WordVector wv) {
        super(wv);
        this.normalized();
    }

    private void normalized() {
        double norm = this.norm();
        for (Map.Entry<String, Double> en : this.wordvector.entrySet()) {
            String word = en.getKey();
            double v = en.getValue();
            this.wordvector.put(word, v / norm);
        }
    }

    public String toFormattedString(String sep) {
        if (this.wordvector.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Double> en : this.wordvector.entrySet()) {
            String word = en.getKey();
            Double value = en.getValue();
            sb.append(word);
            sb.append(sep);
            sb.append(value);
            sb.append(sep);
        }
        int last = sb.lastIndexOf(sep);
        sb.replace(last, sb.length(), "");

        return sb.toString();
    }

    /**
     * Read the word vectors file and normalize the word vectors.
     *
     * @param infile
     * @return
     */
    public static SortedMap<Category, NormalizedWordVector> readNormalizedWordVectors(File infile) {
        SortedMap<Category, NormalizedWordVector> map = new TreeMap<Category, NormalizedWordVector>();
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));

                while (in.hasNext()) {
                    String[] tokens = in.nextLine().split("\t");
                    Category cat = new Category(tokens[0], tokens[1]);
                    String[] wordtfidf = Arrays.copyOfRange(tokens, 2, tokens.length);
                    NormalizedWordVector nwv = new NormalizedWordVector(wordtfidf);
                    map.put(cat, nwv);
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
