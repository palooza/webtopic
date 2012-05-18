package rawparser;

import java.util.Iterator;
import java.util.List;
import java.util.regex.*;
import java.util.logging.*;


public class UrlFilter {
	public static void main(String[] args) {
		//System.out.println();
		String sd = "www.forum.com%)";
		String td = "www.hardwareportal.ru','[hardwareportal.ru])(";
		try{
			String url =  "www.hardwareportal.ru','[hardwareportal.ru]";
		}catch(Exception e){
				e.printStackTrace();
		}
	}
        
        
        /*
	public static boolean isFilteredBySourceKeyPattern (String url, String sourceDomainName){
		String targetDomainName = util.Url.getDomainName(url);
		boolean r = false;
			if(!isFilteredBySyntax(sourceDomainName)){
				try{
					List<Pattern> plist = util.Url.domainKeyPattern(sourceDomainName);
					if( plist != null){
						for ( Iterator<Pattern> iterator = plist.iterator(); iterator.hasNext();) {
							Pattern keyPattern = (Pattern) iterator.next();
							r = r || keyPattern.matcher(targetDomainName).find();
						}
						if(r) return r;
					}
				}catch(PatternSyntaxException e){
					logger.log(Level.SEVERE, "Can't compile: " + "'" + sourceDomainName + "'");
					// throw new OmitThisPageException();
				}
			}else{
				// throw new OmitThisPageException();
			}
			return r;
		}
         * */
         
	
	public static boolean isFilteredBySyntax (String url) {
		boolean r = false; 
		
		// filter specific symbol. 
		r = r || leftParenthesis.matcher(url).find() ||
			rightParenthesis.matcher(url).find() ||
			questionMark.matcher(url).find() ||
			space.matcher(url).find() ||
			star.matcher(url).find();
		return r;
	}

	
	public static boolean isFilteredByLength(String url, int len){
		boolean r = false;
		
		if(url.length() > len || url.length() == 0){
			r = true;
			return r;
		}
		return r;
	}
	
	public static boolean isFilteredBySite(String url){
		boolean r = false;
		
		// filter specific sites.
		r = r || google.matcher(url).find() ||
			bing.matcher(url).find() ||
			yahoo.matcher(url).find() ||
			altavista.matcher(url).find();
		if(r) return r;
		return r;
	}
	
	public static boolean isFilteredByFormat(String url){
		boolean r = false;
		
		// filter specific format. 
		r = r || css.matcher(url).find() ||
			cfm.matcher(url).find();
		return r;
	}
	
	
	private static Pattern google = Pattern.compile("^www.google");
	private static Pattern bing = Pattern.compile("^www.bing");
	private static Pattern yahoo = Pattern.compile("^www.yahoo");
	private static Pattern altavista = Pattern.compile("^www.altavista");
	
	private static Pattern css = Pattern.compile("\\.css$");
	private static Pattern cfm = Pattern.compile("\\.cfm$");
	
	private static Pattern questionMark = Pattern.compile("\\?");
	// private static Pattern multiSlash = Pattern.compile("//");
	private static Pattern leftParenthesis = Pattern.compile("\\(");
	private static Pattern rightParenthesis = Pattern.compile("\\)");
	private static Pattern star = Pattern.compile("\\*");
	private static Pattern space = Pattern.compile("\\ ");
	
	// private static Logger logger = Logger.getLogger("webcomm");
}
