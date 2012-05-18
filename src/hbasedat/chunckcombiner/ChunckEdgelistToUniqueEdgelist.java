/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hbasedat.chunckcombiner;

import java.util.*;
import java.io.*;

import hbasedat.luovendat.UrltoIndexBimap;

/**
 * Write every URL in the chunck-edgelist in to a single unique URL file. The
 * file can be sorted and indexed later as an universal index.
 *
 * @author hoshun
 */
public class ChunckEdgelistToUniqueEdgelist {

    public static void main(String[] args) {

        try {

            String baseChunckurl = "/media/netdisk/hoshun/webtopic/wb3/sortbychunck/01-2011-sorted-url-";
            String baseEdgelist =  "/media/netdisk/hoshun/webtopic/wb3/chunckedgelist/01-2011-edgelist-";
            File uniqfile = new File("/media/netdisk/hoshun/webtopic/wb3/01-2011-all-connected-url");
            PrintWriter uniqout = null;
                
            try{
                uniqout = new PrintWriter(uniqfile);
                for (int clabel = 0; clabel <= 18; clabel++) {
                    File chunckurl = new File(baseChunckurl + clabel);
                    File edgelist = new File(baseEdgelist + clabel);
                    chunckEdgelistToUniqueEdgelist(chunckurl, edgelist, uniqout);

                }

            } finally {
                uniqout.close();
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void chunckEdgelistToUniqueEdgelist(File chunckurl, File edgelist, PrintWriter uniqout) throws IOException {

        UrltoIndexBimap urltoIdbimap = new UrltoIndexBimap(chunckurl);

        Scanner elin = null;
        try {
            elin = new Scanner(new BufferedReader(new FileReader(edgelist)));


            boolean[] idused = new boolean[urltoIdbimap.size];

            while (elin.hasNextLine()) {
                String[] tokens = elin.nextLine().split("\t");
                int sid = Integer.parseInt(tokens[0]);
                int tid = Integer.parseInt(tokens[1]);

                if (!idused[sid]) {
                    uniqout.println(urltoIdbimap.getUrl(sid));
                    idused[sid] = true;
                }
                if (!idused[tid]) {
                    uniqout.println(urltoIdbimap.getUrl(tid));
                    idused[tid] = true;
                }

            }


        } finally {
            elin.close();
        }


    }
}
