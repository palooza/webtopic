/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.stat;

import java.io.*;
import java.util.*;

import dmoz.util.Category;
/**
 *
 * @author hoshun
 */
public class ProfileCategories {

    public static void main(String[] args) {
        File infile = new File("/home/hoshun/dmoz/dmoz-sorted-content");
        File outfile = new File("/home/hoshun/dmoz/categories-profile");
        profileCategories(infile, outfile);
    }

    public static void profileCategories(File infile, File outfile) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                Map<Category, Integer> map = new TreeMap<Category, Integer>();

                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    count(map, new Category(tokens[0], tokens[1]));
                }

                for(Map.Entry<Category,Integer> en : map.entrySet()){
                    Category pair = en.getKey();
                    int count = en.getValue();
                    out.println(pair.first + "\t" + pair.second + "\t" + count);
                }
                
                
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void count(Map<Category, Integer> map, Category pr) {
        if (map.containsKey(pr)) {
            map.put(pr, map.get(pr) + 1);
        } else {
            map.put(pr, 1);
        }
    }
}
