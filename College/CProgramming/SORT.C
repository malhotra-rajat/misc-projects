#include<stdio.h>
#include<conio.h>

void main()
{
	int a[] = {1,2,3,0,0,0};
	int i,j;
	int n = 6;
	int temp, small,loc;

	clrscr();


	printf("\n\nBubble sort\n");


	for (i = 0; i<n; i++)
	{

		for (j=0; j<n-i-1; j++)
		{
			if ( a[j] > a[j+1] )
			{
				temp = a[j];
				a [j] = a[j+1];
				a [j+1] = temp;
			}
		}
	}


       printf ("\n\n");
       for (i=0; i<n; i++)
	{
		printf ("%d ", a[i]);
	}
	getch();

	printf("\n\n\nSelection sort\n");

	for (i = 0; i<n; i++)
	{

		small = a[i];
		loc = i;
		for (j= i+1; j<n; j++)
		{
			if (small > a[j])
			{
				small = a[j];
				loc = j;
			}
		}
		if ( loc != i )
		{
			temp = a[i];
			a[i] = a[loc];
			a [loc] = temp;
		}
	}

       printf ("\n\n");
       for (i=0; i<n; i++)
	{
		printf ("%d ", a[i]);
	}
	getch();

	printf("\n\n\nInsertion sort\n");

	for (i=0; i<n-1; i++)
	{

		for (j=i+1; j>0 ; j--)
		{
			if ( a[j] < a[j-1] )
			{
				temp = a[j-1];
				a[j-1] = a[j];
				a[j] = temp;
			}
			else
			{
				break;
			}
		}




	}



       printf ("\n\n");
       for (i=0; i<n; i++)
	{
		printf ("%d ", a[i]);
	}
	getch();

}