import java.io.*;
public class charcount 
{
	public static void main(String args[]) throws IOException
	{
		char ch;
		
		
		int charac=0;
		int wrd=-1;
		BufferedReader inputstream =new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a string...");

        do{
        	ch=(char)inputstream.read();
        	if(Character.isWhitespace(ch))
                wrd++;
        	else charac++;
        }while(ch!='\n');
        System.out.println("No Of characters in a line:" +charac);
        System.out.println("No Of words in a line:" +wrd);
	}
}

/* OUTPUT
Enter a string...
HI i am attending the JAVA class
No Of characters in a line:26
No Of words in a line:7 */
