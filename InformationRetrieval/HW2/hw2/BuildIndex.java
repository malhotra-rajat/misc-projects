package hw2;

import hw2.model.DocNosPositions;
import hw2.model.TermStats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.tartarus.snowball.ext.PorterStemmer;


public class BuildIndex {

	public static ArrayList<String> stopWords = new ArrayList<String>();

	static String folder = "E:\\Dropbox\\Dropbox\\IR\\InformationRetrieval\\IR_data\\AP89_DATA\\AP_DATA\\ap89_collection";
 	static String indexFolder = "E:\\Dropbox\\Dropbox\\IR\\indexFiles";

 	static String stopWordsFilePath = "E:\\Dropbox\\Dropbox\\IR\\InformationRetrieval\\IR_data\\AP89_DATA\\AP_DATA\\stoplist.txt";

	static String indexFolderTempPath = "E:\\Dropbox\\Dropbox\\IR\\indexTextTemp\\";
	
	static String catalogFolderTempPath = "E:\\Dropbox\\Dropbox\\IR\\catalogTextTemp\\";

	static String indexFolderMain = "E:\\Dropbox\\Dropbox\\IR\\indexMain\\";
	
	static String mainIndexFilePath = "E:\\Dropbox\\Dropbox\\IR\\indexMain\\mainIndex"; 
	static String mainCatalogFilePath = "E:\\Dropbox\\Dropbox\\IR\\indexMain\\mainCatalog"; 
	

	
	static HashMap<Integer, String> docIdMap;

	public static void main(String[] args) throws Exception {

     	populateStopWordsSet();
		buildIndexFiles();
		writeHashMapsToFilesMain();
		mergetoMainIndexMain();

	}

	static HashMap<String, TermStats> termMapFor1000docs = new HashMap<String, TermStats>();
	
