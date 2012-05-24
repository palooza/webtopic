/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lucene;

import java.util.*;
import java.io.*;
import org.apache.lucene.analysis.Analyzer;
/**
 *
 * @author hoshun
 */
public class SiteStopwordList {
    
    public static void main(String[] args) {
        
        File sitelist = new File("/home/hoshun/webtopic/01-2011.sitelist.txt");
        File outfile = new File("/home/hoshun/webtopic/sitebased-stopword-list");
        
        try {
            Scanner in = new Scanner(new BufferedReader(new FileReader(sitelist)));
            PrintWriter out = new PrintWriter(outfile);
            
            StringBuilder sb = new StringBuilder();
            while(in.hasNextLine()){
                String [] tokens = in.nextLine().split("\t");
                
                if(tokens.length < 2)
                    continue;
                
                int numOfPage = Integer.parseInt(tokens[1]);
                if(numOfPage >= 10000){
                    String [] siteTokens = tokens[0].split("\\.");
                    for(String token : siteTokens){
                        if(token.length() > 3){
                            sb.append(" " + token);
                        }
                    }
                }
            }
            
            Analyzer analyzer = TokenizeUtil.getMyAnalyzer();
            ArrayList<String> stopwordarr = TokenizeUtil.tokenizeString(analyzer, sb.toString());
            
            Set<String> stopwordSet = new TreeSet<String>(stopwordarr);
            
            for(String word : stopwordSet){
                out.println( word );
            }
            
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
