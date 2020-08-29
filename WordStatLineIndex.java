import java.util.*;
import java.io.*;

public class WordStatLineIndex {
  public static void main(String[] args) {

	if (args.length < 2) {
		System.out.println("Input and output file names should be defined");
		return;
	}

	NavigableMap<String, ArrayList<Integer>> wordsMap = new TreeMap<String, ArrayList<Integer>>(); 
	int strNum = 0, numInLine = 0;
	try {
		Scanner inputFile = new Scanner( new FileInputStream(args[0]), "utf-8");
		try {
			String str;
			while (inputFile.hasNextLine()) {
				str = inputFile.nextLine().toLowerCase() + " ";
				int wordLength = 0;
				strNum++;
				numInLine = 0;
				for (int i = 0; i < str.length() ; i++) {
					char c = str.charAt(i);
					if (Character.isLetter(c) || c == '\'' || Character.getType(c) == Character.DASH_PUNCTUATION) {
						wordLength++;
					} else {
						if (wordLength > 0) {
							numInLine++;
							String word = str.substring(i - wordLength, i);
							ArrayList<Integer> v = wordsMap.get(word);
							if (v == null) {
								v = new ArrayList<Integer>();
								wordsMap.put(word, v); 
							}
							v.add(strNum);
							v.add(numInLine);
							wordLength = 0;
						}
					}	
				}	
			}
		} finally {
			inputFile.close();
		}
	} catch (FileNotFoundException err) {
		System.out.println("Input file not found: " + err.getMessage());
        return;
	}  catch (UnsupportedEncodingException err) {
        System.out.println("Incorrect input file encoding: " + err.getMessage());
        return;
    }

	try {
		OutputStreamWriter outWriter = new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8");
		try {
			for (Map.Entry<String, ArrayList<Integer>> mapEntry:wordsMap.entrySet()) {
				ArrayList<Integer> v = mapEntry.getValue();
				outWriter.write(mapEntry.getKey() + " " + v.size() / 2);
				int k=0;
				for (int n : v) {
					outWriter.write(((++k & 1) == 0 ? ":" : " ") + n);
				}
				outWriter.write("\n");
			}
		} finally {
			outWriter.close();
		} 
	} catch (IOException err) {
			System.out.println("Output file write error: " + err.getMessage());
	} 
  }
}
