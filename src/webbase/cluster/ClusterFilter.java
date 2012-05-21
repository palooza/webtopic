/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import java.io.*;
import java.util.*;

import util.CategorizedURLs;

/**
 * Filter clusters that are not needed. 
 * 
 * @author hoshun
 */
public class ClusterFilter {
    
    public static void main(String[] args) {
        String dir = "/media/netdisk/hoshun/webtopic/wb3/hdfsdat/";
        
        File dmozIndexedUrl = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/dmoz-indexed-url");
        File urlAndClusterTableOutFile = new File(dir + "helper/initcluster-sorted-url-table.dat");
        
        File filteredClusterUrlTable = new File( dir + "helper/filtered-cluster-url-table.dat");
        
        Set<String> topics = new TreeSet(Arrays.asList(new String [] {"Arts", "Science", "Society", "Computers"}));
        
        //RUN
        filter(topics, urlAndClusterTableOutFile, filteredClusterUrlTable, dmozIndexedUrl);
    }
    /**
     * Filter cluster that has no page in the category set. 
     * Filter cluster with less than 10 pages. 
     */
    public static void filter(Set<String> topics, 
            File clusterUrlTableInfile, 
            File filteredClusterUrlTableOutfile,
            File dmozIndexedUrlInfile){
        
        try {
            Scanner in = null;
            PrintWriter out = null;
            try{
                in = new Scanner(new BufferedReader(new FileReader(clusterUrlTableInfile)));
                out = new PrintWriter(filteredClusterUrlTableOutfile);
                
                CategorizedURLs urlToCategory = new CategorizedURLs(dmozIndexedUrlInfile);
                                
                String line = null;
                if(in.hasNextLine()){
                    line = in.nextLine();
                }
                
                Cluster cluster = new Cluster(line);
                while(line != null){
                    line = cluster.fillCluster(in);
                    // do something with the cluster!
                    // filter cluster that are too small
                    // filter cluster without labeled page in the topics set. 
                    if(cluster.numOfMember() >= 10 
                            && cluster.hasMemberInSpecifiedTopics(topics, urlToCategory)){
                        cluster.printColumnFormat(out);
                    }
                                        
                }
                
                
            }finally{
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
