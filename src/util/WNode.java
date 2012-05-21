/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author hoshun
 */
public class WNode {
    
    public WNode(int id, String url){
        this.id = id;
        this.url = url;
    }
    
    public WNode(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "V:" + id;
    }
        
    public int id;
    public String url;
}
