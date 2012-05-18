/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dmoz;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hoshun
 */
public class ContentFormat {

    public static void main(String[] args) {
        
        String a = "Top/Arts/gogle";
        Matcher match = topicTwoHeirachyPattern.matcher(a);

        //System.out.println(match.find());
        System.out.println(match.groupCount());

        if (match.find()) {
            for (int i = 1; i <= match.groupCount(); i++) {
                System.out.println(match.group(i));
            }
        }

        
        System.out.println(getURL("<ExternalPage about=\"http://www.awn.com/\">"));
        //System.out.println( a.substring(4,match.end()));
        //System.out.println( match.group(2) );
        //System.out.println( match.group(3) );
    }

    public static boolean isExternPageStart(String line) {
        Matcher matcher = externPageStartPattern.matcher(line);
        return matcher.find();
    }

    public static boolean isExternPageEnd(String line) {
        return line.trim().equalsIgnoreCase("</ExternalPage>");
    }
    
    public static boolean isPriorityTag(String line){
        Matcher matcher = priorityPattern.matcher(line);
        return matcher.find();
    }
    
        
    public static String getURL(String line){
        Matcher matcher = externPageStartPattern.matcher(line);
        if(!matcher.find()){
            return "";
        }
        return matcher.group(1);
    }

    public static String getTitle(String line) {
        Matcher matcher = titlePattern.matcher(line);
        if (!matcher.find()) {
            //System.out.println("Cannot find title in: " + line);
            return "";
        }
        return matcher.group(1);
    }

    public static String getDescription(String line) {
        Matcher matcher = descriptoinPattern.matcher(line);
        if (!matcher.find()) {
            //System.out.println("Cannot find descriiption in: " + line);
            return "";
        }
        return matcher.group(1);
    }

    public static String getTopic(String line) {
        Matcher matcher = topicPattern.matcher(line);
        if (!matcher.find()) {
            //System.out.println("Cannot find topic in: " + line);
            return "";
        }
        return matcher.group(1);
    }

    public static ArrayList<String> getFirstTwoHeirachy(String topic) {
        Matcher match = topicTwoHeirachyPattern.matcher(topic);
        ArrayList<String> rs = new ArrayList<String>();
        if (match.find()) {
            for (int i = 1; i <= match.groupCount(); i++) {
                rs.add(match.group(i));
            }
        }
        return rs;
    }

    
    private static Pattern topicTwoHeirachyPattern = Pattern.compile("Top/([a-zA-Z]+)/([a-zA-Z]+)/*.*?");
    private static Pattern externPageStartPattern = Pattern.compile("<ExternalPage about=\"http://(.+?)\"");
    private static Pattern titlePattern = Pattern.compile("<d:Title>(.+?)</d:Title>");
    private static Pattern descriptoinPattern = Pattern.compile("<d:Description>(.+?)</d:Description>");
                                                                                         
    private static Pattern topicPattern = Pattern.compile("<topic>(.+?)</topic>");
    private static Pattern priorityPattern = Pattern.compile("<priority>(.+)?</priority>");
}
