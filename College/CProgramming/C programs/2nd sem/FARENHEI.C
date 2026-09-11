#include<stdio.h>
#include<conio.h>

// Program to convert the input temperature in celsius and convert to farenheit

void main()
{
	float celsius, farenheit;
	clrscr();

	printf ("Enter the temperature in celsius: ");
	scanf ("%f", &celsius);

	farenheit = (1.8*celsius) + 32;

	printf ("\nThe temperature in farenheit is: %f", farenheit);


	getch();
}

