/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz;

import dmoz.util.Category;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hoshun
 */
public class ParseContent {

    public static void main(String[] args) {

        File infile = new File("/home/hoshun/dmoz/rawdata/xcl.content.rdf.u8");
        File wordvectoroutfile = new File("/home/hoshun/dmoz-parsed-content");
        //parseContentToColumnFormat(infile, wordvectoroutfile);
        String catname = "*";
        File urlcatfile = new File("/home/hoshun/dmoz/url-" + "all" + "-list");
        Category within = new Category(catname,"*");
        parseContentToURLandCategory(infile, urlcatfile, within);

    }

    public static void parseContentToColumnFormat(File infile, File outfile) {

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (ContentFormat.isExternPageStart(line)) {
                        String url = ContentFormat.getURL(line);
                        ExternPage externpage = new ExternPage(url);
                        externpage.fillExternPage(in);
                        externpage.printUnnamedWordVectorColumnFormat(out);
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

    public static void parseContentToURLandCategory(File infile, File outfile, Category category) {

        try {
            Scanner in = null;
            PrintWriter out = null;
            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (ContentFormat.isExternPageStart(line)) {
                        String url = ContentFormat.getURL(line);
                        ExternPage externpage = new ExternPage(url);
                        externpage.fillExternPage(in);
                        externpage.printURLwithinACategory(out, category);
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
}
