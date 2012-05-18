/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rawparser;

import java.util.*;
import java.io.*;

/**
 * The UrlsIndexer takes all the valid and uniquely sorted urls as input and
 * outputs the urls and their associated domain level and url level indexes.
 *
 * @author hoshun
 */
public class UrlsIndexer {

    public static void main(String[] args) {
        System.out.println( Long.MAX_VALUE);
        String input = args[0];
        String output = args[1];
        File infile = new File(input);
        File outfile = new File(output);
    }

    public static void index(File infile, File outfile) {

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);
                
                
                while(in.hasNext()){
                    String line = in.nextLine();
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
