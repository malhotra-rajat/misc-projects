#include<stdio.h>
#include<conio.h>

/* Program to input three sides of a triangle and display whether the triangle
   is isoceles, equilateral or scalene
*/

void main()
{
	int a, b, c;
	clrscr();

	printf ("Enter the three sides of a triangle: ");
	scanf ("%d%d%d", &a, &b, &c);

	if (a==b && b==c)
		printf ("\nThe traingle is equilateral");

	else if (a==b || b==c || c==a)
		printf ("\nThe traingle is isoceles");

	else
		printf ("\nThe traingle is scalene");

	getch();

}











