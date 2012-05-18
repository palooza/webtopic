/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Arrays;
import java.util.Comparator;
import java.io.*;
import java.util.*;

/**
 * Read the sorted URL list and use the ordering as id (started from 0).
 * @author hoshun
 */
public class UrltoIndexBimap {

    public static void main(String[] args) {
        String a = null;
        System.out.println(!(a = "b").equals("b"));
        System.out.println(a);
        try {
            UrltoIndexBimap map = new UrltoIndexBimap(new File("/home/hoshun/01-2011-sorted-url-18"));
            int id = map.getIndex("    www.whitehouse.gov/ ");
            System.out.println("the = " + id);
            if(id < 0){
                System.out.println( map.getUrl(-id));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*
     * The input file has the id, crc32 hash and url seperated by tab. The url
     * must be sorted. The array size is bounded by int.
     *
     * All URLs are sorted in lower case.
     */
    public UrltoIndexBimap(File infile) throws IOException {
        Scanner in = null;
        size = countNumOfLine(infile);
        System.out.println(infile + "has " + size + " lines.");

        urlarr = new String[size];
        try {
            int id = 0;
            in = new Scanner(new BufferedReader(new FileReader(infile)));
            while (in.hasNextLine()) {
                String url = in.nextLine();
                urlarr[ id] = url.trim().toLowerCase();
                id++;
            }

            // TODO: find the difference between UNIX sort and Java String CompareTo
            // this is a quick fix...
            // Becasue www.adobe.com/ is not searchable at the first time.
            Arrays.sort(urlarr);

            //TODO: for simple debug
            int dummyid = Arrays.binarySearch(urlarr, rawparser.RawchunckParser.breakSymbol.toLowerCase().trim());
            if (dummyid >= 0) {
                urlarr[dummyid] = rawparser.RawchunckParser.breakSymbol + "dummy";
            }

        } finally {
            in.close();
        }
    }

    public int getIndex(String url) {
        return Arrays.binarySearch(urlarr, url.trim().toLowerCase());
    }

    public String getUrl(int id) {
        return urlarr[id];
    }

    private static int countNumOfLine(File infile) throws IOException {
        int count = 0;
        Scanner in = null;
        try {
            in = new Scanner(new BufferedReader(new FileReader(infile)));
            while (in.hasNextLine()) {
                count++;
                in.nextLine();
            }
        } finally {
            in.close();
        }
        return count;
    }
    public String[] urlarr;
    public int size;
}
