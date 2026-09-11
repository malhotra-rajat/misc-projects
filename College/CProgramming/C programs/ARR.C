#include<stdio.h>
#include<conio.h>

void main()
{
	int a[10] = {1,2,3,4,5,6};
	int n=6;
	int data;
	int i;

	clrscr();

	printf ("Enter the element to be added: ");
	scanf ("%d", &data);

	a [n] = data;
	n = n+1;
	printf ("Now, the array is: ");

	for (i=0; i<n; i++)
	{
		printf("\n%d", a[i]);
	}

	getch();

}

