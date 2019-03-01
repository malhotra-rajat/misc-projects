package edu.neu.madcourse.rajatmalhotra.dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// This class is for splitting the text dictionary into small files 
public class EncodeDictionary {
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		    BufferedWriter bw = null;
			BufferedReader br = new BufferedReader(new FileReader("res/raw/wordlist.txt"));
			String word;
			while ((word = br.readLine()) != null)
			{
		        FileWriter fw;
			    fw = new FileWriter("res/raw/dict_files/" + word.charAt(0) + word.charAt(1)+ ".txt", true);
			    bw = new BufferedWriter(fw);
		    	bw.write(word + "\n");
		    	bw.flush();
			}
			br.close();
			bw.close();
	}
}
