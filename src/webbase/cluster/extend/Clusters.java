/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster.extend;

import java.util.*;
import java.io.*;

/**
 *
 * @author hoshun
 */
public class Clusters {

    public static void main(String[] args) {
    }

    public Clusters() {
        step = 0;
        clusterIdToMembers = new TreeMap<Integer, Set<Integer>>();
        urlIdToClusterId = new TreeMap<Integer, Set<Integer>>();
        nextUrlToClusterId = new TreeMap<Integer, Set<Integer>>();
    }

    public static int padding = 10000;
    
    public static Clusters getInitialClusters(File initClusterFile, File extendClusterOutfile) {
        Clusters c = new Clusters();
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(initClusterFile)));
                out = new PrintWriter(extendClusterOutfile);
                
                // the format is clusterid, urlid, url, adjlists
                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    int clusterid = Integer.parseInt(tokens[0]);
                    int urlid = Integer.parseInt(tokens[1]);
                    String url = tokens[2];
                    String [] adjtokens = Arrays.copyOfRange(tokens,3,tokens.length);
                    // remove hub node
                    if(adjtokens.length >= 40){
                        continue;
                    }
                    
                    out.printf("%s\t%s\t%s\t%s",clusterid+padding, c.step, urlid, url);
                    for(int i=0; i < adjtokens.length; i=i+2){
                        out.printf("\t%s\t%s",adjtokens[i], adjtokens[i+1]);
                    }
                    out.printf("\n");
                    
                    if (!c.clusterIdToMembers.containsKey(clusterid)) {
                        Set<Integer> memset = new TreeSet<Integer>();
                        memset.add(urlid);
                        c.clusterIdToMembers.put(clusterid, memset);
                    } else {
                        c.clusterIdToMembers.get(clusterid).add(urlid);
                    }

                    // add to nextUrltoCategory
                    c.putAdjlistToClusterIDforNextTraversal(clusterid,adjtokens);
                }

                // switch
                c.toNextTraversal();


            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded the clusters");
        return c;
    }

    public void putAdjlistToClusterIDforNextTraversal(int clusterid, String[] adjtokens) {
        Set<Integer> visitedSet = clusterIdToMembers.get(clusterid);
        for (int i = 0; i < adjtokens.length; i = i + 2) {
            int urlid = Integer.parseInt(adjtokens[i]);
            
            if(visitedSet.contains(urlid)){
                continue;
            }
                        
            if (!nextUrlToClusterId.containsKey(urlid)) {
                Set<Integer> clusterids = new TreeSet<Integer>();
                clusterids.add(clusterid);
                nextUrlToClusterId.put(urlid, clusterids);
            } else {
                nextUrlToClusterId.get(urlid).add(clusterid);
            }
        }
    }

    public boolean isTargetUrl(int id) {
        return urlIdToClusterId.containsKey(id);
    }

    public Set<Integer> getClusterIDsOfTargetURL(int id) {
        return urlIdToClusterId.get(id);
    }

    public void setVisited(int clusterid, int urlid){
        clusterIdToMembers.get(clusterid).add(urlid);
    }
    
    /**
     * Update the state of the clusters
     */
    public void toNextTraversal() {
        step++;
        // switch
        urlIdToClusterId = nextUrlToClusterId;
        nextUrlToClusterId = new TreeMap<Integer, Set<Integer>>();
    }
    
    public int step;
    private Map<Integer, Set<Integer>> urlIdToClusterId;
    private Map<Integer, Set<Integer>> nextUrlToClusterId;
    private Map<Integer, Set<Integer>> clusterIdToMembers;
}
