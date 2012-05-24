/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author hoshun
 */
public class WNode implements Comparable<WNode>{
    
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

    @Override
    public boolean equals(Object obj) {
        return this.id == ((WNode) obj).id;
    }

    @Override
    public int compareTo(WNode o) {
        return ((Integer) this.id).compareTo(o.id);
    }
        
    
    public int id;
    public String url;
}
