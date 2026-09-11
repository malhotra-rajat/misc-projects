//TO CALCULATE SQUARE USING MACROS

#include<iostream.h>
#include<conio.h>

#define SQUARE(x) x*x       // Macro defined

void main()
{
	clrscr();
	int num;
	cout<<"\nEnter a number: ";
	cin>>num;
	cout<<"\nSquare of number is: "<<SQUARE(num);
	getch();
}


