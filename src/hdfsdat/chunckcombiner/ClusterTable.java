/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdfsdat.chunckcombiner;

import java.io.*;
import java.util.*;

import hdfsdat.luovendat.UrltoIndexBimap;

/**
 * Produce a data file for HBase for loading. The format is Rowid: cluster id.
 * mem-family: url-id (padded by 1000000000). info-family: init-size.
 *
 * @author hoshun
 */
public class ClusterTable {

    public static void main(String[] args) {
            
        try {
            String baseChunckurl = "/media/netdisk/hoshun/webtopic/wb3/sortbychunck/01-2011-sorted-url-";
            String baseMember = "/media/netdisk/hoshun/webtopic/wb3/chunckcluster/%d.member";
            File univurl = new File("/media/netdisk/hoshun/webtopic/wb3/01-2011-all-connected-sorted-uniq-url");
            File clustertabledat = new File("/media/netdisk/hoshun/webtopic/wb3/hbasedat/cluster-table.hdfs.dat");
            
            PrintWriter clusterout = null;
            
           
            try {
                clusterout = new PrintWriter(clustertabledat);
                UrltoIndexBimap univurlmap = new UrltoIndexBimap(univurl);
                
                int clusterid = 0;
                for (int clabel = 0; clabel <= 18; clabel++) {
                    System.out.println("Now processing chunck " + clabel);
                    File chunckurl = new File(baseChunckurl + clabel);
                    File memfile = new File( String.format(baseMember, clabel));
                    clusterid = writeUnivCluster(univurlmap, chunckurl, memfile, clusterout, clusterid);
                    System.out.println("After processing chunck " + clabel + " clusterid = " + clusterid);
                }

            } finally {
                clusterout.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /*
     * In memory.
     */
    public static int writeUnivCluster(UrltoIndexBimap univmap, File chunckurl, File memfile, PrintWriter clusterout, int firstClusterId) {
        int clusterId = firstClusterId;
        int padding = 0;
        try {
            UrltoIndexBimap chunckmap = new UrltoIndexBimap(chunckurl);
            
            int initClusterSize = 800000;
            Scanner memin = null;
            try {
                memin = new Scanner(new BufferedReader(new FileReader(memfile)));
                ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>(initClusterSize);
                extendClustersSize(clusters, initClusterSize);

                //read cluster members in arraylist.
                while (memin.hasNextLine()) {
                    String[] tokens = memin.nextLine().split(" ");
                    int chunckUrlid = Integer.parseInt(tokens[0]);
                    int chunckClid = Integer.parseInt(tokens[1]);

                    int univUrlid = getUrlUnivId(univmap, chunckmap, chunckUrlid);

                    if (univUrlid >= 0) {
                        if (chunckClid >= clusters.size()) {
                            extendClustersSize(clusters, (chunckClid - clusters.size() + 1));
                        }
                        clusters.get(chunckClid).add(univUrlid);
                    }

                }

                //write each cluster and its members to a tab seperated line.
                for (ArrayList<Integer> cluster : clusters) {
                    if (cluster.size() >= 5) {
                        clusterout.printf("%d", clusterId);
                        for (int memid : cluster) {
                            int padmemid = padding + memid;
                            clusterout.printf("\t%d", padmemid);
                        }
                        clusterout.printf("\n");
                        clusterId++;
                    } else {
                        // not used
                    }
                }

            } finally {
                memin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clusterId;
    }

    public static void extendClustersSize(ArrayList<ArrayList<Integer>> cl, int addsize) {
        for (int i = 0; i < addsize; i++) {
            cl.add(new ArrayList<Integer>());
        }
    }

    public static int getUrlUnivId(UrltoIndexBimap univmap, UrltoIndexBimap chunckmap, int chunckid) {
        String url = chunckmap.getUrl(chunckid);
        int univid = univmap.getIndex(url);
        return univid;
    }
}
