/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.*;
import java.io.*;        
        
import dmoz.util.Category;
/**
 *
 * @author hoshun
 */
public class UrlToSpecifiedCategory {
    
    public static void main(String[] args) {
        makeCode();
    }
    
    @Deprecated
    public static void makeCode(){
        File infile = new File ("/home/hoshun/dmoz/dmoz-indexed-url");
        File outfile = new File ("/home/hoshun/dmoz/generateUrlToSpecifiedCategory");
        File outformatfile = new File("/home/hoshun/dmoz/dmoz-specified-url");
        
        CategorizedURLs urlToCategory = new CategorizedURLs(infile, true);
        SpecifiedTopics specifiedTopics = new SpecifiedTopics();        
        try{
            PrintWriter out = new PrintWriter(outfile);
            PrintWriter out2 = new PrintWriter(outformatfile);
            
            Map<Integer, Category> idToCategory = urlToCategory.idToCategory;
            Map<Integer, String> idToUrl = urlToCategory.idToUrl;
            
            for(int id : idToCategory.keySet()){
                Category cat = idToCategory.get(id);
                if(specifiedTopics.containsCategory(cat)){
                    String url = idToUrl.get(id);
                    
                    out.printf("idToCategory.put(%s,new Category(\"%s\",\"%s\"));\n", id, cat.first,cat.second);
                    out.printf("idToURL.put(%s,\"%s\");\n",id, url);
                    
                    out2.printf("%s\t%s\t%s\t%s\n", id, cat.first,cat.second,url);
                }
            }
            
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private Map<Integer,Category> idToCategory;
    private Map<Integer,String> idToURL;
    
    public boolean hasId(int id){
        return idToCategory.containsKey(id);
    }
    
    public String getURL(int id){
        return idToURL.get(id);
    }
    
    public Category getCategory(int id){
        return idToCategory.get(id);
    }
    
    public UrlToSpecifiedCategory(File dmozSpecifiedUrlfile, boolean isWithUrl){
        idToCategory = new TreeMap<Integer,Category>();
        idToURL = new TreeMap<Integer,String>();

        CategorizedURLs temp = new CategorizedURLs(dmozSpecifiedUrlfile, isWithUrl);
        
        if(isWithUrl){
            idToURL = temp.idToUrl;
        }
        idToCategory = temp.idToCategory;
    }
    
}
