#include<iostream.h>
#include<conio.h>
//program to convert celsius into farenheit
void main()
{
	clrscr();
	float x;
	cout<<"Enter the temperature in celsius: ";cin>>x;
	float y;
	y=9*x/5.0+32;
	cout<<"The temperature in farenheit is: "<<y;
	getch();
}
