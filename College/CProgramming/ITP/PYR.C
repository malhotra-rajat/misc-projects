#include<stdio.h>
#include<conio.h>

void main()
{
	int i , j, n;
	clrscr();
	printf ("Enter the number of rows: ");

	scanf ("%d", &n);
	printf ("\n");
	for (i=1; i<=n; i++)
	{
		for (j=0; j<i; j++)
		{
			printf ("%d ", i);
		}
		printf ("\n");
	}

	getch();
}