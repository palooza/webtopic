/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rawparser;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author hoshun
 */
public class SourcePage {

    public static void main(String[] args) {
        String url = "www.donga.com/e-county/sssboard/board.php?tcode=90018&s_wor";
        System.out.println(url.regionMatches(true, 0, "xhttp://", 1, 7));
        boolean filtered = false;
        filtered = UrlFilter.isFilteredByLength(url, 511)
                || UrlFilter.isFilteredByFormat(url)
                || UrlFilter.isFilteredBySyntax(url);
        System.out.println( filtered );
                

    }

    SourcePage(String url) {
        sourceUrl = url;
        adjList = new ArrayList<String>();
    }

    /**
     * Fill the SourcePage object with all its adjacency links. The returned
     * source url is used by site to indicate whether the site is ended.
     *
     * @param the input file handler.
     * @return the next source url or null if reached the end of page.
     *
     * "http://" part of the url is removed. <em>only url with htm/html type is
     * kept</em>
     */
    public String fillSourcePage(Scanner in) throws IOException, Exception {
        String url = null;
        boolean nextpage = false;
        while (!nextpage && in.hasNextLine()) {
            String line = in.nextLine();

            // line is a adjacency link url and is html/htm file or a directory file.
            if (line.regionMatches(true, 0, "http://", 0, 7) && line.length() > 7
                    && (line.endsWith("htm") || line.endsWith("html") || line.endsWith("/"))) {

                url = line.substring(7);

                boolean filtered = false;
                filtered = UrlFilter.isFilteredByLength(url, 511)
                        || UrlFilter.isFilteredByFormat(url)
                        || UrlFilter.isFilteredBySyntax(url)
                        || url.startsWith(sourceUrl);  //not a direct up-link

                if (!filtered) {
                    adjList.add(url);
                }


                // line is a breaking symbol followed by a source page url
            } else if (line.equals(RawchunckParser.breakSymbol)) {
                line = in.nextLine();
                if (line.regionMatches(true, 0, "URL: http://", 0, 12) && line.length() > 12) {
                    // take substring after "URL: " 
                    url = line.substring(12);
                    nextpage = true;
                } else {
                    throw new Exception("No valid URL after breaking symbol. line is " + line);
                }
            }

        }
        // if reaching the end of the file 
        if (nextpage == false) {
            return null;
        }

        return url;
    }

    public int neiCount() {
        return adjList.size();
    }
    String sourceUrl;
    ArrayList<String> adjList;
}
