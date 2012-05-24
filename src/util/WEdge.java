/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.*;
import java.io.*;

/**
 *
 * @author hoshun
 */
public class WEdge implements Comparable<WEdge>{
    
    public static void main(String[] args) {
       
        WEdge e1 = new WEdge("");
        WEdge e2 = new WEdge("");
        
        System.out.println(e1);
        System.out.println(e2);
        
    }
    
    public WEdge(String linktype){
        this.id = idCounter++;
        this.linktype = linktype;
    }

    @Override
    public String toString() {
        return "E:" + id ;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((WEdge) obj).id;
    }

    @Override
    public int compareTo(WEdge o) {
        return ((Integer) this.id).compareTo(o.id);
    }
    
    public double getWeight() throws Exception{
        if(linktypeToWeight == null)
            throw new Exception("The linktype to weight relation is not set");
        
        return linktypeToWeight.get(linktype);
    }
    
    public static void setLinktypeToWeightRelation(Map<String,Double> map){
        linktypeToWeight = map;
    }
    
    
    public int id;
    public String linktype;
    private static Map<String,Double> linktypeToWeight;
    private static int idCounter = 0;
}
