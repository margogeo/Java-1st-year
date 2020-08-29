//author Margarita Shimanskaia
//converts Markdown markup to HTML
package md2html;

import java.util.*;
import java.io.*;

public class Md2Html {
	static final int hTagBase = 100;	//+0 - <p> +1,2,3 ... - <h*>

	static Stack<TagItem> tags = new Stack<TagItem>();
	static StringBuilder sout = new StringBuilder();	//Output text
	
	static final String markTags = "`**__-+";
	static final int[] markFlags = {1,3,3,3,3,2,2};  //1-only single char, 3-single or double, 2-only double
	static final String htmlTags[] = {"code", "em", "strong", "em", "strong", "s", "u"};

	static private void closeParagraph() {	
		if (!tags.empty()) {
			TagItem ctag = tags.pop();
			if (ctag.tag == hTagBase) {
				sout.append("</p>");
			}
			if (ctag.tag > hTagBase) {
				sout.append("</h" + (ctag.tag - hTagBase) + ">");
			}
		}
	}

	static private void cleanSingleTags() { //Replace unclosed single *_ with initial symbols	
		while (!tags.empty()) {
			TagItem stored = tags.peek();
			if (stored.tag == 1 || stored.tag == 3) { //Unpaired -> replace
				sout.delete(stored.pos, stored.pos + 4);
				sout.insert(stored.pos, markTags.substring(stored.tag, stored.tag + 1));
				tags.pop();
			}
			else
				return;	
		}
	}	

	static final String htmlSpecial = "<>&";	
	static final String htmlReplace[] = {"&lt;", "&gt;", "&amp;"};

	static private boolean isSpecial(char c) {
		int e = htmlSpecial.indexOf(c);
		if (e >= 0)	{	//Special symbol &<> 
			sout.append(htmlReplace[e]);
			return true;
		}
		return false;
	}

  public static void main(String[] args) {
	if (args.length < 2) {
		System.err.println("2 file names should be defined");
		return;
	}
	boolean isPrevLineEmpty = true;

	try {
		Scanner in = new Scanner (new FileInputStream(args[0]), "utf8"); 

		while (in.hasNextLine()) {
			String s = in.nextLine();

			if (s.trim().length() == 0) {
				cleanSingleTags();	
				closeParagraph();
				isPrevLineEmpty = true;
				continue;
			}
			int ns = 0;  //Current index in s
			if (sout.length() > 0) {  //Not for 1st textline
				sout.append("\n");
			}

			if (isPrevLineEmpty) {
				while (ns < s.length() && s.charAt(ns) == '#') {
					ns++;
				}
				if (s.charAt(ns) > ' ') {
					ns = 0;		//<p> tag	
				}
				tags.push(new TagItem(ns + hTagBase, sout.length()));
				if (ns > 0) {	
					sout.append("<h" + ns + ">");
					ns++; 	//Skip separating space after ###
				}
				else {
					sout.append("<p>");
				}
			}
			isPrevLineEmpty = false;
			while (ns < s.length()) {
				char c = s.charAt(ns++);
				if (isSpecial(c)) {
					continue;
				}
				int t = markTags.indexOf(c);
				int nch = 1;
				if (ns < s.length() && c == s.charAt(ns)) {  
					nch = 2;		//Next char is the same
				}
				if (t >= 0 && (markFlags[t] & nch) > 0) {	//Markdown tags + supported number of chars
					if (markFlags[t] == 3 && nch == 2) {
						t++;	//Take next code for double char, if single is also possible
					}
					ns += nch - 1;  //Skip also 2nd char, if double

					if (tags.peek().tag == t) {  //Close tag
						TagItem ctag = tags.pop();
						sout.append("</" + htmlTags[t] + ">");
						continue;
					}
					tags.push(new TagItem(t, sout.length()));
					sout.append("<" + htmlTags[t] + ">");				
					continue;
				} 
				if (c == '\\') {
					c = s.charAt(ns++);  //Keep only next symbol after 
				}
				sout.append(c);
			}
		}	
		cleanSingleTags();
		closeParagraph();
		in.close();
    } catch (IOException err) {
		System.err.println("Input file not found: " + err.getMessage());
		return;
    }	

	try {
		OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8");
		outWriter.write(new String(sout), 0, sout.length());
		outWriter.close();	
	} catch (IOException err) {
		System.err.println("Output file write error: " + err.getMessage());
	} 
	//Clean output data 
	sout.delete(0, sout.length());
  }
}

