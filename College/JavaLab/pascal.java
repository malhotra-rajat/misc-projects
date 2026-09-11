import java.util.Scanner;
public class pascal 
{
    public static void main(String args[])
    {
    	int n;
    	int[][] b=new int[10][10];
    	System.out.println("Please enter the number of rows of your pascal's triangle:");
    	Scanner in=new Scanner(System.in);
    	n=in.nextInt();
    	in.close();
    	for(int i=0;i<n;i++)
    	   {
    	           b[i][0]=1;
    	   }
    	   int k=0;
    	   for(int i=1;i<n;i++)
    	   {
    	         for(int j=1;j<=k+1;j++)
    	         {
    	               if(i==j)
    	                 b[i][j]=b[i-1][j-1];
    	                else
    	                b[i][j]=b[i-1][j-1]+b[i-1][j];
    	         }
    	         k++;
    	    }
    	    int l=n;
    	    for(int i=0;i<n;i++)
    	    {
    	      System.out.println();
    	      for(int j=0;j<(n-i);j++)
    	      {
    	            System.out.print(" ");
    	            if(j==l-1)
    	            {
    	                  for(int p=0;p<i+1;p++)
    	                  {
    	                       System.out.print(b[i][p]+" ");
    	                  }        
    	            }
    	      }
    	      l--;
    	    }
    	     
    	    
    }
}
/*OUTPUT
 
Please enter the number of rows of your pascal's triangle:
5

     1 
    1 1 
   1 2 1 
  1 3 3 1 
 1 4 6 4 1 */
