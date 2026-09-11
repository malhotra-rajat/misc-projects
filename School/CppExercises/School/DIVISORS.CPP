#include<iostream.h>
#include<conio.h>
void main()
{
	clrscr();
	int i,num;
	cout<<"Enter an integer: ";
	cin>>num;
	for(i=1;i<=num/2;i++)
	{
		if(num%i==0)
		cout<<"\n"<<i;
	}

	getch();
}