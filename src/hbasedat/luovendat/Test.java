/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasedat.luovendat;

import java.util.*;
import java.io.*;

import rawparser.RawchunckParser;

/**
 *
 * @author hoshun
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("start");
        File testsort = new File("/home/hoshun/testsortlist");
        File testadj = new File("/home/hoshun/testadjlist");

        try {
            PrintWriter sortlist = new PrintWriter(testsort);
            PrintWriter adjlist = new PrintWriter(testadj);

            int initid = 100;
            int maxid = 110;
            for (int i = initid; i <= (maxid); i++) {
                sortlist.println(i);
            }
            //sortlist.println(RawchunckParser.breakSymbol);

            adjlist.println(RawchunckParser.breakSymbol);
            for (int id = initid; id <= maxid; id++) {
                adjlist.println(id);
                if (id % 5 == 0) {
                    adjlist.println(RawchunckParser.breakSymbol);
                    adjlist.println(RawchunckParser.breakSymbol);
                }
            }
            
            adjlist.println("310");
            adjlist.println("311");
            adjlist.println("312");
            
            adjlist.println(RawchunckParser.breakSymbol);
            for (int id = initid; id <= maxid; id++) {
                adjlist.println(id);
                if (id % 5 == 0) {
                    adjlist.println(RawchunckParser.breakSymbol);
                    adjlist.println(RawchunckParser.breakSymbol);
                }
            }


            sortlist.close();
            adjlist.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
