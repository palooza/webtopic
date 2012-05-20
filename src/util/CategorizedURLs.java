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
public class CategorizedURLs {

    public CategorizedURLs(File indexedDmozUrl) {

        idToCategory = new HashMap<Integer, Category>();
        try {
            Scanner in = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(indexedDmozUrl)));
                while (in.hasNextLine()) {
                    String[] tokens = in.nextLine().split("\t");
                    int id = Integer.parseInt(tokens[0]);
                    Category category = new Category(tokens[1], tokens[2]);
                    idToCategory.put(id, category);
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the category associated with the id. 
     * @param id
     * @return associated category or null if the id is not in the open 
     * directory (dmoz).
     */
    public Category getCategory(int id) {
        if (idToCategory.containsKey(id)) {
            return idToCategory.get(id);
        }
        return null;
    }
    
    public boolean hasId(int id){
        return idToCategory.containsKey(id);
    }
    
    private Map<Integer, Category> idToCategory;
}
