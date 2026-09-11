//PROGRAM TO CHECK IF A NUMBER IS EVEN OR ODD

#include<iostream.h>
#include<conio.h>

void check(int);

void main()
{
	clrscr();
	int num;
	cout<<"Enter the number: ";
	cin>>num;
	check(num);    // function called
	getch();
}

void check(int no)
{
	if(no%2==0) // checking if even or odd
		cout<<"\n"<<no<<" is an Even Number";
	else
		cout<<"\n"<<no<<" is an Odd Number";
}
