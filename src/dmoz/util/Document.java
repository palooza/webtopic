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
public class Document {

    public Document(Category cat, NormalizedWordVector nwv) {
        category = cat;
        normalizedWordVector = nwv;
    }

    public static ArrayList<Document> readFile(File infile) {
        ArrayList<Document> docarr = new ArrayList<Document>();
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));

                while (in.hasNext()) {
                    String[] tokens = in.nextLine().split("\t");
                    Category cat = new Category(tokens[0], tokens[1]);
                    String[] wordtfidf = Arrays.copyOfRange(tokens, 2, tokens.length);
                    NormalizedWordVector nwv = new NormalizedWordVector(wordtfidf);
                    Document doc = new Document(cat, nwv);
                    docarr.add(doc);                    
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docarr;
    }
    public Category category;
    public NormalizedWordVector normalizedWordVector;
}
