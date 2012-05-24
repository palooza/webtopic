/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.io.*;
import java.util.*;


import util.CategorizedURLs;
import util.UrlToSpecifiedCategory;

/**
 *
 * @author hoshun
 */
public class BreakLargeClusters {

    public static void main(String[] args) {
        File clusteridUrlTableInfile = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/helper/filtered-cluster-url-table.dat");
        File filteredClusterUrlTableOutfile = 
                new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/filtered-breaked-clusterid-url-table.hdfs.dat");
        File dmozeSpecifiedUrlInfile = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/dmoz-specified-url");
        int minClusterSize = 10;
        int maxClusterSize = 100;

        breakLargeCluster(
                clusteridUrlTableInfile,
                filteredClusterUrlTableOutfile,
                dmozeSpecifiedUrlInfile,
                minClusterSize, maxClusterSize);
    }

    /**
     * Recursively break large cluster to smaller one. The targeting cluster
     * size is 100. Only clusters that contain dmoz page in specific topics are
     * kept.
     *
     * @param topics
     * @param clusteridUrlTableInfile
     * @param filteredClusterUrlTableOutfile
     * @param dmozIndexedUrlInfile
     */
    public static void breakLargeCluster(
            File clusteridUrlTableInfile,
            File filteredClusterUrlTableOutfile,
            File dmozSpecifiedUrlInfile,
            int minClusterSize,
            int maxClusterSize) {

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(clusteridUrlTableInfile)));
                out = new PrintWriter(filteredClusterUrlTableOutfile);

                UrlToSpecifiedCategory urlToSpecifiedCategory = new UrlToSpecifiedCategory(dmozSpecifiedUrlInfile, false);

                String line = null;
                if (in.hasNextLine()) {
                    line = in.nextLine();
                }

                int newClusterId = 0;

                while (line != null) {
                    Cluster cluster = new Cluster(line);
                    line = cluster.fillCluster(in);

                    // do nothing if the community is already small enough
                    if (cluster.numOfMember() < maxClusterSize) {
                        continue;
                    }

                    DirectedGraph<Integer, Integer> graph = cluster.toDirectedInnerGraph();

                    // break the cluster and write the resulting clusters to the file
                    //value: Set<Set<Integer> clusters. recursive breaker(graph)
                    RecursiveClusterer recurClusterer = new RecursiveClusterer(graph, minClusterSize, maxClusterSize);
                    Collection<Set<Integer>> memberSets = recurClusterer.cluster();

                    //check if the sub-cluster are kept (having at least a labeled page)
                    Collection<Set<Integer>> labeledMemberSets = filterUnlabeledCluster(memberSets, urlToSpecifiedCategory);

                    // prefix the category pattern to url
                    cluster.prefixCategoryNameToUrls(urlToSpecifiedCategory);
                    
                    //assign the kept sub-cluster a new id (newClusterId) and print
                    for (Set<Integer> newMemberSet : labeledMemberSets) {
                        Cluster newCluster = cluster.makeSubcluster(newClusterId++, newMemberSet);
                        newCluster.printColumnFormat(out);
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

    public static Collection<Set<Integer>> filterUnlabeledCluster(
            Collection<Set<Integer>> clusters,
            UrlToSpecifiedCategory urlToCategory) {

        Iterator iter = clusters.iterator();
        while (iter.hasNext()) {

            Set<Integer> cluster = (Set<Integer>) iter.next();
            boolean hasLabeledPage = false;
            for (int id : cluster) {
                if (urlToCategory.hasId(id)) {
                    hasLabeledPage = true;
                    break;
                }
            }

            if (!hasLabeledPage) {
                iter.remove();
            }
        }

        return clusters;

    }
}
