/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.util.*;
import java.io.*;
/**
 *
 * @author hoshun
 */
public class BufferTest {
    
    public static void main(String[] args) {
        try {
            PrintWriter out = new PrintWriter("/home/hoshun/lengthtest");
            
            
            for(int i=1; i<=100000; i++){
                out.print("a");
            }
            out.print("\n");
            out.println("here");
            out.close();
            
            Scanner in = new Scanner(new BufferedReader(new FileReader("/home/hoshun/lengthtest"),8092));
            String l1 = in.nextLine();
            String l2 = in.nextLine();
            System.out.println(l1.length());
            System.out.println(l2);
            
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
}
