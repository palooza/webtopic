/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package topicmodel;

import java.util.*;
import java.io.*;
import util.UrlToSpecifiedCategory;

/**
 *
 * @author hoshun
 */
public class ProduceDocumentSet {

    public static void main(String[] args) {
        File extendClusterUrlInfile = new File("/home/hoshun/webtopic/extend-clusterid-url-table.hdfs.dat");
        File extendPageContentInfile = new File("/home/hoshun/webtopic/extend-page-content.dat");
        File basicDocOutfile = new File("/home/hoshun/webtopic/basic-specify-doc-file");
        basicProducer(extendClusterUrlInfile, extendPageContentInfile, basicDocOutfile);
    }

    /**
     * Produce document from step=0 pages. (initial member)
     *
     * @param extendClusterUrlInfile
     * @param extendPageContentInfile
     * @param basicDocOutfile
     */
    public static void basicProducer(File extendClusterUrlInfile, File extendPageContentInfile, File basicDocOutfile) {

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(extendClusterUrlInfile)));
                out = new PrintWriter(basicDocOutfile);

                Map<Integer, String> urlidToPageContent = urlidToPageContent(extendPageContentInfile);

                String line = null;
                if (in.hasNextLine()) {
                    line = in.nextLine();
                }

                while (line != null) {
                    Document doc = new Document(line, urlidToPageContent);
                    line = doc.fillDocument(in);
                    
                    // specify
                    if (!doc.isFilter()) {
                        doc.printDocumentLine(out);
                    }
                    
                }

            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // notice the pagecontent data sperated by space!
    public static Map<Integer, String> urlidToPageContent(File extendPageContentInfile) {
        Map<Integer, String> urlidToPageContent = new TreeMap<Integer, String>();

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(extendPageContentInfile)));

                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] tokens = line.split(" ");
                    int urlid = Integer.parseInt(tokens[1]);
                    if (!urlidToPageContent.containsKey(urlid)) {
                        String[] words = Arrays.copyOfRange(tokens, 2, tokens.length);
                        String content = tokensToString(words, 3, " ");
                        if (!content.isEmpty()) {
                            urlidToPageContent.put(urlid, content);
                        }
                    }
                }
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded urlToPagecontent");
        return urlidToPageContent;
    }

    public static String tokensToString(String[] tokens, int minWordlength, String sep) {
        StringBuilder sb = new StringBuilder();
        if (tokens.length == 0) {
            return "";
        }

        int iter = 0;
        while (iter < tokens.length && tokens[iter].length() < minWordlength) {
            iter++;
        }
        
        if(iter == tokens.length){
            return "";
        }
        
        sb.append(tokens[iter]);
        for (int i = iter + 1; i < tokens.length; i++) {
            if (tokens[i].length() > minWordlength) {
                sb.append(sep);
                sb.append(tokens[i]);
            }
        }

        return sb.toString();
    }
}
