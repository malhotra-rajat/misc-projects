#include<iostream.h>
#include<conio.h>
#include<math.h>
//this is a temperature;
void main()
{
	clrscr();
	cout<<"Rules\n1.Celsius to Farenheit\n2.Farenheit to Celsius\n\n";

	float celsius,farenheit;
	int choice;

	cout<<"Enter your choice: ";cin>>choice;

	if(choice!=1&&choice!=2)
	cout<<"Wrong choice\n";

	if(choice==1)
	{
		cout<<"Enter the temperature in celsius: ";
		cin>>celsius;
		farenheit=(1.8*celsius)+32.0;
		cout<<"The temperature in farenheit is: "<<farenheit;
	}

	if(choice==2)
	{
		cout<<"Enter the temperature in farenheit: ";cin>>farenheit;
		celsius=(farenheit-32)/1.8;
		cout<<"The temperature in celsius is: "<<celsius;
	}

	getch();
}