	private static void buildIndexFiles() throws IOException {
		docIdMap = new HashMap<Integer, String>();
		List<File> files = DirWalker.getFiles(folder);
		
		int count = 0;
		for (File file : files) {
			buildIndexForFile(file);
			count++;
			System.out.println("Files processed: " + count);
		}
		
		if (noOfDocs !=0 && noOfDocs < 1000)
		{
			String filename = "ap" + fileNameSuffix;
			FileOutputStream f = new FileOutputStream(indexFolder + "//"
				+ filename);
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(termMapFor1000docs);
			s.close();
			noOfDocs = 0;
			fileNameSuffix = 1;
			termMapFor1000docs.clear();
		}
		
		
		Iterator<Entry<Integer, String>> it1 = docIdMap.entrySet().iterator(); //build docid map

		System.out.println(docIdMap.size());

		PrintWriter writer = null;
		try {

			writer = new PrintWriter(indexFolderMain + "docIdFile",
					"UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		while (it1.hasNext()) {

			Map.Entry pair = (Map.Entry) it1.next();
			int dochash = (Integer) pair.getKey();
			String docnoSize = (String) pair.getValue();
			String parts[] = docnoSize.split(",");
			

			writer.println(dochash + "," + parts[0] + "," + parts[1]);
			it1.remove(); // avoids a ConcurrentModificationException
		}
		writer.close();

	}

	private static void writeHashMapsToFilesMain() throws IOException {
		List<File> indexFiles = DirWalker.getFiles(indexFolder);
		int count = 0;
		for (File file : indexFiles) {
			if (file.getName().startsWith("ap")) {
				writeHashMapsToFiles(file);
				count++;
				System.out.println("Files processed: " + count);
			}

		}
	}

	private static void writeHashMapsToFiles(File file) {
		try {
			FileInputStream fisFile = new FileInputStream(file);
			ObjectInputStream oisFile = new ObjectInputStream(fisFile);
			HashMap<String, TermStats> termMapForFile = (HashMap<String, TermStats>) oisFile
					.readObject();
	
			oisFile.close();
	
			PrintWriter catalogWriter = null;
			
			RandomAccessFile indexWriterFile = new RandomAccessFile(new File (indexFolderTempPath 
					+ file.getName()), "rws");			
			
			try {
				catalogWriter = new PrintWriter(catalogFolderTempPath + 
						 file.getName(), "UTF-8");
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			Iterator it1 = termMapForFile.entrySet().iterator();
	
			System.out.println(termMapForFile.size());
	
			while (it1.hasNext()) {
	
				Map.Entry pair = (Map.Entry) it1.next();
				String term = (String) pair.getKey();
	
				TermStats termStats = (TermStats) pair.getValue();
				ArrayList<DocNosPositions> docNosTFPositionsList = termStats
						.getDocNosTFPositions();
	
				String docNoTFPositionsStringFinal = getStringFromDocNosPositionsString(docNosTFPositionsList);
		
				
				int length = docNoTFPositionsStringFinal.getBytes().length;
	
				catalogWriter.println(term + "," + indexWriterFile.getFilePointer() + "," + length);
				indexWriterFile.write(docNoTFPositionsStringFinal.getBytes());
	
				it1.remove(); // avoids a ConcurrentModificationException
	
			}
			catalogWriter.close();
			indexWriterFile.close();
		} catch (Exception e) {
	
		}
	}
	
	static int noOfDocs = 0;
	static int fileNameSuffix = 1;

	private static void buildIndexForFile(File file) {

		try {
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("<DOC>")) {
					String docno = null;
					StringBuilder text = new StringBuilder();
					String textString = null;
					String docLine;
					int current_doc_no_hash = 0;
					while (!(docLine = br.readLine()).contains("</DOC>")) {
						if (docLine.contains("<DOCNO>")) {
							int indexStart = docLine.indexOf("<DOCNO>") + 7;
							int indexEnd = docLine.indexOf("</DOCNO>");
							docno = docLine.substring(indexStart, indexEnd)
									.trim();
							current_doc_no_hash = docno.hashCode();
							
						}

						if (docLine.contains("<TEXT>")) {
							String textLine;
							while (!(textLine = br.readLine())
									.contains("</TEXT>")) {
								text.append(" " + textLine);
							}
						}
					}

					//textString = text.toString().trim().toLowerCase(); //option1 only this thing, remvove others
					 textString = removeStopWords(text.toString().trim().toLowerCase()); //option2

					String[] words = textString.split("\\s+");
					ArrayList<String> textStringWords = new ArrayList<String>();

					for (int z = 0; z < words.length; z++) {
						if (words[z].matches("\\w+(\\.?\\w+)*")) {
							
							words[z] = stemmer(words[z]); //option3
							
							//option4 -> combine option2 and option3
							textStringWords.add(words[z]);
						}
					}
					
					String finalString = "";
					for(int i=0; i<textStringWords.size(); i++)
					{
						finalString = finalString + textStringWords.get(i) + " ";
					}
					
					finalString = finalString.trim();
					
					if (!docIdMap.containsKey(docno)) {
						docIdMap.put(current_doc_no_hash, docno + "," + getWordcount(finalString));
						noOfDocs++;
					}

					for (int i = 0; i < textStringWords.size(); i++) {
						
						ArrayList<DocNosPositions> docNosTFPositionsList;
						String currentTerm = textStringWords.get(i);
						if (termMapFor1000docs.containsKey(currentTerm)) {
							TermStats termStats = termMapFor1000docs
									.get(currentTerm);
							docNosTFPositionsList = termStats
									.getDocNosTFPositions();

							ArrayList<DocNosPositions> newDocNosTFPositionsList = new ArrayList<DocNosPositions>();

							for (int x = 0; x < docNosTFPositionsList.size(); x++) {
								int doc_no_hash = docNosTFPositionsList.get(x)
										.getDoc_no_hash();

								if (doc_no_hash == current_doc_no_hash) {
									DocNosPositions newDocNosTfPositions;
									ArrayList<Integer> positions = docNosTFPositionsList
											.get(x).getPositions();

									positions.add(i + 1);

						
									newDocNosTfPositions = new DocNosPositions(doc_no_hash, positions);
									newDocNosTFPositionsList
											.add(newDocNosTfPositions);
								} else {
									newDocNosTFPositionsList
											.add(docNosTFPositionsList.get(x));
								}
							}

							boolean documentInList = false;
							for (int p = 0; p < newDocNosTFPositionsList.size(); p++) // adding
																						// a
																						// new
																						// row
																						// for
																						// new
																						// docs
							{
								if (newDocNosTFPositionsList.get(p)
										.getDoc_no_hash() == current_doc_no_hash) {
									documentInList = true;
								}
							}
							if (documentInList == false) {
								ArrayList<Integer> positions = new ArrayList<Integer>();
								positions.add(i + 1);
								newDocNosTFPositionsList
										.add(new DocNosPositions(current_doc_no_hash, positions));
							}

							termMapFor1000docs.replace(currentTerm, new TermStats(newDocNosTFPositionsList));
						} else {
							docNosTFPositionsList = new ArrayList<DocNosPositions>();

							ArrayList<Integer> positions = new ArrayList<Integer>();
							positions.add(i + 1);
							int tf = 1;
							docNosTFPositionsList.add(new DocNosPositions(current_doc_no_hash, positions));

							// TermStats termStats = new TermStats(term_id,
							// docNosTFPositionsList, 0, 0);
							TermStats termStats = new TermStats(docNosTFPositionsList);
							termMapFor1000docs.put(currentTerm, termStats);
							
							if (noOfDocs >= 1000)
							{
								String filename = "ap" + fileNameSuffix;
								FileOutputStream f = new FileOutputStream(indexFolder + "//"
									+ filename);
								ObjectOutputStream s = new ObjectOutputStream(f);
								s.writeObject(termMapFor1000docs);
								s.close();
								noOfDocs = 0;
								fileNameSuffix++;
								termMapFor1000docs.clear();
							}
						
							
							
						}
					}
				}
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	static int count = 0;
	
	private static void mergetoMainIndexMain() throws IOException {
		List<File> indexFiles = DirWalker.getFiles(indexFolderTempPath);
		List<File> catalogFiles = DirWalker.getFiles(catalogFolderTempPath);
	
		
		superMainCatalogHashMap = new HashMap<String, ArrayList<String>>();
		
		
		String mainIndexFileTempPath = "E:\\Dropbox\\Dropbox\\IR\\indexMain\\mainIndexTemp";
		String mainCatalogTempPath = "E:\\Dropbox\\Dropbox\\IR\\indexMain\\mainCatalogTemp";
	
		
		File mainIndexFile = null;
		File mainCatalogFile = null;
		
		File mainIndexFileTemp = null;
		File mainCatalogFileTemp = null;
		
		for (int i=0; i<indexFiles.size(); i++)
		{
			String indexFileName = FilenameUtils.getName(indexFiles.get(i).getName());
			String catalogFileName = FilenameUtils.getName(catalogFiles.get(i).getName());
			
			if (indexFileName.equals(catalogFileName) && indexFileName.startsWith("ap") && catalogFileName.startsWith("ap")) {
				
				
				if (count % 10 == 0) {
					
					
					mainIndexFile = new File(mainIndexFilePath + file_no);
					mainCatalogFile = new File(mainCatalogFilePath + file_no);
					
					mainIndexFileTemp = new File(mainIndexFileTempPath + file_no);
					mainCatalogFileTemp = new File(mainCatalogTempPath + file_no);
					
					FileUtils.copyFile(indexFiles.get(i), mainIndexFile);
					FileUtils.copyFile(catalogFiles.get(i), mainCatalogFile);
					
					file_no++;
					
				}

				else {
					mergeToMainIndex(indexFiles.get(i), catalogFiles.get(i), mainIndexFile, mainCatalogFile, mainIndexFileTemp, mainCatalogFileTemp);
				}

				count++;
				System.out.println("Files processed: " + count);
			}
		}
		
		FileOutputStream fos = new FileOutputStream(new File("E:\\Dropbox\\Dropbox\\IR\\indexMain\\superMainCatalogHashMap"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(superMainCatalogHashMap);
		oos.close();

	}

	static RandomAccessFile mainIndexFileRandom;
	static RandomAccessFile mainIndexFileModifiedRandom;
	static PrintWriter mainCatalogFileWriter;
	
	static HashMap<String, ArrayList<String>> superMainCatalogHashMap;
	
	static int file_no = 1;
	
	private static void mergeToMainIndex(File indexFile, File catalogFile, File mainIndexFile, File mainCatalogFile, File mainIndexFileTemp, File mainCatalogFileTemp) {
		try {

			HashMap<String, String> catalogMainMap = new HashMap<String, String>();
			HashMap<String, String> catalogFileMap = new HashMap<String, String>();
			
			BufferedReader brCatalogMain  = new BufferedReader(new FileReader(mainCatalogFile));
			String line;
			while ((line = brCatalogMain.readLine()) != null) {
				String words[] = line.split(",");
				catalogMainMap.put(words[0], words[1] + "," + words[2]);
			}

			BufferedReader brCatalog = null;
			brCatalog = new BufferedReader(new FileReader(catalogFile));
			String line1;
			while ((line1 = brCatalog.readLine()) != null) {
				String words[] = line1.split(",");
				catalogFileMap.put(words[0], words[1] + "," + words[2]);
				
			}
			
			brCatalogMain.close();
			brCatalog.close();
			
			mainIndexFileRandom = new RandomAccessFile(mainIndexFile, "r");
		
			mainIndexFileModifiedRandom = new RandomAccessFile(mainIndexFileTemp, "rw");
			mainCatalogFileWriter = new PrintWriter(new FileWriter(mainCatalogFileTemp)); 
				
			RandomAccessFile indexFileRandom = new RandomAccessFile(indexFile, "r");
			
			List<String> keysNotInIndexButInFile = new ArrayList<String>();
			List<String> keysInIndex = new ArrayList<String>();
			List<String> keysInIndexButNotInFile = new ArrayList<String>();
			
			for (Entry<String, String> entry: catalogFileMap.entrySet()) {
				
					   if (catalogMainMap.containsKey(entry.getKey())) //write the terms which are in both files
						    {
						   		keysInIndex.add(entry.getKey());
						    
						    }
					   else
					   {
						   keysNotInIndexButInFile.add(entry.getKey());
					   }
					}
			
							for (int i=0; i<keysInIndex.size(); i++)
							{
						    	   String offsetLengthString = catalogFileMap.get(keysInIndex.get(i));
								   String words[] = offsetLengthString.split(",");
								   int offsetInFile = Integer.parseInt(words[0]);
								   int lengthInFile = Integer.parseInt(words[1]);
								   
								   
								   indexFileRandom.seek(offsetInFile);
								   
								   byte[] bytes = new byte[lengthInFile];
								   
								   indexFileRandom.read(bytes);
								   String newTermStatsInFile =  new String(bytes, "UTF-8"); 
								   newTermStatsInFile = newTermStatsInFile.trim();
								   
								   
								   ArrayList<DocNosPositions> docNosTfPositionsListInFile = getDocNosPositionsFromText(newTermStatsInFile);
								   
								   String offSetLengthMainIndexString = catalogMainMap.get(keysInIndex.get(i)); 
								   String wordsIndex[] = offSetLengthMainIndexString.split(",");
								   int offsetInIndex = Integer.parseInt(wordsIndex[0]);
								   int lengthInIndex = Integer.parseInt(wordsIndex[1]);
	
								   mainIndexFileRandom.seek(offsetInIndex);
								   byte[] bytesIndex = new byte[lengthInIndex];
								   
								   mainIndexFileRandom.read(bytesIndex);
								   String termStatsInIndex =  new String(bytesIndex, "UTF-8"); 
								   termStatsInIndex = termStatsInIndex.trim();
								   
								   ArrayList<DocNosPositions> docNosTfPositionsListInIndex = getDocNosPositionsFromText(termStatsInIndex);
								   
								   
								   String newList = getStringFromDocNosPositionsString
										   (combineDocNoTfPositions(docNosTfPositionsListInIndex, docNosTfPositionsListInFile));
								   
								   //write this new string to file
								   //update catalog
								  
								   if (superMainCatalogHashMap.containsKey(keysInIndex.get(i)))
								   {
									   ArrayList<String> fileList = superMainCatalogHashMap.get(keysInIndex.get(i));
									   String indexCatalogFileName = mainIndexFile.getCanonicalPath() + "," + mainCatalogFile.getCanonicalPath();
									   if (!fileList.contains(indexCatalogFileName))
									   {
										   fileList.add(indexCatalogFileName);
									   }
									   superMainCatalogHashMap.replace(keysInIndex.get(i), fileList);
								   }
								   else
								   {
									   ArrayList<String> fileList = new ArrayList<String>();
									   String indexCatalogFileName = mainIndexFile.getCanonicalPath() + "," + mainCatalogFile.getCanonicalPath();
									   fileList.add(indexCatalogFileName);
									   superMainCatalogHashMap.put(keysInIndex.get(i), fileList);
								   }
								   
								   mainCatalogFileWriter.println(keysInIndex.get(i) + "," + mainIndexFileModifiedRandom.getFilePointer() + "," + newList.getBytes().length);
									mainIndexFileModifiedRandom.write(newList.getBytes());
								   
							}
							
						for (int i=0; i<keysNotInIndexButInFile.size(); i++) //write the terms in the new file which were not in the old file
						{
							 String offsetLengthString = catalogFileMap.get(keysNotInIndexButInFile.get(i));
							   String words[] = offsetLengthString.split(",");
							   int offsetInFile = Integer.parseInt(words[0]);
							   int lengthInFile = Integer.parseInt(words[1]);
							   
							   indexFileRandom.seek(offsetInFile);
							   
							   byte[] bytes = new byte[lengthInFile];
							   
							   
							   indexFileRandom.read(bytes);
							   
							   if (superMainCatalogHashMap.containsKey(keysNotInIndexButInFile.get(i)))
							   {
								   ArrayList<String> fileList = superMainCatalogHashMap.get(keysNotInIndexButInFile.get(i));
								   
								   String indexCatalogFileName = mainIndexFile.getCanonicalPath() + "," + mainCatalogFile.getCanonicalPath();
								   if (!fileList.contains(indexCatalogFileName))
								   {
									   fileList.add(indexCatalogFileName);
								   }
								 
								   superMainCatalogHashMap.replace(keysNotInIndexButInFile.get(i), fileList);
							   }
							   else
							   {
								   ArrayList<String> fileList = new ArrayList<String>();
								   String indexCatalogFileName = mainIndexFile.getCanonicalPath() + "," + mainCatalogFile.getCanonicalPath();
								   fileList.add(indexCatalogFileName);
								   superMainCatalogHashMap.put(keysNotInIndexButInFile.get(i), fileList);
							   }
							   
							   mainCatalogFileWriter.println(keysNotInIndexButInFile.get(i) + "," + mainIndexFileModifiedRandom.getFilePointer() + "," + bytes.length);
								mainIndexFileModifiedRandom.write(bytes);

						}
						
						
						for (Entry<String, String> entry: catalogMainMap.entrySet()) { //write the terms in the old file which were not in the new file
							   if (!catalogFileMap.containsKey(entry.getKey()))
							    {
								   keysInIndexButNotInFile.add(entry.getKey());
								}
						}
						
						for (int i=0; i<keysInIndexButNotInFile.size(); i++)
						{
							 String offsetLengthString = catalogMainMap.get(keysInIndexButNotInFile.get(i));
							   String words[] = offsetLengthString.split(",");
							   int offsetInFile = Integer.parseInt(words[0]);
							   int lengthInFile = Integer.parseInt(words[1]);
							   
							   
							   mainIndexFileRandom.seek(offsetInFile);
							   
							   byte[] bytes = new byte[lengthInFile];
							   
							   
							   mainIndexFileRandom.read(bytes);
							
							   
							   if (superMainCatalogHashMap.containsKey(keysInIndexButNotInFile.get(i)))
							   {
								   ArrayList<String> fileList = superMainCatalogHashMap.get(keysInIndexButNotInFile.get(i));
								   String indexCatalogFileName = mainIndexFile.getCanonicalPath() + "," + mainCatalogFile.getCanonicalPath();
								   if (!fileList.contains(indexCatalogFileName))
								   {
									   fileList.add(indexCatalogFileName);
								   }
								   superMainCatalogHashMap.replace(keysInIndexButNotInFile.get(i), fileList);
							   }
							   else
							   {
								   ArrayList<String> fileList = new ArrayList<String>();
								   String indexCatalogFileName = mainIndexFile.getCanonicalPath() + "," + mainCatalogFile.getCanonicalPath();
								   fileList.add(indexCatalogFileName);
								   superMainCatalogHashMap.put(keysInIndexButNotInFile.get(i), fileList);
							   }
							   
							   mainCatalogFileWriter.println(keysInIndexButNotInFile.get(i) + "," + mainIndexFileModifiedRandom.getFilePointer() + "," + bytes.length);
								mainIndexFileModifiedRandom.write(bytes);

						}
						
					
						
						mainIndexFileModifiedRandom.close();
						mainCatalogFileWriter.close();
						mainIndexFileRandom.close();
						
						indexFileRandom.close();
		
						mainIndexFile.delete();
						mainCatalogFile.delete();
						
						mainIndexFileTemp.renameTo(mainIndexFile);
						mainCatalogFileTemp.renameTo(mainCatalogFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	private static ArrayList<DocNosPositions> getDocNosPositionsFromText(String s)
	{
		ArrayList<DocNosPositions> docNosTfPositionsList = new ArrayList<DocNosPositions>();
		
		s = s.substring(1, s.length()-1);
		
		String[] docs = s.split(",");
		
		for (int i=0; i<docs.length; i++)
		{
			docs[i] =  docs[i].substring(1,docs[i].length() - 1);
		}
		
		for (int i=0; i<docs.length; i++)
		{
			String[] docNoPositions = docs[i].split("\\?");
			
			String doc_no_hash = docNoPositions[0];
			docNoPositions[1] = docNoPositions[1].substring(1, docNoPositions[1].length() - 1);
			String[] positionsStrings = docNoPositions[1].split(";");
			
			ArrayList<Integer> positions = new ArrayList<Integer>();
			for (int j=0; j<positionsStrings.length; j++)
			{
				positions.add(Integer.parseInt(positionsStrings[j]));
			}
			
			docNosTfPositionsList.add(new DocNosPositions(Integer.parseInt(doc_no_hash), positions));
		}
		
		return docNosTfPositionsList;
		
	}
	
	private static ArrayList<DocNosPositions> combineDocNoTfPositions(ArrayList<DocNosPositions> mainIndexList, ArrayList<DocNosPositions> listInFile)
	{
		ArrayList<DocNosPositions> docNosTfPositionsList = new ArrayList<DocNosPositions>();
		
		docNosTfPositionsList.addAll(mainIndexList);
		docNosTfPositionsList.addAll(listInFile);
		
		return docNosTfPositionsList;
	}
	
	private static String getStringFromDocNosPositionsString(ArrayList<DocNosPositions> docNosTFPositionsList)
	{
		
		String docNoTFPositionsStringFinal = "[";

		for (int i = 0; i < docNosTFPositionsList.size(); i++) {
			
			int doc_no_hash = docNosTFPositionsList.get(i)
					.getDoc_no_hash();

			String positionsString = "[";

			ArrayList<Integer> positions = docNosTFPositionsList.get(i)
					.getPositions();
			for (int j = 0; j < positions.size(); j++) {
				int position = positions.get(j);

				if (j == 0) {
					positionsString = positionsString + position;
				} else {
					positionsString = positionsString + ";" + position;
				}
			}
			
			positionsString = positionsString + "]";

			String docNoPositionsString = "[" + doc_no_hash + "?"
					+ positionsString + "]";
			
			
			if (i == 0) {
				docNoTFPositionsStringFinal = docNoTFPositionsStringFinal + docNoPositionsString;
			}

			else {
				docNoTFPositionsStringFinal = docNoTFPositionsStringFinal
						+ "," + docNoPositionsString;
			}

		}
		docNoTFPositionsStringFinal = docNoTFPositionsStringFinal + "]";

		return docNoTFPositionsStringFinal;
	}
	
	private static ArrayList<DocNosPositions> returnTermStatsFromFile(File mainIndexFile, File mainCatalogFile, String term) throws IOException
	{
		HashMap<String, String> catalogMainMap = new HashMap<String, String>();
		
		BufferedReader brCatalogMain  = new BufferedReader(new FileReader(mainCatalogFile));
		String line;
		while ((line = brCatalogMain.readLine()) != null) {
			String words[] = line.split(",");
			catalogMainMap.put(words[0], words[1] + "," + words[2]);
		}

		brCatalogMain.close();

		RandomAccessFile mainIndexFileRandom = new RandomAccessFile(mainIndexFile, "r");
				    	
					    	   String offsetLengthString = catalogMainMap.get(term);
							   String words[] = offsetLengthString.split(",");
							   int offsetInFile = Integer.parseInt(words[0]);
							   int lengthInFile = Integer.parseInt(words[1]);
							   
							   
							   mainIndexFileRandom.seek(offsetInFile);
							   
							   byte[] bytes = new byte[lengthInFile];
							   
							   
							   mainIndexFileRandom.read(bytes);
							   String newTermStatsInFile =  new String(bytes, "UTF-8"); 
							   newTermStatsInFile = newTermStatsInFile.trim();
							   
							   
							   ArrayList<DocNosPositions> docNosTfPositionsListInFile = getDocNosPositionsFromText(newTermStatsInFile);
							   
							   mainIndexFileRandom.close();
					
							   return docNosTfPositionsListInFile;
	}
	
	
	private static boolean testIndex(String term) throws IOException, ClassNotFoundException
	{
			FileInputStream fis = new FileInputStream (new File("E:\\Dropbox\\Dropbox\\IR\\indexMain\\superMainCatalogHashMap"));
			ObjectInputStream ois = new ObjectInputStream(fis);
			HashMap<String, ArrayList<String>> superMainCatalogHashMap = new HashMap<String, ArrayList<String>>();
			
			superMainCatalogHashMap = (HashMap<String, ArrayList<String>>) ois.readObject();

			ois.close();
			fis.close();
			
			
			if (superMainCatalogHashMap.containsKey(term))
			{
				ArrayList<DocNosPositions> docNosTfPositionsListInIndex = new ArrayList<DocNosPositions>();
				List<String> docs = superMainCatalogHashMap.get(term);
				
				for (int i=0; i<docs.size(); i++)
				{
					String parts[] = docs.get(i).split(",");
					
					File mainIndexFile = new File(parts[0]);
					File mainCatalogFile = new File(parts[1]);
					docNosTfPositionsListInIndex.addAll(returnTermStatsFromFile(mainIndexFile, mainCatalogFile, term));
				}
				
				 System.out.println("DF: " + docNosTfPositionsListInIndex.size());
				   
				   System.out.println("TF in a doc: " + docNosTfPositionsListInIndex.get(0).getPositions().size());
				   
				   int ttf = 0;
				   for (int j=0; j<docNosTfPositionsListInIndex.size(); j++)
				   {
					   ttf = ttf + docNosTfPositionsListInIndex.get(j).getPositions().size();
				   }
				   System.out.println("TTF: " + ttf);
			
				return true;
			}
				
			else
			{
				return false;
			}
			
	}
	
	private static String getWordcount (String text)
	{
		String trimmed = text.trim();
		Integer words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		return words.toString();
	}

	public static String removeStopWords(String string) {
		String result = "";
		String[] words = string.split(" ");
		ArrayList<String> wordsList = new ArrayList<String>();
		for(String word : words)
		{
			String wordCompare = word.toLowerCase();
			if(!stopWords.contains(wordCompare))
			{
				wordsList.add(word);
			}
		}
		for (String str : wordsList){
			result = result + str + " ";
		}
		return result;
	}
	
	private static void populateStopWordsSet()
	{
		File stopWordsFile = new File(stopWordsFilePath);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(stopWordsFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		try {
			while ((line = br.readLine()) != null) {
				stopWords.add(line);
			}
			br.close();
			System.out.println(stopWords.size());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String stemmer(String word){
		PorterStemmer obj = new PorterStemmer();
		obj.setCurrent(word);
		obj.stem();
		return obj.getCurrent();
	}

}