#include<stdio.h>
#include<conio.h>

void main()
{
	int num, rev = 0, i, n;
	clrscr();
	printf ("Enter the no: ");
	scanf ("%d", &num);
	n = num;

	do
	{
		i = num%10;
		rev = (rev*10) + i;
		num = num / 10;

	}while (num);

	if (n == rev)
		printf ("\nThe number is a palindrome");
	else
		printf ("\nThe number is not a palindrome");

	getch();

}
