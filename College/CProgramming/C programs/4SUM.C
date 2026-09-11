#include<stdio.h>
#include<conio.h>

void main()
{
	int num, quo, rem, sum = 0;
	clrscr();

	printf ("Enter a 4 - digit number : ");
	scanf ("%4d", &num);

	rem = num%10;
	sum = sum + rem;

	quo = num / 10;
	rem = quo%10;
	sum = sum + rem;


	quo = quo / 10;
	rem = quo%10;
	sum = sum + rem;

	quo = quo / 10;
	rem = quo%10;
	sum = sum + rem;

	printf ("\n\nThe sum is: %d", sum);
	getch();

}











