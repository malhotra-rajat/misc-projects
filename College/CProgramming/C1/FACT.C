#include<stdio.h>
#include<conio.h>

void main()
{
	int num, fact = 1, n;
	clrscr();

	printf ("Enter the number: ");
	scanf ("%d", &num);

	n = num;

	while (num)
	{
		fact = fact*num;
		num--;
	}

	printf ("\nThe factorial of %d is: %d",n, fact);

	getch();
}





