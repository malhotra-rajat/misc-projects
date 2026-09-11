/*PROGRAM TO CALCULATE AREA AND SHOW FUNCTION OVERLOADING*/

#include<iostream.h>
#include<conio.h>
#include<math.h>       //for square root

inline float area(float l=0.0,float b=0.0)
{
	return l*b;
}

float area(int r=0)
{
	return 3.14*r*2;
}

float area(float a,float b,float c)
{
	float s;
	s=(a+b+c)/2;
	return sqrt(s*(s-a)*(s-b)*(s-c));
}

void main()
{
	int ch;
	float l,a,b,c;
	int r;
	clrscr();

	cout<<"Enter 1 for Rectangle ";
	cout<<"\nEnter 2 for Circle ";
	cout<<"\nEnter 3 for Triangle \n";

	cin>>ch;

	switch(ch)
	{
		case 1: cout<<"\nEnter Length: ";
			cin>>l;
			cout<<"Enter Breadth: ";
			cin>>b;
			cout<<"\nArea = "<<area(l,b);
			break;

		case 2: cout<<"\nEnter Radius: ";
			cin>>r;
			cout<<"\nArea = "<<area(r);
			break;

		case 3: cout<<"\nEnter First Side: ";
			cin>>a;
			cout<<"Enter Second Side:  ";
			cin>>b;
			cout<<"Enter Third Side: ";
			cin>>c;
			cout<<"\nArea = "<<area(a,b,c);
			break;

		default: cout<<"Wrong Choice ";
	}
	getch();
}
