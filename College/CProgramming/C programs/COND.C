#include<stdio.h>
#include<conio.h>

void main()
{
	int year, leap=0;
	clrscr();
	printf ("Enter an year: ");
	scanf ("%d", &year);


	year%4 == 0 ? (year % 100 !=0 ? leap = 1 : leap = 0) : leap = 0;
       //	year % 400 == 0 ? leap = 1 : leap = 0;

	if (leap == 1)
		printf ("The year %d is a leap year", year);
	else
		printf ("\nThe year %d is not a leap year", year);

	getch();

}