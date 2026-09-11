//Program to check if a given number is positive or negative

#include<iostream.h>
#include<conio.h>

void check(int no)
{
	if (no > 0)
	{
		cout<<"\nThe number entered is positive";
	}
	else if (no < 0)
	{
		cout<<"\nThe number entered is negative";
	}
	else
	{
		cout<<"\nThe number entered is zero";
	}

}



void main()
{

	char ch;
	int no;

	start:

	clrscr();
	cout<<"Enter a number: ";  //Taking input from user
	cin>>no;

	check (no);    //Calling check() function

	cout<<"\n\nDo you wish to continue? (y, n) ";
	ch=getche();

	if (ch == 'y' || ch == 'Y')
	{
		goto start;
	}


}

