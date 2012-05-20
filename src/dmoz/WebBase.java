/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz;

import java.util.*;
import java.io.*;

import util.UrltoIndexBimap;
import dmoz.util.Category;

/**
 *
 * @author hoshun
 */
public class WebBase {

    public static void main(String[] args) {
        File webbaseUrlInfile = new File("/media/netdisk/hoshun/webtopic/wb3/01-2011-all-connected-sorted-uniq-url");
        File dmozIndexedOutfile = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/dmoz-indexed-url");
        File dmozUrlCategoryInfile = new File("/media/netdisk/hoshun/webtopic/dmoz/dmoz-url-all-sorted-list");
        indexDmozURLwithWebBaseId(dmozUrlCategoryInfile, dmozIndexedOutfile, webbaseUrlInfile);
    }

    public static void indexDmozURLwithWebBaseId(
            File dmozUrlCategoryInfile,
            File dmozIndexedOurfile,
            File webbaseUrlInfile) {
        Map<String, Integer> categoryCount = new TreeMap<String, Integer>();
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                
                in = new Scanner(new BufferedReader(new FileReader(dmozUrlCategoryInfile)));
                out = new PrintWriter(dmozIndexedOurfile);

                UrltoIndexBimap WBUrlToIndex = new UrltoIndexBimap(webbaseUrlInfile);
                
                while(in.hasNextLine()){
                    String [] tokens = in.nextLine().split("\t");
                    String url = tokens[0];
                    Category category = new Category(tokens[1], tokens[2]);
                    
                    int id = WBUrlToIndex.getIndex(url.trim().toLowerCase());
                    if(id >= 0){
                        out.printf("%s\t%s\t%s\t%s\n",id, 
                                category.first, category.second,
                                url);
                        
                        if(categoryCount.containsKey(category.first)){
                            categoryCount.put(category.first, categoryCount.get(category.first)+1);
                        }else{
                            categoryCount.put(category.first,1);
                        }
                    }  
                }
                
                //print the stat of each category
                for(Map.Entry<String,Integer> en : categoryCount.entrySet()){
                    String topCategory = en.getKey();
                    int count = en.getValue();
                    System.out.println(topCategory + ":\t" + count);
                }

            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
