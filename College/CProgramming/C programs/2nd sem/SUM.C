#include<stdio.h>
#include<conio.h>

// Program to add two numbers

void main()
{
	int a, b, sum;
	clrscr();
	printf ("Enter number 1: ");
	scanf ("%d", &a);
	printf ("\nEnter number 2: ");
	scanf ("%d", &b);
	sum = a+b;
	printf ("\n\nThe sum is: %d", sum);
	getch();
}

