/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz.util;

/**
 *
 * @author hoshun
 */
public class Category implements Comparable<Category> {

    public Category(String a, String b) {
        first = a;
        second = b;
    }

    @Override
    public int compareTo(Category o) {
        int b = this.first.compareTo(o.first);
        if (b == 0) {
            return this.second.compareTo(o.second);
        }
        return b;
    }

    @Override
    public boolean equals(Object obj) {
        Category other = (Category) obj;
        return this.first.equals(other.first) && this.second.equals(other.second);
    }

    @Override
    public String toString() {
        return first + "\t" + second;
    }
    public String first;
    public String second;
}