#include<stdio.h>
#include<conio.h>
#include<string.h>

void main()
{
	int i;
	clrscr();
	float sum = 0;

	for (i=1; i<=20; i++)
	{
		sum = sum + 1.0/i;
	}

	printf ("\n1 + 1/2 + 1/3 + .... 1/20\n\nThe sum of the above series is: %f", sum);
	getch();
}


