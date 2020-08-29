//author Margarita Shimanskaia
//Standart Scanner replacement, based on Reader, works faster than the Standart one on selected tasks(f. e. ReverseMin and WordStat from my GitHub)
import java.util.*;
import java.io.*;

public class Scanner {

  private String inLine = null;	/* next input line */
  private BufferedReader bufReader = null;
  private InputStreamReader streamReader = null;
  private final String inputEmpty = "Scanner input is empty";
  private int foundWordPos = -1, lastIntVal = 0; 	

  //Load next line for processing and return true, if exists, otherwise - false 
  private boolean loadNextLine() {
	try {
			if (null != bufReader)
				inLine = bufReader.readLine();
			else
				inLine = null;
			return null != inLine;
	}
	catch (Exception err) {
		err.printStackTrace();
		return false;
	}
  }	

  //Find the next word position (1st char index), load next lines, if necessary
  private int findWordPos() {
	for( ;; ) {
		if( inLine == null)
			if( ! loadNextLine())
				return -1;
		for (int i=0 ; i < inLine.length() ; i++)		
			if (!Character.isWhitespace(inLine.charAt(i))) 
				return (foundWordPos = i);	
		inLine = null; 
	}
  }

  //Find end pos of earlier found word, parameter is always correct
  private int endOfWord(int iFrom) {
	int i;
	for ( i = iFrom ; i < inLine.length() ; i++)		
		if (Character.isWhitespace(inLine.charAt(i))) 
			break;
	return i;  
  }

  //Extract found word from loaded line and cut it, parameters are always correct	
  private String getWord(int iFrom, int iTo) {
	String word = inLine.substring(iFrom, iTo);
	inLine = inLine.substring(iTo);
	if(inLine.length() == 0)
		inLine = null;
	return word;
  }	

  //Extract and cut word by 1st pos only
  private String getWord(int iFrom) {
	return getWord(iFrom, endOfWord(iFrom));
  }	

  public boolean hasNext() {
	return findWordPos() >= 0;
  }

  public String next() {
	String nextWord = getWord(findWordPos());
	if (nextWord == null)
		throw new IllegalStateException(inputEmpty);
	return nextWord;
  }

  public boolean hasNextLine() {
	if (inLine == null) 	
		loadNextLine();
	return inLine != null; 	
  }

  public String nextLine() {
	if( !hasNextLine() ) {
		throw new IllegalStateException(inputEmpty);
	}
	return getWord(0,inLine.length()); 	
  }

  public boolean hasNextInt(int radix) {
	int iFrom = findWordPos();
	if (iFrom < 0)
		return false;
	String strNum = inLine.substring(iFrom, endOfWord(iFrom));
	try {
		lastIntVal = Integer.parseInt( strNum, radix );
	}
	catch (Exception err) {
		return false;
	}
	return true;
  }

  public boolean hasNextInt() {
	return hasNextInt(10);
  }

  public int nextInt(int radix) {
	if (hasNextInt(radix)) {
		getWord(foundWordPos);
		return lastIntVal;
	}
	throw new IllegalStateException("Not int value");
  }

  public int nextInt() {
	return nextInt(10);
  }

  public Scanner(InputStream source) {
	try {
		bufReader = new BufferedReader(streamReader = new InputStreamReader(source));
 	}
	catch (Exception err) {
		err.printStackTrace();
		close();
	} 
 }

  public Scanner(InputStream source, String charsetName) {
	try {
		bufReader = new BufferedReader(streamReader = new InputStreamReader(source, charsetName));
	}
	catch (Exception err) {
		err.printStackTrace();
		close();
	} 
  }

  public Scanner(String source) {
	inLine = source;			
  }

  public void close() {
	try {
		if(bufReader != null)
			bufReader.close();
	}
	catch (Exception err) {
		err.printStackTrace();
	}
	try {
		if(streamReader != null)
			streamReader.close();
	}
	catch (Exception err) {
		err.printStackTrace();
	}
	bufReader = null;
	streamReader = null;
	inLine = null;
  }
}
