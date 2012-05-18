/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rawparser;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

import util.Url;

/**
 *
 * @author hoshun
 */
public class Site {
 
    Site(String url){
        siteName = util.Url.getSiteName(url);
        pagelist = new ArrayList<SourcePage>();
        urlToCount = new HashMap<String,Integer>();
    }
    
    public String fillSite(Scanner in, String firstSourceUrl) throws IOException, Exception{
        assert(firstSourceUrl != null);       
        String nextSourceUrl = firstSourceUrl;
        
        // while next source url is also in the site and not reaching the end of file
        while(nextSourceUrl != null && Url.getSiteName(nextSourceUrl).equals(siteName)){
            SourcePage page = new SourcePage(nextSourceUrl);
            nextSourceUrl = page.fillSourcePage(in);
            
            //filter page with too many out-links
            if( page.neiCount() > 0 && page.neiCount() <= 85){
                this.addPage( page );
            }
            
        }
        
        return nextSourceUrl;
    }
    
    public void addPage(SourcePage page){
        
        pagelist.add(page);
        
        for(String adjlink : page.adjList){
            if(urlToCount.containsKey(adjlink)){
                urlToCount.put(adjlink, 
                        urlToCount.get(adjlink) + 1);
            }else{
                urlToCount.put(adjlink, 1);
            }
        }
    }
    
    /**
     * Write the pages of a site and their adjacency links to the new adjacency list. 
     * 
     * We should decide, on a site level, that which adjacency links are redundant. 
     * We want to remove the structural links by examining the UrlToCount map. 
     * If an adjacency link appears too often in a site, it could be a structural 
     * links and does not bear semantic meanings. 
     * 
     * @param output file handler.
     * 
     */
    public void writeAdjlist(PrintWriter out){
        
        int occurThreshold = adjlinkOccurThreshold();

        for(SourcePage page : pagelist){
            out.println(RawchunckParser.breakSymbol);
            out.println(page.sourceUrl);
            
            for(String adjlink : page.adjList){
                if(urlToCount.get(adjlink) <= occurThreshold){
                    out.println(adjlink);
                }
            }
            
        }
        
    }
    
    private int adjlinkOccurThreshold(){
        int pageCount = pageCount();
        int th = Integer.MAX_VALUE;
        if( pageCount >= 1000){
            th = 500;
        }else if(pageCount >= 100){
            th = (int) Math.ceil(pageCount * 0.5);
        }else{
            th = (int) Math.ceil(pageCount * 0.9);
        }
        return th;
    }
    
    public int pageCount(){
        return pagelist.size();
    }
    
    public String siteName; 
    public Map<String, Integer> urlToCount;
    public ArrayList<SourcePage> pagelist; 
    
}
