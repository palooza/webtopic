/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.util;

import dmoz.topicmodel.*;
import java.util.*;

/**
 *
 * @author hoshun
 */
public class Filter {
    
    public Filter(){
        filteredCategory = new HashSet<Category>();
    }
    
    public Filter(ArrayList<Category> arr){
        filteredCategory = new HashSet<Category>(arr);
    }
    
    Set<Category> filteredCategory;
}
