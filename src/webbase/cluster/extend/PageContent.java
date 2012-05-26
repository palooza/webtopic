/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster.extend;

import java.util.*;
import java.io.*;

/**
 * Retrieve content of the page which is in the extended cluster.
 *
 * @author hoshun
 */
public class PageContent {

    public static void main(String[] args) {
        File extendClusterInfile =
                new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/extend-clusterid-url-table.hdfs.dat");
        File extendPageContent = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/extend-page-content.hdfs.dat");

        //writeContentOfExtendCluster(extendClusterInfile);
        writeContentOfExtendCluster(extendClusterInfile, args[0], args[1]);
    }

    public static void writeContentOfExtendCluster(File extendClusterInfile, String pagecontentFile,
            String outfile) {

        try {
            Scanner in = null;
            PrintWriter out = null;

            /*
             * String pagecontentFile =
             * "/media/netdisk/hoshun/webtopic/wb3/hdfsdat/pagecontent/pagecontent-"
             * + docid; String outfile =
             * "/media/netdisk/hoshun/webtopic/wb3/hdfsdat/filtered-" + docid;
             *
             */

            try {
                in = new Scanner(new BufferedReader(
                        new FileReader(new File(pagecontentFile))));
                out = new PrintWriter(new File(outfile));

                Map<Integer, Set<Integer>> urlidToCategories =
                        urlidToCategories(extendClusterInfile);

                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] tokens = line.split(" ");
                    int urlid = Integer.parseInt(tokens[0]);
                    if (urlidToCategories.containsKey(urlid)) {
                        Set<Integer> clusterids = urlidToCategories.get(urlid);
                        for (int clusterid : clusterids) {
                            out.printf("%d ", clusterid);
                            out.printf("%s\n", line);
                        }
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

    public static Map<Integer, Set<Integer>> urlidToCategories(File extendClusterInfile) {

        Map<Integer, Set<Integer>> urlidToCategories = new TreeMap<Integer, Set<Integer>>();

        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(extendClusterInfile)));

                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] tokens = line.split("\t");
                    int clusterid = Integer.parseInt(tokens[0]);
                    int urlid = Integer.parseInt(tokens[2]);

                    if (urlidToCategories.containsKey(urlid)) {
                        urlidToCategories.get(urlid).add(clusterid);
                    } else {
                        Set<Integer> clusterSet = new TreeSet<Integer>();
                        clusterSet.add(clusterid);
                        urlidToCategories.put(urlid, clusterSet);
                    }
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urlidToCategories;

    }
}
