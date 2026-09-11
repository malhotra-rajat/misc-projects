#include<iostream.h>
#include<conio.h>
//this is a program to find the least among three integers a,b&c;
void main()
{
	clrscr();
	int a,b,c,l;
	cout<<"Enter the three integers: ";cin>>a>>b>>c;
	if(a<b)
	l=a;
	else
	l=b;
	if(l>c)
	l=c;
	cout<<"The least value is: "<<l;
	getch();
}