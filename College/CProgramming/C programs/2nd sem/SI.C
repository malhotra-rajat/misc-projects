#include<stdio.h>
#include<conio.h>

// Program to calculate Simple Interest

void main()
{
	float pr, rt, time, si;
	clrscr();

	printf ("Enter the principle amount: Rs. ");
	scanf ("%f", &pr);

	printf ("\nEnter the rate (in %): ");
	scanf ("%f", &rt);

	printf ("\nEnter the time: ");
	scanf ("%f", &time);

	si = (pr*rt*time) / 100;

	printf ("\n\nThe principal interest is: Rs. %f", si);

	getch();
}

