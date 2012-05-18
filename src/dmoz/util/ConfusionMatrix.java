/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.util;

import java.util.*;
import java.io.*;
/**
 *
 * @author hoshun
 */
public class ConfusionMatrix {
    
    public ConfusionMatrix(Collection<Category> category){
        
        this.categoryArr = new ArrayList(category);
        Collections.sort(this.categoryArr);
        confMatrix = new int [this.categoryArr.size()][this.categoryArr.size()];
        
    }
        
    public boolean add(Category real, Category label){
        int rid = _getIndex(real);
        int lid = _getIndex(label);
        if(rid < 0 || lid < 0){
            return false;
        }
            
        confMatrix[rid][lid] =  confMatrix[rid][lid] + 1;
        
        return true;
    }
    
    public void print(File outfile){
        PrintWriter out = null;
        try{
            out = new PrintWriter(outfile);
            try{
                for(int i=0; i < categoryArr.size(); i++){
                    out.println(i + "\t" + categoryArr.get(i).toString());
                }
                
                for(int row=0; row < categoryArr.size(); row++){
                    for(int col=0; col < categoryArr.size(); col++){
                        out.printf("R"+row+"C"+col+":"+confMatrix[row][col]+"\t");
                    }
                    out.printf("\n");
                }
                
            }finally{
                out.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    private int _getIndex(Category cat){
        return Collections.binarySearch(categoryArr,cat);
    }
    
    ArrayList<Category> categoryArr;
    int [][] confMatrix;
    
}
