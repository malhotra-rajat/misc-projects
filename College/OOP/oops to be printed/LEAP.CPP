//TO GENERATE LEAP YEARS BETWEEN 1900 AND 2008

#include<iostream.h>
#include<conio.h>

void leap();

void main()
{
	clrscr();
	leap();     // function leap called
	getch();
}
void leap()
{
	int count=0; // to count no of leap year

	cout<<"Leap Year Between 1900 and 2008 are ... \n\n";

	for(int i=1900;i<=2008;i++)
	{
		if(i%4==0)
			if(i%100!=0||i%400==0)
			{
				cout<<i<<"\n";    // printing years
				count++;
			}
	}
	cout<<"\nThere are "<<count<<" leap year between 1900 and 2008 ";
}
