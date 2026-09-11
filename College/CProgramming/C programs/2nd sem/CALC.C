#include<stdio.h>
#include<conio.h>

// Program which uses switch statement to create a calculator


void main()
{
	char choice;
	int a, b, res;

	clrscr();

	printf ("Enter your choice (+, -, *, /): ");
	scanf ("%c", &choice);

	switch (choice)
	{
		case '+': printf ("\nEnter two numbers: ");
			  scanf ("%d%d", &a, &b);
			  res = a+b;
			  printf ("\nThe sum is: %d", res);
			  break;

		case '-': printf ("\nEnter two numbers: ");
			  scanf ("%d%d", &a, &b);
			  res = a-b;
			  printf ("\nThe difference is: %d", res);
			  break;

		case '*': printf ("\nEnter two numbers: ");
			  scanf ("%d%d", &a, &b);
			  res = a*b;
			  printf ("\nThe product is: %d", res);
			  break;

		case '/': printf ("\nEnter two numbers: ");
			  scanf ("%d%d", &a, &b);
			  res = a/b;
			  printf ("\nThe quotient is: %d", res);
			  break;

		default: printf ("Invalid choice");

	}


	getch();

}		 