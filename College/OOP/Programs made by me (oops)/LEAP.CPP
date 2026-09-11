//Program to generate list of leap years falling between 1900 & 2008

#include<iostream.h>
#include<conio.h>

void printleap(int lower, int higher)
{
	int year;

	cout<<"The leap years between "<<lower<<" and "<<higher<<" are:\n";

	for (year = lower; year <= higher ; year++)
	{
		if ( (year%4==0 && year%100!=0) || (year%400==0 ) )
		{
			cout<<"\n"<<year;
		}
	}
}


void main()
{
	clrscr();

	printleap (1900, 2008);

	getch();


}

