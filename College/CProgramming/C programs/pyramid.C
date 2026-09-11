#include<stdio.h>
#include<conio.h>

void main()
{
	int i,j,n,k,mid;
	clrscr();
	printf ("Enter an even number: ");
	scanf ("%d", &n);
	mid = n/2;
	for ( i=1; i<=mid; i++)
	{
	      if(i>1)
	      {
		 for(j=1;j<=i-1;j++)
		    printf("  ");
	      }
	      for (j=n;j>=mid+i;j--)
		 { printf ("%d ",j);
		 }
	      printf ("%d ", mid);
	     for(k=mid-i;k>=0;k--)
		  {
		   printf("%d ", k);
		   }
	     printf("\n");
	}
	for(i=1;i<=mid;i++)
	{printf("  ");
	 }
	 printf("%d",mid);
	getch();
}