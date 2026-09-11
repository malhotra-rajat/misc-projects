#include<stdio.h>
#include<conio.h>

void main()
{
	int num, i, j;
	clrscr();
	printf ("Enter the number: ");

	scanf ("%d", &num);
	printf ("\nThe multiplication table of %d: \n", num);

	for (i=1; i<=10; i++)
	{
		j = num * i;
		printf ("\n%d * %d = %d\n", num, i, j);
	}
	getch();
}





