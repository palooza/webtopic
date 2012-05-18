/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rawparser;

import java.util.*;
import java.io.*;
import rawparser.UrlFilter;
/**
 *
 * @author hoshun
 */
public class FilterUniqueURLlist {
    
    public static void main(String[] args) {
              
        Scanner in = null;
        PrintWriter out = null;
        int size = 32883319;
        try{
            try{
                in = new Scanner(new BufferedReader(new FileReader("/media/netdisk/hoshun/webtopic/wb3/01-2011-all-connected-sorted-uniq-url")));
                out = new PrintWriter("/media/netdisk/hoshun/webtopic/wb3/01-2011-all-trimmed-sorted-uniq-url");
                
                //String [] arr = new String[size];
                int slen = 0;
                int ind = 0;
                while(in.hasNextLine()){
                    String url  = in.nextLine().trim();
                    
                    boolean filtered = false;
                    filtered = filtered || 
                            UrlFilter.isFilteredByLength(url,512) ||
                            UrlFilter.isFilteredBySite(url);
                    
                    if(!filtered)
                        out.println(url);
                    
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
