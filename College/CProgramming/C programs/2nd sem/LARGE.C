#include<stdio.h>
#include<conio.h>

// Program to find out the greatest of three integers

void main()
{
	int a, b, c, large;

	clrscr();

	printf ("Enter three numbers: ");
	scanf ("%d%d%d", &a, &b, &c);

	if (a>b)
		large = a;
	else
		large = b;

	if (large < c)
		large = c;

	printf ("\nThe greatest of three numbers is: %d", large);

	getch();
}

