#include<stdio.h>
#include<conio.h>

void main()
{
	int num, last, i, first;
	clrscr();

	printf ("Enter a number : ");
	scanf ("%d", &num);

	last = num % 10;

	first  = num/10;

	while (first>10)
	{
		first = first /10;
	}


	printf ("\n\nThe sum of first and last digit is: %d", first + last );
	getch();

}











