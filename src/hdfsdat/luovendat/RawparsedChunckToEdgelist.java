/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdfsdat.luovendat;

import java.util.*;
import java.io.*;

import rawparser.RawchunckParser;

/**
 *
 * @author hoshun
 */
public class RawparsedChunckToEdgelist {
    public static String task="";
    public static void main(String[] args) {
        task = args[0].replaceAll(".*[0-9]*-[0-9]*-", "");
        try {
            
            File lookupfile = new File(args[0]);
            File linklist = new File(args[1]);
            File edgelist = new File(args[2]);

            long startTime = System.nanoTime();

            UrltoIndexBimap lookup = new UrltoIndexBimap(lookupfile);
            
            long curTime = System.nanoTime();
            float duration = (float) ((curTime - startTime) / Math.pow(10, 9) / 60);
            System.out.printf("loaded the lookup table used %.2f min\n", duration);

            writeIndexedEdgeList(linklist, edgelist, lookup);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeIndexedEdgeList(File linklist, File indexed_edgeList, UrltoIndexBimap lookup) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(linklist)));
                out = new PrintWriter(indexed_edgeList);
                String line = null;
                String curSourceUrl = null;
                int curSourceIndex = 0;
                int targetIndex = 0;
                int cnt = 0;
                int pageCount = 0;
                long startTime = System.nanoTime();
                while (in.hasNextLine()) {
                    cnt++;
                    line = in.nextLine();
                    
                    if (line.equals(RawchunckParser.breakSymbol)) {
                        pageCount++;
                        while (in.hasNextLine() && (curSourceUrl = in.nextLine()).equals(RawchunckParser.breakSymbol)) {
                            // read through all consecutive break symbols. 
                        }
                        if(curSourceUrl.equals(RawchunckParser.breakSymbol))
                            break;
                     
                        curSourceIndex = lookup.getIndex(curSourceUrl);
                        if (curSourceIndex < 0) {
                            //throw new Exception("Cannot find " + curSourceUrl + " in lookup map");
                            System.out.println(task + ": Cannot find SOURCE: " + curSourceUrl + " in lookup map");

                            boolean findNextValidSource = false;
                            while (in.hasNextLine() && !findNextValidSource) {
                                while (in.hasNextLine() && !in.nextLine().equals(RawchunckParser.breakSymbol)) {
                                    // omit all targets pages associated with an invalid source page
                                }
                                while (in.hasNextLine() && (curSourceUrl = in.nextLine()).equals(RawchunckParser.breakSymbol)) {
                                    // read through all consecutive break symbols.
                                }
                                // if next source page is found and it is valid. 
                                if (!curSourceUrl.equals(RawchunckParser.breakSymbol)) {
                                    curSourceIndex = lookup.getIndex(curSourceUrl);
                                    if (curSourceIndex >= 0) {
                                        findNextValidSource = true;
                                    }else{
                                        System.out.println(task + ": Cannot find SOURCE2: " + curSourceUrl + " in lookup map");
                                    }
                                }
                            }
                        }


                    } else {
                        //System.out.println(line);
                        targetIndex = lookup.getIndex(line);
                        if (targetIndex < 0) {
                            System.out.println(task + ": Cannot find TARGET: " + line + " in lookup map");
                        } else {
                            out.println(curSourceIndex + "\t" + targetIndex);
                        }
                    }

                    if (cnt % 100000 == 0) {
                        long curTime = System.nanoTime();
                        float duration = (float) ((curTime - startTime) / Math.pow(10, 9) / 60);
                        int b = (int) (cnt / 10000000);
                        //System.out.printf("line %d \tbatch %d-th\t page\t%d\t used %.2f min\n", cnt, b, pageCount, duration);
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
}