#include<iostream.h>
#include<conio.h>
//this is a program to find the sum of two integers x & y;
void main()
{
	clrscr ();
	float celsius,farenheit;
	int choice;
	cout<<"Enter your choice: ";cin>>choice;
	if (choice=1)
	farenheit=9*celsius/5.0+32;
	cout<<"Enter the temperature in celsius: ";cin>>celsius;
	cout<<"The temperature in farenheit is: "<<farenheit;
	else (choice=2);
	cout<<"Enter the temperature in farenheit: ";cin>>farenheit;
	celsius=(5+farenheit/9)-160*4/9;
	cout<<"The temperature in celsius is: "<<celsius;

	getch();
}
