//TO SHOW NO OF DAYS IN GIVEN YEAR AND MONTH

#include<iostream.h>
#include<conio.h>

int leap(int year)
{
	if((year%4==0&&year%100!=0)||(year%400==0))
	{
		return 0;
	}
	else
	{
		return 1;
	}
}

void days(int year,int month)
{
	int days=0,daysy=0;
	int l;
	l=leap(year);

	switch (month)
	{
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: days = 31;
			 break;

		case 2: if(l==0)
				days=29;
			else
				days=28;
			break;

		case 4:
		case 6:
		case 9:
		case 11: days = 30;
			 break;

		default: cout<<"\n\nInvalid Choice";

	}

	if(l==0)
		daysy=366;
	else
		daysy=365;

	cout<<"\n\nNo. of days in the year are: "<<daysy;
	cout<<"\nNo. of days in the month are: "<<days;

}

void main()
{
	clrscr();
	int year, month;

	cout<<"Enter the year: ";
	cin>>year;

	cout<<"\nEnter the month (1-12): ";
	cin>>month;

	days(year, month);

	getch();
}
