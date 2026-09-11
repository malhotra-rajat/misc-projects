import java.util.Scanner;
public class selection 
{
  public static void main(String args[])
  {
	  int n,small,pos,temp;
	  int[] a=new int[10];
	  Scanner in=new Scanner(System.in);
	  System.out.print("Enter the number of elements to b sorted <=10:");
	  n=in.nextInt();
	  System.out.print("Enter the elements to be sorted:");
	  for(int i=0;i<n;i++)
	  {
		 a[i]=in.nextInt(); 
	  }
	  in.close();
	  for(int i=0;i<n;i++)
	  {
		  small=a[i];
		  pos=i;
		  for(int j=i+1;j<n;j++)
		  {
			  if(a[j]<small)
			  {
				  small=a[j];
				  pos=j;
			  }
		  }
		  temp=a[i];
		  a[i]=a[pos];
		  a[pos]=temp;
	  }
	  System.out.println("The array after the selection sort is:");
	  for(int i=0;i<n;i++)
	  {
		  System.out.print(a[i] + " ");
	  }
  }
}
/* OUTPUT
 
Enter the number of elements to b sorted <=10:5
Enter the elements to be sorted:33 1 12 10 6 
The array after the selection sort is:
1 6 10 12 33 */