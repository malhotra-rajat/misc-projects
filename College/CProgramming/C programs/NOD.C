#include<stdio.h>
#include<conio.h>

void main()
{
	int a, count=0;

	clrscr();

	printf ("Enter a no: (max. 4 digits): ");
	scanf ("%d", &a);


	if ( a!=0)
	count ++;

	a = a/10;
	if ( a!=0)
	count ++;

	a = a/10;
	if ( a!=0)
	count ++;

	a = a/10;
	if ( a!=0)
	count ++;

	printf ("The no of digits is: %d", count);



	getch();
}