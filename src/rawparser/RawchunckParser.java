/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rawparser;

import java.io.*;
import java.util.*;
/**
 * The raw chunck parser take adjacency link files downloaded from the Standford 
 * WebBase as input. The output is the file contains filtered adjacency link list.
 * The criteria is to delete 1. structured urls 
 *                           2. not valid urls
 *                           3. urls with too many adjacency links
 * @author hoshun
 */
public class RawchunckParser {
    
    public static String breakSymbol = "==P=>>>>=i===<<<<=T===>=A===<=!Junghoo!==>";
    
    public static void main(String[] args) {
        File input = new File(args[0]);
        File output = new File(args[1]);
        //File input = new File("/home/hoshun/webtopic/sample-1");
        //File output = new File("/home/hoshun/webtopic/adjlist");
        parseRawchunck(input, output);
        
    }
    
    public static void parseRawchunck(File input, File output){
        
        try {
            Scanner in = null;
            PrintWriter out = null;
            try{
                in = new Scanner(new BufferedReader(new FileReader(input)));
                out = new PrintWriter(output);
                assert(in != null);
                assert(out != null);
                
                
                String nextSiteFirstPage = null;
                // get the first starting page of the site
                boolean isGet = false;
                while(in.hasNextLine() && !isGet){
                    String line = in.nextLine();                 
                    if(line.equals(breakSymbol)){
                        if( (line = in.nextLine()) != null){
                            if(line.regionMatches(true,0,"URL: http://",0,12)){
                                nextSiteFirstPage = line.substring(12);
                                isGet = true;
                            }else{
                                throw new Exception("after breaking symbol, no valid source url.\n"
                                        + "the line is " + line);
                            }
                        }
                    }
                }
                // while not reaching the end of page
                while(nextSiteFirstPage != null){
                    Site site = new Site(nextSiteFirstPage);
                    nextSiteFirstPage = site.fillSite(in, nextSiteFirstPage);
                    site.writeAdjlist(out);
                }
                
            }finally{
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
