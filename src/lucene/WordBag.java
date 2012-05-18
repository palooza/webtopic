/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene;

import java.util.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 *
 * @author hoshun
 */
public class WordBag {

    public WordBag(int docid) {
        this.docid = docid;
        bag = new TreeMap<String, Integer>();
    }

    public WordBag() {
        bag = new TreeMap<String, Integer>();
    }

    public void addWords(ArrayList<String> wordlist, int weight) {
        for (String word : wordlist) {
            if (isNumeric(word)) {
                continue;
            }

            if (bag.containsKey(word)) {
                bag.put(word, bag.get(word) + weight);
            } else {
                bag.put(word, weight);
            }
        }
    }

    public int getTotalWrodCount() {
        int count = 0;
        for (int c : bag.values()) {
            count += c;
        }
        return count;
    }

    /**
     * Print the bag words in column format. Easier to parse by Hadoop and
     * HBase.
     *
     * @param out
     * @throws IOException
     */
    public void printColumnFormat(PrintWriter out) throws IOException {
        int totalWordCount = this.getTotalWrodCount();
        for (Map.Entry<String, Integer> en : this.bag.entrySet()) {
            String word = en.getKey();
            int count = en.getValue();
            out.println(docid + "\t"
                    + word + "\t"
                    + count + "\t"
                    + totalWordCount);
        }
    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
    public int docid;
    public Map<String, Integer> bag;
}
