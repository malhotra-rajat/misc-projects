#include<iostream.h>
#include<conio.h>
//program to convert total marks & percentage
void main()
{
	clrscr();
	float a,b,c,d,e,sum;
	cout<<"Enter the marks of English: ";cin>>a;
	cout<<"Enter the marks of Maths: ";cin>>b;
	cout<<"Enter the marks of Physics: ";cin>>c;
	cout<<"Enter the marks of Chemistry: ";cin>>d;
	cout<<"Enter the marks of Computer Science: ";cin>>e;
	sum=a+b+c+d+e;
	float percentage;
	percentage=sum/5.0;

	cout<<"Total marks are: " <<sum;
	cout<<"\nThe percentage is: "<<percentage;


	getch();
}
