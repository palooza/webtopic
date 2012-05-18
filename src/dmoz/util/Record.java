/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.util;

import java.util.*;

/**
 *
 * @author hoshun
 */
public class Record implements Comparable{

    public Record(Category cat, double val){
        category = cat;
        value = val;
    }

    @Override
    public boolean equals(Object obj) {
        return this.value == ((Record) obj).value;
    }

    @Override
    public int compareTo(Object o) {
        Record other = (Record) o;
        if (this.value == other.value) {
            return 0;
        }
        return (this.value > other.value) ? 1 : -1;
    }

    @Override
    public String toString() {
        return category.toString() + "\t" + value;
    }
    
    
    
    public Category category;
    public double value;
}
