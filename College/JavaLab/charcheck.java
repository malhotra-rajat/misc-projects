import java.io.*;

class charcheck{

      public static void main(String args[]) throws IOException
      {
         char ch;
         int digit=0;
         int upper=0;
         int lower=0;
         int other=0;

         BufferedReader inputstream =new BufferedReader(new InputStreamReader(System.in));

         System.out.println("Enter a string...");

         do{

          ch=(char)inputstream.read();

          if(Character.isDigit(ch))
            digit++;
          else if(Character.isUpperCase(ch))
            upper++;
          else if(Character.isLowerCase(ch))
            lower++;
		  else if(ch!='\n')
			other++;

          }while(ch!='\n');

          System.out.println("No Of Digits:" +digit);
          System.out.println("No Of Uppercase Characters:" +upper);
          System.out.println("No Of Lowercase Characters:" +lower);
          System.out.println("No Of Other Characters:" +other);

         }
     }


