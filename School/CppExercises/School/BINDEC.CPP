#include<iostream.h>
#include<conio.h>
#include<process.h>
unsigned int creatnum(int*num);
void main()
{
	clrscr();
	const int max=10;
	int a[max],m;
	cout<<"Enter the no of digits: ";
	cin>>m;
	if(m>max)
	{
		cout<<"Please enter between 1-10";
		getch();
		exit(0);
	}
	for(int i=0;i<m;i++)
	cin>>a[i];
	int z=creatnum(int 55);
	cout<<z;

	getch();
}



	unsigned int creatnum(int num)
	{
	int i;
	unsigned int a=0;
	for(i=0;i<32;i++)
	{
	 a=a/num[i];
	 if(i!=31)
	 i<<=1;
	}
	return a;
	}

