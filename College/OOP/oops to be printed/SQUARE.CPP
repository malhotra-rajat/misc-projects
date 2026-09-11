//TO CALCULATE SQUARE AND SHOW FUNCTION OVERLOADING

#include<iostream.h>
#include<conio.h>

int square(int a)
{
	return a*a;
}

float square(float a)  //function with same name as above
{
	return a*a;
}

void main()
{
	clrscr();
	int a;
	float b;
	cout<<"Enter Integer Number: ";
	cin>>a;
	cout<<"\nEnter Floating Point Number: ";
	cin>>b;
	cout<<"\n\nSquare of Integer = "<<square(a);
	cout<<"\n\nSquare Of Float = "<<square(b);
	getch();
}

