/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqldat;

import java.util.*;
import java.util.zip.CRC32;
import java.util.regex.*;
/**
 *
 * @author hoshun
 */
public class Hashtest {
    
    public static void main(String[] args) {
        CRC32 crc = new CRC32();
        String url = "0.0.0.0/";
        byte [] b = url.getBytes();
        crc.update(b);
        System.out.println( crc.getValue() );
        
        String a = "dire/01-2011-sorted-0";
        System.out.println( a.replaceAll(".*01-2011-.*-","") );
        
        System.out.println("\"" + "\"");
    }
    
    
    
}
