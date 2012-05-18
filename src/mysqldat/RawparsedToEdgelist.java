/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqldat;

import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import java.util.*;
import java.io.*;
import java.sql.*;

import rawparser.RawchunckParser;
import util.PasswordField;
/**
 *
 * @author hoshun
 */
public class RawparsedToEdgelist {
    
    public static void main(String[] args) {
        File infile = new File(args[0]);
        File outfile = new File(args[1]);
        writeIndexedEdgeList(infile, infile, "jdbc:mysql://spade.ee.ucla.edu/webtopic", "hoshun", "xianwu");
    }
    
    	public static void writeIndexedEdgeList(File linklist, File indexed_edgeList, 
			String sqlurl, String user, String passwd){
		try {
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String query = "SELECT id FROM urls where urlcrc = crc32(?) AND url = ? limit 1";
			
			Scanner in = null;
			PrintWriter out = null;

			try {
				passwd = (passwd.isEmpty()) ? PasswordField.readPassword(user + "enter mysql password: ") : passwd;
				conn = DriverManager.getConnection(sqlurl, user, passwd);
				pstmt = conn.prepareStatement(query);
				
				in = new Scanner( new BufferedReader(new FileReader(linklist)) );
				out = new PrintWriter(indexed_edgeList);
				
				String line = null;
                                String curSourceUrl = null;
				int curSourceIndex = 0;
				int targetIndex = 0;
				int cnt = 0;
				int pageCount = 0;
				long startTime = System.nanoTime();
				while(in.hasNextLine()){
					cnt ++;
					line = in.nextLine();

					if(line.equals(RawchunckParser.breakSymbol)){
						pageCount++;
						curSourceUrl = in.nextLine();
						//System.out.println(curSourceUrl);
						curSourceIndex = getIndex(curSourceUrl, pstmt);
					}
					else{
						//System.out.println(line);
						targetIndex = getIndex(line, pstmt);
						out.println(curSourceIndex + "\t" + targetIndex);
					}
					
					if(cnt % 100000 == 0){
						long curTime = System.nanoTime();
						float duration = (float)((curTime - startTime) / Math.pow(10,9) / 60);
						int b = (int) (cnt / 100000);
						System.out.printf("line %d \tbatch %d-th\t page\t%d\t used %.2f min\n",cnt, b, pageCount,duration);
					}
				}
				
			} finally {
				conn.close();
			}
		} catch (Exception e) {
                    e.printStackTrace();
		}	
	}
        
	private static int getIndex(String url, PreparedStatement pstmt) throws SQLException {
		
		pstmt.setString(1, url);
		pstmt.setString(2, url);
		ResultSet rs = pstmt.executeQuery();
		int ind = -1;
		while( rs.next() ){
			ind = rs.getInt(1);
		}
		return ind;
	}
	
    
}
