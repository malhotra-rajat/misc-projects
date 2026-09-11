#include<iostream.h>
#include<conio.h>


void main()
{
	clrscr();
	int a[5];

	cout<<"\nEnter the elements: ";

	for ( int i =0 ; i<5; i++)
	{	cin>>a[i];
		cout<<endl;
	}

	int  min = 32767, max = -32768;

	for (i = 0 ; i<5; i++)
	{
		if ( max < a[i] )
			max = a[i];
		if ( min > a[i] )
			min = a[i];
	}

	cout<<"Minimum: "<<min;
	cout<<"\nMaximum: "<<max;
	getch();
}

