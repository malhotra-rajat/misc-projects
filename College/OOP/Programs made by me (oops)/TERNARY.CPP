//Program to print maximum number using ternary operator

#include<iostream.h>
#include<conio.h>

void check(int no1, int no2, int no3)
{
	int max = (no1>no2) ? (no1>no3 ? no1:no3) : (no2>no3 ? no2:no3);
	cout<<"\n\nThe highest number is: "<<max;

}

void main()
{

	int no1, no2, no3;

	clrscr();

	cout<<"Enter three numbers: ";  //Taking input from user
	cin>>no1>>no2>>no3;

	check (no1,no2,no3);    //Calling check() function

	getch();


}
