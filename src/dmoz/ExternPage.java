/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz;

import dmoz.util.Category;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import lucene.TokenizeUtil;
import lucene.WordBag;

import org.apache.lucene.analysis.Analyzer;

/**
 *
 * @author hoshun
 */
public class ExternPage {

    public ExternPage(String url) {
        this.URL = url;
    }

    public void fillExternPage(Scanner in) {

        try {
            boolean found = false;
            while (in.hasNextLine() && !found) {
                String line = in.nextLine();
                title = ContentFormat.getTitle(line);
                if (!title.isEmpty()) {
                    found = true;
                } else if (ContentFormat.isExternPageEnd(line)) {
                    throw new Exception("missing title");
                }
            }

            found = false;
            while (in.hasNextLine() && !found) {
                String line = in.nextLine();
                description = ContentFormat.getDescription(line);
                if (!description.isEmpty()) {
                    found = true;
                } else if (ContentFormat.isExternPageEnd(line)) {
                    throw new Exception("missing description");
                }
            }

            found = false;
            while (in.hasNextLine() && !found) {
                String line = in.nextLine();
                topic = ContentFormat.getTopic(line);
                if (!topic.isEmpty()) {
                    found = true;
                } else if (ContentFormat.isExternPageEnd(line)) {
                    throw new Exception("missing topic");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // do not print this extern page
            topic = "";
            description = "";
            title = "";
        }

    }
// topic1 topic2 word1 c1 word2 c2 ....

    public void printUnnamedWordVectorColumnFormat(PrintWriter out) throws IOException {

        ArrayList<String> topics = ContentFormat.getFirstTwoHeirachy(topic);
        if (topics.size() < 2) {
            return;
        }

        if (topics.get(0).equals("World")) {
            return;
        }

        Analyzer analyzer = TokenizeUtil.getMyAnalyzer();
        WordBag wordbag = new WordBag();

        ArrayList<String> rsTitle = TokenizeUtil.tokenizeString(analyzer, title);
        wordbag.addWords(rsTitle, 1);
        ArrayList<String> rsDescrption = TokenizeUtil.tokenizeString(analyzer, description);
        wordbag.addWords(rsDescrption, 1);

        out.printf("%s\t%s", topics.get(0), topics.get(1));
        for (Map.Entry<String, Integer> en : wordbag.bag.entrySet()) {
            String word = en.getKey();
            int count = en.getValue();
            out.printf("\t%s\t%d", word, count);
        }
        out.printf("\n");

    }

    public void printURLwithinACategory(PrintWriter out, Category category) throws IOException {
        ArrayList<String> topics = ContentFormat.getFirstTwoHeirachy(topic);
        if (topics.size() < 2) {
            return;
        }

        if (topics.get(0).equals("World") || topics.get(0).equals("Regional")) {
            return;
        }
        

        /**
         * Check whether the url is in the cateogry of interest, if yes, print it.
         */
        if (category.first.equals("*") && category.second.equals("*")) {
            // pass all
        } else {
            if (!topics.get(0).equals(category.first)) {
                return;
            } else {
                if (!category.second.equals("*") && !topics.get(1).equals(category.second)) {
                    return;
                }
            }
        }
        
        
        out.printf("%s\t%s\t%s\n", URL.trim().toLowerCase(), topics.get(0), topics.get(1));
    }
    public String URL;
    public String title;
    public String description;
    public String topic;
}
