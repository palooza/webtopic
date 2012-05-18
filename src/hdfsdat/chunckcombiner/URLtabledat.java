/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdfsdat.chunckcombiner;

import java.io.*;
import java.util.*;

import hdfsdat.luovendat.UrltoIndexBimap;
/*
 * Produce data file for hbase for loading. The format is: Rowid, info-family,
 * adj-family urlid(padded by 1,000,000,000) (Int), url:URL(String), adj-urlid
 * (Int),link type(String)
 *
 * Notice the URL is already trimmed and convert to lower cases.
 *
 * @author hoshun
 */

public class URLtabledat {

    public static void main(String[] args) {

        try {
            String baseChunckurl = "/media/netdisk/hoshun/webtopic/wb3/sortbychunck/01-2011-sorted-url-";
            String baseEdgelist = "/media/netdisk/hoshun/webtopic/wb3/chunckedgelist/01-2011-edgelist-";
            File univurl = new File("/media/netdisk/hoshun/webtopic/wb3/01-2011-all-connected-sorted-uniq-url");
            File urltabledat = new File("/media/netdisk/hoshun/webtopic/wb3/hbasedat/url-table.hbase.dat");

            PrintWriter urltableout = null;

            try {
                
                urltableout = new PrintWriter(urltabledat);
                
                UrltoIndexBimap univurlmap = new UrltoIndexBimap(univurl);
                
                for (int clabel = 0; clabel <= 18; clabel++) {
                    System.out.println("Now processing chunck " + clabel);
                    File chunckurl = new File(baseChunckurl + clabel);
                    File edgelist = new File(baseEdgelist + clabel);
                    makeURLTabledat(univurlmap, chunckurl, edgelist, urltableout);
                }
                
                                
            } finally {
                urltableout.close();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeURLTabledat(UrltoIndexBimap univurlmap, File chunckurl, File edgelist, PrintWriter urltableout) throws IOException {

        int padding = 0;
        UrltoIndexBimap chunckidmap = new UrltoIndexBimap(chunckurl);
        

        String chunck = chunckurl.toString().replaceAll(".*01-2011-chunck-", "");
        try {
            Scanner elin = null;
            try {
                elin = new Scanner(new BufferedReader(new FileReader(edgelist)));


                int cursid = -1;
                String curSURL = "";
                while (elin.hasNextLine()) {

                    String[] tokens = elin.nextLine().split("\t");
                    int sid = Integer.parseInt(tokens[0]);
                    int tid = Integer.parseInt(tokens[1]);

                    // this line is the start of a new source page
                    if (sid != cursid) {

                        String sURL = chunckidmap.getUrl(sid);
                        String tURL = chunckidmap.getUrl(tid);

                        int univSid = univurlmap.getIndex(sURL);
                        int univTid = univurlmap.getIndex(tURL);

                        String linktype = "";
                        if (univSid < 0) {
                            System.out.println(chunck + ":In univUrlmap: Cannot find " + sURL);
                        } else if (univTid < 0) {
                            System.out.println(chunck + ":In univUrlmap: Cannot find " + tURL);
                        } else {
                            linktype = util.Url.getLinktype(sURL, tURL);
                        }

                        int padUnivSid = padding + univSid;
                        int padUnivTid = padding + univTid;

                        // when line is valid
                        if (linktype.length() != 0) {
                            // not the first line
                            if (cursid != -1) {
                                urltableout.printf("\n");
                            }
                            urltableout.printf("%d\t%s\t%d\t%s", padUnivSid, sURL, padUnivTid, linktype);
                            cursid = sid;
                            curSURL = sURL;
                        }


                    } // this line is an edge belong to previous source page.
                    else {
                        String tURL = chunckidmap.getUrl(tid);
                        int univTid = univurlmap.getIndex(tURL);
                        String linktype = util.Url.getLinktype(curSURL, tURL);

                        int padUnivTid = padding + univTid;

                        urltableout.printf("\t%d\t%s", padUnivTid, linktype);
                    }

                }
                
                urltableout.printf("\n");
                
            } finally {
                elin.close();
            }
        } catch (Exception e) {
        }
    }
}
