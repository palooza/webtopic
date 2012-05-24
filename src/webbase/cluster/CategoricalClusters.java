/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webbase.cluster;

import java.util.*;
import java.io.*;

import dmoz.util.Category;
import util.CategorizedURLs;

/**
 *
 * @author hoshun
 */
public class CategoricalClusters {

    public static void main(String[] args) {
        File categorizedUrlInfile = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/dmoz-indexed-url");
        File clustertableInfile = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/cluster-table.hdfs.dat");
        
        String categoryName = "Society";
        File categoryProfileOutfile = new File("/media/netdisk/hoshun/webtopic/wb3/hdfsdat/profile/" + 
                "cluster-"+ categoryName);
        
        profileCategoryCluster(categoryName, clustertableInfile, categorizedUrlInfile, categoryProfileOutfile);
    }

    public static void profileCategoryCluster(
            String categoryName,
            File clustertableInfile,
            File categorizedUrlInfile,
            File categoryProfileOutfile) 
    {
        try {
            Scanner clusterin = null;
            PrintWriter out = null;
            try {
                clusterin = new Scanner(new BufferedReader(new FileReader(clustertableInfile)));
                out = new PrintWriter(categoryProfileOutfile);

                CategorizedURLs categorizedUrl = new CategorizedURLs(categorizedUrlInfile, false);
                
                while(clusterin.hasNextLine()){
                    String [] tokens = clusterin.nextLine().split("\t");
                    int clusterId = Integer.parseInt(tokens[0]);
                    int [] memberArr = new int [tokens.length-1];
                    for(int i=1; i<tokens.length; i++){
                        int memid = Integer.parseInt(tokens[i]);
                        memberArr[i-1] = memid;
                    }
                    
                    Map<Category,Integer> catToCount = categoryWiseURLCount(memberArr, categorizedUrl);
                    
                    // print cluster information only if the cluster contains the
                    // specified top category url. 
                    if(hasTopCategory(categoryName, catToCount)){
                        for(Map.Entry<Category,Integer> en : catToCount.entrySet()){
                            Category cat = en.getKey();
                            int count = en.getValue();
                            out.printf("%s\t%s\t", clusterId, memberArr.length);
                            out.printf("%s\t%s\t%s", cat.first, cat.second, count);
                            out.printf("\n");
                        }                        
                    }
                    
                }
                
                
            } finally {
                clusterin.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Map<Category,Integer> categoryWiseURLCount(int [] memberArr, CategorizedURLs categorizedUrl){
        Map<Category,Integer> catToCount = new TreeMap<Category,Integer>();
        
        for(int memid : memberArr){
            if(categorizedUrl.hasId(memid)){
                Category cat = categorizedUrl.getCategory(memid);
                if(catToCount.containsKey(cat)){
                    catToCount.put(cat, catToCount.get(cat) + 1);
                }else{
                    catToCount.put(cat,1);
                }
            }
        }
        
        return catToCount;
    }
    
    private static boolean hasTopCategory(String topCatName, Map<Category,Integer> catToCount){
        boolean hasTopic = false;
        for(Category cat : catToCount.keySet()){
            if(cat.first.equals(topCatName)){
                hasTopic = true;
                break;
            }
        }
        return hasTopic;
    }
    
}
