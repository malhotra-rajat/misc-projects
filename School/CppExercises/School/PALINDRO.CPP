#include<iostream.h>
#include<conio.h>
#include<graphics.h>
void main()
{

	clrscr();
	int x;
	cout<<"Enter the number: ";cin>>x;
	cout<<endl;
	int d=x,rev=0,rem;
	while(d!=0)
	{
		rem=d%10;
		rev=rev*10+rem;
		d=d/10;
	}
	if(rev=x)
	cout<<"The number is a palendrome";
	else
	cout<<"The number is not a palendrome";
	getch();
}