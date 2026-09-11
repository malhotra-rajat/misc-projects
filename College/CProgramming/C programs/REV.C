#include<stdio.h>
#include<conio.h>

void main()
{
	int num, quo, rem, rev=0;
	clrscr();

	printf ("Enter a 4 - digit number : ");
	scanf ("%4d", &num);

	rem = num%10;
	rev = rem * 1000;

	quo = num / 10;
	rem = quo%10;
	rev = rev + (rem * 100);

	quo = quo / 10;
	rem = quo%10;
	rev = rev + (rem * 10);

	quo = quo / 10;
	rem = quo%10;
	rev = rev + rem;

	printf ("\n\nThe reverse is: %d", rev);
	getch();

}











