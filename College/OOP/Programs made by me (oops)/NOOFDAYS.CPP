/*Program to display the number of days of a given year & month using switch
  case
*/

#include<iostream.h>
#include<conio.h>

void noofdays(int year, int month)
{
	int number_of_days = 0;
	int leap;

	if ( (year%4==0 && year%100!=0) || (year%400==0 ) )
	{
		leap = 1;
	}
	else
	{
		leap = 0;
	}

	if (leap == 1)
	{
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12: number_of_days = 31;
				 break;

			case 2: number_of_days = 29;
				break;

			case 4:
			case 6:
			case 9:
			case 11: number_of_days = 30;
				break;


			default: cout<<"\n\nInvalid Choice";
		}
	}
	else
	{
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12: number_of_days = 31;
				 break;

			case 2: number_of_days = 28;
				break;

			case 4:
			case 6:
			case 9:
			case 11: number_of_days = 30;
				break;


			default: cout<<"\n\nInvalid Choice";

		}
	}

	if (number_of_days != 0)
	{
		cout<<"\n\nThe number of days of the entered year and month is: "<<number_of_days;
	}

}


void main()
{
	clrscr();
	int year, month;
	cout<<"Enter the year: ";
	cin>>year;

	cout<<"\nEnter the month (1-12): ";
	cin>>month;

	noofdays(year, month);

	getch();


}

