#include<stdio.h>
#include<conio.h>

void main()
{
	int i , j=1, n, k;
	clrscr();
	printf ("Enter the number of rows: ");

	scanf ("%d", &n);
	printf ("\n");
	for (i=1; i<=n; i++)
	{
		for (k=j; k<n; k++)
		{
			printf (" ");
		}
		for (j=1; j<=i; j++)
		{
			printf ("%d ", i);
		}
		printf ("\n");
	}

	getch();
}