#include<iostream.h>
#include<conio.h>
//this is a program to find the area of the circle;
int main()
{
	clrscr ();
	float radius,area;
	const float PI=3.14;
	cout<<"Enter the radius of the circle: ";
	cin>>radius;
	area=PI*radius*radius;
	cout<<"The area of the circle is: "<<area;
	getch();
    return 0;    
}
