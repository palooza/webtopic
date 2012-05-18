/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;
import java.util.*;
/**
 *
 * @author hoshun
 */
public class Url {

    public static void main(String[] args) {
        String sURL = "www.abc.com/abc/Lookup";
        String tURL = "www.abc.com/abc/Lookup/ho";
        
        System.out.println( getLinktype(sURL, tURL) );
        
        String s = "<p>ok</p> <body> mehere </body"
                + ">";
        System.out.println(s.replaceAll("\\<.*?>",""));
        
        
    }

    
    public static String getSiteName(String url) {
        assert (url != null);

        // avoid http:// and http://* 
        if (url.startsWith("http:")) {
            url = url.replaceFirst("http:/*", "");
        }

        boolean isSep = false;
        int iter = 0;
        for (iter = 0; iter < url.length(); iter++) {
            if (url.charAt(iter) == '/') {
                isSep = true;
                break;
            }
        }
        if (isSep) {
            return url.substring(0, iter);
        }

        return url;
    }

    /**
     * Classify the relation to: 
     * 1. intra-domain directed up link DU. 
     * 2. intra-domain directed down link DD. 
     * 3. intra-domain link ID. 
     * 4. inter-domain link OL.
     *
     * @param sURL
     * @param tURL
     * @return
     */
    public static String getLinktype(String sURL, String tURL) {
        String type = "";

        sURL = sURL.toLowerCase();
        tURL = tURL.toLowerCase();

        String sSite = getSiteName(sURL);
        String tSite = getSiteName(tURL);

        // inter-domain
        if (sSite.equals(tSite)) {

            if (sURL.contains(tURL)) {
                type = "DU";
            } else if (tURL.contains(sURL)) {
                type = "DD";
            } else {
                type = "IL";
            }

        } else {
            type = "OL";
        }

        return type;
    }
}
