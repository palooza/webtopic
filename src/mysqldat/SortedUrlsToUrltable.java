/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqldat;

import java.util.*;
import java.io.*;
import java.util.zip.CRC32;
import java.sql.*;

/**
 * Provide index and crc32 code for sorted urls.
 *
 * @author hoshun
 */
public class SortedUrlsToUrltable {

    public static void main(String[] args) {
        
        writeToTableFormat(new File(args[0]), new File(args[1]));
        System.out.println("Processed " + args[0] );
        String sqlurl = "jdbc:mysql://spade.ee.ucla.edu/urlmap";
        String tablename = "url_" + args[0].toString().replaceAll(".*01-2011-.*-","");
        
        createTable(tablename, sqlurl, "hoshun", "xianwu");
        
        System.out.println("Load data into " + tablename);
        loadDataToTable(tablename, new File(args[1]), sqlurl, "hoshun", "xianwu");
        
    }

    public static void writeToTableFormat(File infile, File outfile) {
        try {
            Scanner in = null;
            PrintWriter out = null;

            try {
                in = new Scanner(new BufferedReader(new FileReader(infile)));
                out = new PrintWriter(outfile);

                long urlindex = 0;
                while (in.hasNextLine()) {
                    String url = in.nextLine();

                    CRC32 crc32 = new CRC32();
                    crc32.update(url.getBytes());
                    long hashval = crc32.getValue();

                    out.println(urlindex + "\t" + hashval + "\t" + url);
                    urlindex++;
                }

            } finally {
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String tablename, String sqlurl, String user, String passwd){
        
        try {
            Connection conn = null;
            try{
                conn = DriverManager.getConnection(sqlurl, user, passwd);
                conn.createStatement().
                        execute("CREATE TABLE IF NOT EXISTS " + tablename
                        + " ( id INT UNSIGNED NOT NULL, "
                        + " urlcrc INT UNSIGNED NOT NULL, "
                        + " url VARCHAR(512) NOT NULL, "
                        + " PRIMARY KEY(id), "
                        + " KEY(urlcrc) "
                        + ") engine = innodb; "
                        );
            }finally{
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
     public static void loadDataToTable(String tablename, File datafile, String sqlurl, String user, String passwd){
        
        try {
            Connection conn = null;
            try{
                conn = DriverManager.getConnection(sqlurl, user, passwd);
                conn.createStatement().
                        execute( "LOAD DATA LOCAL INFILE " + "\"" + datafile + "\"" +  " INTO TABLE " + tablename + ";"
                        );
            }finally{
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
