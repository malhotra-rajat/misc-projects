#include<conio.h>
#include<stdio.h>

void main()
{

	float a,x,b, exp;

	clrscr();

	printf("Enter the value of a, x and b: ");
	scanf ("%f%f%f", &a,&x,&b);

	exp = (a*x+b)/(a*x-b);
	printf("\nThe value of the expression (ax+b)/(ax-b) is: %f", exp);

	getch();

}