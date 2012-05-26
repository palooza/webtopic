/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster.extend;

import java.util.*;
import java.io.*;

/**
 * Extend cluster from the initial clusterid, urlid, url, adjlist table.
 *
 * The new format would be clusterid, step:int, urlid:int, url:string,
 * adjlist:list(int,linktype);
 *
 * @author hoshun
 */
public class ExtendCluster {

    public static void main(String[] args) {
        File urlTableInfile =
                new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/url-table.hdfs.dat");
        File filteredClusterUrlTableInfile =
                new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/filtered-breaked-clusterid-url-table.hdfs.dat");
        File extendedClusterOutfile =
                new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/extend-clusterid-url-table.hdfs.dat");

        Clusters clusters = Clusters.getInitialClusters(filteredClusterUrlTableInfile,
                new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/extend-temp"));
        iteration(clusters, 3, urlTableInfile, extendedClusterOutfile);
    }

    public static int padding = 10000;
    
    public static void iteration(Clusters clusters, int numOfIteration,
            File urlTableInfile, File extendedClusterOutfile) {

        PrintWriter out = null;
        try {
            out = new PrintWriter(extendedClusterOutfile);

            // Traverse url-table n iterations to obtain n-hop neighbor
            for (int iter = 1; iter <= numOfIteration; iter++) {
                System.out.println("Starting iteration: " + iter);

                Scanner in = new Scanner(new BufferedReader(new FileReader(urlTableInfile)));
                try {
                    //
                    while (in.hasNextLine()) {
                        String[] tokens = in.nextLine().split("\t");
                        int urlid = Integer.parseInt(tokens[0]);

                        if (clusters.isTargetUrl(urlid)) {
                            String url = tokens[1];
                            String[] adjtokens = Arrays.copyOfRange(tokens, 2, tokens.length);
                            // remove hub node
                            if(adjtokens.length >= 40){
                                continue;
                            }
                            
                            Set<Integer> clusteridList = clusters.getClusterIDsOfTargetURL(urlid);

                            //Print 
                            for (int clusterid : clusteridList) {
                                // prevent looping in each cluster
                                clusters.setVisited(clusterid, urlid);
                                
                                out.printf("%s\t%s\t%s\t", clusterid+padding, clusters.step, urlid);
                                out.printf("%s", url);
                                for (int i = 0; i < adjtokens.length; i = i + 2) {
                                    out.printf("\t%s\t%s", adjtokens[i], adjtokens[i + 1]);
                                }
                                out.printf("\n");

                                clusters.putAdjlistToClusterIDforNextTraversal(clusterid, adjtokens);
                            }


                        }
                    }
                } finally {
                    in.close();
                }

                clusters.toNextTraversal();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
