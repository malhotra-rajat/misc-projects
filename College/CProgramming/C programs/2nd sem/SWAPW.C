#include<stdio.h>
#include<conio.h>

// Program to swap two numbers using extra variable

void main()
{
	int a, b, temp;

	clrscr();

	printf ("Enter a: ");
	scanf ("%d", &a);

	printf ("\nEnter b: ");
	scanf ("%d", &b);

	temp = a;
	a = b;
	b = temp;

	printf ("\n\nThe value of a and b after swapping are:\n\na = %d\nb = %d", a, b);

	getch();

}