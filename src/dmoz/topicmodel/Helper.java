/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.topicmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author hoshun
 */
public class Helper {

    public static Map<String, Integer> getWordsMap(File infile) {
        Map<String, Integer> map = new TreeMap<String, Integer>();
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));

                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    String word = tokens[0];
                    int c = Integer.parseInt(tokens[1]);
                    map.put(word, c);
                }

            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static int getLineCount(File infile) {
        int count = 0;
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));

                while (in.hasNextLine()) {
                    in.nextLine();
                    count++;
                }

            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }
}
