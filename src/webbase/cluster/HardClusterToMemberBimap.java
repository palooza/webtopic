/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import java.io.*;
import java.util.*;

/**
 * Assume the cluster ids and member(URL) ids are continously ranging from 0 
 * and have one-to-one relationship.
 * @author hoshun
 */
public class HardClusterToMemberBimap {

    public HardClusterToMemberBimap(File clusterToMembersFile) {
        
        clusterToMember = new ArrayList<Set<Integer>>();
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(clusterToMembersFile)));

                // create clusters to members one-to-many mapping.
                while(in.hasNextLine()){
                    String [] tokens = in.nextLine().split("\t");
                    int clid = Integer.parseInt(tokens[0]);
                    
                    Set<Integer> tempMember = new TreeSet<Integer>();
                    for(int i=1; i<tokens.length;i++){
                        Integer memberId = Integer.parseInt(tokens[i]);
                        tempMember.add( memberId );
                        maxUrlId = (maxUrlId > memberId) ? maxUrlId : memberId;
                    }
                    
                    if( (clusterToMember.size() - 1) < clid){
                        for(int igap=clusterToMember.size(); igap <= clid; igap++ ){
                            clusterToMember.add(new TreeSet<Integer>());
                        }
                        clusterToMember.get(clid).addAll(tempMember);
                    }else{
                        clusterToMember.get(clid).addAll(tempMember);
                    }                    
                }
                
                
                // Create members to clusters many-to-one mapping.
                memberToClusterArr = new int [maxUrlId+1];
                for(int i=0; i< memberToClusterArr.length;i++){
                    memberToClusterArr[i] = -1;
                }
                
                for(int iCluster=0; iCluster < clusterToMember.size(); iCluster++){
                    for(int memid : clusterToMember.get(iCluster)){
                        memberToClusterArr[memid] = iCluster;
                    }
                }
                
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    
    public int getClusterId(int urlId){
        return memberToClusterArr[urlId];
    }
    
    public Set<Integer> getMembers(int clusterId){
        return clusterToMember.get(clusterId);
    }
    
    private int maxUrlId;
    private ArrayList<Set<Integer>> clusterToMember;
    private int [] memberToClusterArr;
}
