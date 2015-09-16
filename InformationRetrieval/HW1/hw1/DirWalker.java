package hw1;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirWalker {
	
	public static List<File> getFiles (String folder)
	{
		List<File> files = new ArrayList<File>();
		  File dir = new File(folder);
    	  File[] directoryListing = dir.listFiles();
    	  if (directoryListing != null) {
    	    for (File child : directoryListing) {
    	    	files.add(child);
    	    }
    	   }
    	  return files;
	}

}
