#include<conio.h>
#include<stdio.h>
#include<math.h>

void main()
{

	float x, y, exp;
	const float pi = 3.14;
	clrscr();

	printf("Enter the value of x and y: ");
	scanf ("%f%f", &x, &y);


	exp = 2.5 * log (x) - cos (pi / 6) + ( pow (x,2) - pow (y,2) ) + sqrt (2 * x * y);
	printf("\nThe value of the expression is: %f", exp);

	getch();

}