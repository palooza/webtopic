/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import dmoz.util.Category;
import java.util.*;
import java.io.*;

import util.CategorizedURLs;

/**
 *
 * dataline: clusterid, urlid, url, destUrl_1, destUrlLinktype_1, destUrl_2, ... 
 * @author hoshun
 */
public class Cluster {
    
    public Cluster(String dataline){

        adjlist = new TreeMap<Integer,Set<DstNode>>();
        srcIdToUrl = new TreeMap<Integer,String>();
        
        String [] tokens = dataline.split("\t");
        clusterId = Integer.parseInt(tokens[0]);
        this._putNode(tokens);
        
    }
    
    private void _putNode(String [] tokens){
        Set<DstNode> member = new TreeSet<DstNode>();
        int srcId = Integer.parseInt(tokens[1]);
     
        String url = tokens[2];
        srcIdToUrl.put(srcId,url);
                
        for(int i=3; i < tokens.length; i=i+2){
            int dstId = Integer.parseInt(tokens[i]);
            String linktype = tokens[i+1];
            member.add(new DstNode(dstId, linktype));
            
        }
        adjlist.put(srcId, member);
    }
    
    public String fillCluster(Scanner in) throws IOException{
        boolean hitNextCluster = false;
        String line = "";
        while(in.hasNextLine() && !hitNextCluster){
            line = in.nextLine();
            String [] tokens = line.split("\t");
            int oClusterId = Integer.parseInt(tokens[0]);
            if(clusterId != oClusterId){
                hitNextCluster = true;
            }else{
                this._putNode(tokens);
            }
        }               
        
        if(hitNextCluster){
            return line;
        }else{
            return null;
        }
    }
    
    
    public int numOfMember(){
        return adjlist.keySet().size();
    }
    
    public boolean hasMemberInSpecifiedTopics(Set<String> topics, CategorizedURLs urlToCategory){
        boolean hasMember = false;
        for(int memid : adjlist.keySet()){
            Category cat = urlToCategory.getCategory(memid);
            if(topics.contains(cat.first)){
                hasMember = true;
                break;
            }
        }
        return hasMember;
    }
    
    public void printColumnFormat(PrintWriter out){
        
        for(Map.Entry<Integer,Set<DstNode>> en : adjlist.entrySet()){
            int srcId = en.getKey();
            String url = srcIdToUrl.get(srcId);
            out.printf("%s\t%s\t%s", clusterId, srcId, url);
            for(DstNode dstNode : en.getValue()){
                out.printf("\t%s\t%s", dstNode.memid, dstNode.linktype);
            }
            out.printf("\n");
        }
        
    }
    
    int clusterId;
    private Map<Integer, Set<DstNode>> adjlist;
    private Map<Integer, String> srcIdToUrl;
         
    
}

class DstNode{
    
    public DstNode(int nodeid, String linktype ){
        memid = nodeid;
        this.linktype = linktype;
    }
    
    public int memid;
    public String linktype;
    
}