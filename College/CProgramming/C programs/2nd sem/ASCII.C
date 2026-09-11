#include<stdio.h>
#include<conio.h>

// Program to display the ASCII value of the character entered

void main()
{
	char a;
	int b;

	clrscr();

	printf ("Enter a character: ");
	scanf ("%c", &a);

	b = a;

	printf ("\nThe ASCII value of the character entered is: %d", b);

	getch();

}