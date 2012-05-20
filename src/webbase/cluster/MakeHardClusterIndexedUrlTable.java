/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import java.util.*;
import java.io.*;
/**
 *
 * @author hoshun
 */
public class MakeHardClusterIndexedUrlTable {
    
    public static void main(String[] args) {
        String dir = "media/netdisk/hoshun/webtopic/wb3/hdfsdat/";
        File urlTableInfile = new File(dir + "url-table.hdfs.dat");
        File clusterTableInfile = new File(dir + "cluster-table.hdfs.dat");
        File urlAndClusterTableOutFile = new File(dir + "helper/initclusterId-url-table.dat");
        makeHardClusterIndexedUrlTable(urlTableInfile, clusterTableInfile, urlAndClusterTableOutFile);
    }
    
    
    public static void makeHardClusterIndexedUrlTable(File urlTableInfile, File clusterTableInfile, 
            File urlAndClusterTableOutfile){
        
        try{
            Scanner in = null;
            PrintWriter out = null;
         
            try{
                in = new Scanner(new BufferedReader(new FileReader(urlTableInfile)));
                out = new PrintWriter(urlAndClusterTableOutfile);
                
                HardClusterToMemberBimap clusterToMember = new HardClusterToMemberBimap(clusterTableInfile);
                
                while(in.hasNextLine()){
                    String line = in.nextLine();
                    String [] tokens = line.split("\t");
                    int urlid = Integer.parseInt(tokens[0]);
                    int clusterid = clusterToMember.getClusterId(urlid);
                    out.printf("%s\t",clusterid);
                    out.printf("%s", line);
                    out.printf("\n");
                }
                
            }finally{
                in.close();
                out.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
