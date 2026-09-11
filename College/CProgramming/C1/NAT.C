#include<stdio.h>
#include<conio.h>

void main()
{
	int i, sum = 0, n;
	clrscr();
	printf ("Enter the number of natural numbers to be summed: ");
	scanf ("%d", &n);

	for (i=1; i<=n; i++)
		sum = sum+i;

	printf ("\nThe sum of 1st %d natural numbers is: %d", n, sum);

	getch();

}
