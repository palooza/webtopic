/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import dmoz.util.Category;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import java.util.*;
import java.io.*;

import util.UrlToSpecifiedCategory;

import util.CategorizedURLs;

/**
 *
 * dataline: clusterid, urlid, url, destUrl_1, destUrlLinktype_1, destUrl_2, ...
 *
 * @author hoshun
 */
public class Cluster {

    public Cluster(String dataline) {

        adjlist = new TreeMap<Integer, Set<DstNode>>();
        srcIdToUrl = new TreeMap<Integer, String>();

        String[] tokens = dataline.split("\t");
        clusterId = Integer.parseInt(tokens[0]);
        this._putNode(tokens);

    }

    public Cluster() {
        adjlist = new TreeMap<Integer, Set<DstNode>>();
        srcIdToUrl = new TreeMap<Integer, String>();
    }

    /**
     * Produce sub cluster consists of only given vertices. 
     * (to reuse print column format method)
     *
     * @param vertices
     */
    public Cluster makeSubcluster(int newClusterId, Set<Integer> vertices) {
        Cluster subcluster = new Cluster();
        
        subcluster.clusterId = newClusterId;
        for(int id : vertices){
            subcluster.adjlist.put(  id, this.adjlist.get(id) );
            subcluster.srcIdToUrl.put( id, this.srcIdToUrl.get(id) );
        }
        
        return subcluster;
    }
    
    /**
     * Revised the pattern of url [TOpic1:Topic2]url
     * @param tokens 
     */
    public void prefixCategoryNameToUrls(UrlToSpecifiedCategory urlToCategory){
        for(Map.Entry<Integer,String> en : this.srcIdToUrl.entrySet()){
            int id = en.getKey();
            if(urlToCategory.hasId(id)){
                Category cat = urlToCategory.getCategory(id);
                String catPattern = "[" + cat.first + ":" + cat.second + "]";
                srcIdToUrl.put(id, catPattern + en.getValue());
            }
        }
    }

    // put 
    private void _putNode(String[] tokens) {
        Set<DstNode> memberSet = new TreeSet<DstNode>();
        int srcId = Integer.parseInt(tokens[1]);

        String url = tokens[2];
        srcIdToUrl.put(srcId, url);

        for (int i = 3; i < tokens.length; i = i + 2) {
            int dstId = Integer.parseInt(tokens[i]);
            String linktype = tokens[i + 1];
            memberSet.add(new DstNode(dstId, linktype));

        }
        adjlist.put(srcId, memberSet);
    }

    public String fillCluster(Scanner in) throws IOException {
        boolean hitNextCluster = false;
        String line = "";
        while (in.hasNextLine() && !hitNextCluster) {
            line = in.nextLine();
            String[] tokens = line.split("\t");
            int oClusterId = Integer.parseInt(tokens[0]);
            if (clusterId != oClusterId) {
                hitNextCluster = true;
            } else {
                this._putNode(tokens);
            }
        }

        if (hitNextCluster) {
            return line;
        } else {
            return null;
        }
    }

    public int numOfMember() {
        return adjlist.keySet().size();
    }

    @Deprecated
    public boolean hasMemberInSpecifiedTopics(Set<String> topics, CategorizedURLs urlToCategory) {
        boolean hasMember = false;
        for (int memid : adjlist.keySet()) {
            Category cat = urlToCategory.getCategory(memid);
            if (cat != null && topics.contains(cat.first)) {
                hasMember = true;
                break;
            }
        }
        return hasMember;
    }

        
    public void printColumnFormat(PrintWriter out) {

        for (Map.Entry<Integer, Set<DstNode>> en : adjlist.entrySet()) {
            int srcId = en.getKey();
            String url = srcIdToUrl.get(srcId);
            out.printf("%s\t%s\t%s", clusterId, srcId, url);
            for (DstNode dstNode : en.getValue()) {
                out.printf("\t%s\t%s", dstNode.memid, dstNode.linktype);
            }
            out.printf("\n");
        }

    }

    /**
     * Convert cluster to graph. Only the intra-cluster links are maintained.
     *
     * @param cluster
     * @return
     */
    public DirectedGraph<Integer, Integer> toDirectedInnerGraph() {

        DirectedGraph<Integer, Integer> graph = new DirectedSparseGraph<Integer, Integer>();

        Set<Integer> memberSet = adjlist.keySet();
        int edgeCount = 0;
        for (Map.Entry<Integer, Set<DstNode>> adjrow : adjlist.entrySet()) {
            int srcId = adjrow.getKey();
            Set<DstNode> dstNodes = adjrow.getValue();
            for (DstNode dstNode : dstNodes) {
                int dstId = dstNode.memid;
                if (memberSet.contains(dstId) && srcId != dstId) {
                    graph.addEdge(edgeCount++, srcId, dstId);
                }
            }
        }

        return graph;
    }
    
    int clusterId;
    private Map<Integer, Set<DstNode>> adjlist;
    private Map<Integer, String> srcIdToUrl;
}

class DstNode implements Comparable<DstNode> {

    public DstNode(int nodeid, String linktype) {
        memid = nodeid;
        this.linktype = linktype;
    }

    @Override
    public int compareTo(DstNode o) {
        return ((Integer) memid).compareTo(o.memid);
    }
    public int memid;
    public String linktype;
}