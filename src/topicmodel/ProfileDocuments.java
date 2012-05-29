/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicmodel;

import java.util.*;
import java.io.*;

import dmoz.util.Category;
/**
 *
 * @author hoshun
 */
public class ProfileDocuments {
    
    public static void main(String[] args) {
        simpleProfile(new File("/home/hoshun/webtopic/basic-filtered-doc-file"), 
                new File("/home/hoshun/webtopic/profile-filtered-basic-doc") );
    }

    public static void simpleProfile(File infile, File outfile) {
        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                Map<Category,Integer> allCatToCount = new TreeMap<Category,Integer>();
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    String [] tokens = line.split(" ");
                    Map<Category,Integer> catToCount = getCategory(tokens[0]);
                    
                    combineMap(allCatToCount, catToCount);                
                    int numOfWord = tokens.length - 1;
                    
                    out.println(countNumberOfTopic1(catToCount) +"\t" +
                            countNumberOfTopic2(catToCount) + "\t" +
                            numOfWord);
                    
                }
                
                for(Map.Entry<Category,Integer> en : allCatToCount.entrySet()){
                    Category cat = en.getKey();
                    int count = en.getValue();
                    System.out.println(cat.first+"\t"+cat.second+"\t"+count);
                }
                
                
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private static int countNumberOfTopic1(Map<Category,Integer> map){
        Set<Category> set = new TreeSet<Category>();
        for(Category c : map.keySet()){
            set.add(c);
        }
        return set.size();
    }
    private static int countNumberOfTopic2(Map<Category,Integer> map){
            return map.size();
    }

    
    private static void combineMap(Map<Category,Integer> map1, Map<Category,Integer> map2){
        for(Map.Entry<Category,Integer> en : map2.entrySet()){
            Category cat = en.getKey();
            int count = en.getValue();
            if(map1.containsKey(cat)){
                map1.put(cat, map1.get(cat) + count);
            }else{
                map1.put(cat,count);
            }
        }
    }
    
    public static Map<Category,Integer> getCategory(String docName){
        Map<Category,Integer> categoryToCount = new TreeMap<Category,Integer>();
             
        String [] catStrings = docName.split("-");
        for(int i=1; i<catStrings.length; i++){
            String catString = catStrings[i];
            String [] topics = catString.split(":");
            Category c = new Category(topics[0], topics[1]);
            if(categoryToCount.containsKey(c)){
                categoryToCount.put(c, categoryToCount.get(c) + 1);
            }else{
                categoryToCount.put(c, 1);
            }
        }                        
        
        return categoryToCount;
    }



}