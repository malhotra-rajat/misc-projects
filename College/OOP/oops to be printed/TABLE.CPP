/*PROGRAM TO PRINT THE MULTIPLICATION TABLE OF A NUMBER*/

#include<iostream.h>
#include<conio.h>

void table (int no)
{
	cout<<"\n\nThe multiplication table of "<<no<<" is:\n\n";
	for (int i = 1; i<=10; i++)
	{
		cout<<no<<" * "<<i<<" = "<<no*i<<endl;
	}
}


void main()
{
	clrscr();
	int number;
	cout<<"Enter the number whose multiplication table is to be printed: ";
	cin>>number;
	table (number);
	getch();
}
