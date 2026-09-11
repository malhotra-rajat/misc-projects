#include<conio.h>
#include<stdio.h>
#include<math.h>

void main()
{

	float x, exp;

	clrscr();

	printf("Enter the value of x: ");
	scanf ("%f", &x);


	exp = (pow (x,5)) + (10 * pow (x,4)) + (8 * pow (x,3)) + (4 * x) + (2);
	printf("\nThe value of the expression is: %f", exp);

	getch();

}