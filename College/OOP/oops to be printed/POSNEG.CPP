//To Show a number IS POSITIVE OR NEGATIVE using goto

#include<iostream.h>
#include<conio.h>

void check(int);

void main()
{
	int num;
	char ch;

	start:      //label
	clrscr();
	cout<<"Enter the number: ";
	cin>>num;

	check(num);          // function called
	cout<<"\n\nWant To Enter More (y, n): ";
	cin>>ch;

	if(ch=='y'||ch=='Y')
	goto start;

	getch();
}
void check(int n)
{
	if(n<0)
		cout<<"\n"<<n<<" is a Negative number";
	else if(n>0)
		cout<<"\n"<<n<<" is a Positive number";
}



