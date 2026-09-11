#include<stdio.h>
#include<conio.h>

// Program to swap two numbers without using extra variable

void main()
{
	int a, b;

	clrscr();

	printf ("Enter a: ");
	scanf ("%d", &a);

	printf ("\nEnter b: ");
	scanf ("%d", &b);

	a = a+b;
	b = a-b;
	a = a-b;

	printf ("\n\nThe value of a and b after swapping are:\n\na = %d\nb = %d", a, b);

	getch();

}