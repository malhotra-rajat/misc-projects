#include<stdio.h>
#include<conio.h>
#include<math.h>


void main()
{
	int a, r, n, sum;
	clrscr();

	printf ("Enter the first term of the geometric series: ");
	scanf ("%d", &a);

	printf ("Enter the common ratio of the series: ");
	scanf ("%d", &r);

	printf ("Enter the no. of terms: ");
	scanf ("%d", &n);

	sum = (a * (pow (r, n) - 1)) / (r-1);

	printf ("The sum of the series is: %d", sum);

	getch();

}


