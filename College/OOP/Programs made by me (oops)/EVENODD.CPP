//Program to check if a number is even or odd

#include<iostream.h>
#include<conio.h>

void check(int no)
{
	if (no%2==0)   //checking if even or odd
	{
		cout<<"\nThe number is even";
	}
	else
	{
		cout<<"\nThe number is odd";
	}
}



void main()
{
	clrscr();

	int no;

	cout<<"Enter a number: ";  //Taking input from user
	cin>>no;

	check (no);    //check() function called

	getch();
}

