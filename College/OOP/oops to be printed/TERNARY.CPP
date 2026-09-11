//TO PRINT MAXIMUM NUMBER USING TERNARY OPERATOR

#include<iostream.h>
#include<conio.h>

void maxm(int num1,int num2,int num3)
{
	int max=0;
	max=(num1>num2)?(num1>num3?num1:num3):(num2>num3?num2:num3);
	cout<<"\nThe Largest number is: "<<max;
}

void main()
{
	int num1,num2,num3;
	clrscr();
	cout<<"Enter the three numbers: ";
	cin>>num1>>num2>>num3; // getting user input
	maxm(num1,num2,num3);     // maximum function called
	getch();
}

