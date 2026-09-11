//PROGRAM TO IMPLEMENT STRUCTURES

#include<iostream.h>
#include<conio.h>

struct stu
{
	char name[20];
	int year;
	int rollno;
} s[5];

void show(int n)
{
	clrscr();
	cout<<"Name \t\t Rollno \t\t Year \n";

	for(int i=0;i<n;i++)
	{
		cout<<"\n"<<s[i].name;
		cout<<"\t\t\t"<<s[i].rollno;
		cout<<"\t\t"<<s[i].year;
	}
}

void input(int n)
{
	for(int i=0;i<n;i++)
	{
		cout<<"\n\n\nEnter Name: ";
		cin>>s[i].name;
		cout<<"\nEnter Roll Number: ";
		cin>>s[i].rollno;
		cout<<"\nEnter Year Of Admission: ";
		cin>>s[i].year;
	}
}

void sort(int n,int ch)
{
	stu temp;

	for(int i=0;i<n;i++)
		for(int j=0;j<=n-1-1;j++)  //bubble sort
		{
			if(ch==1)
			{
				if(s[j].year>s[j+1].year)
				{
					temp=s[j];
					s[j]=s[j+1];
					s[j+1]=temp;
				}
			}

			else if(ch==2)
			{

				if(s[j].rollno>s[j+1].rollno)
				temp=s[j];
				s[j]=s[j+1];
				s[j+1]=temp;
			}
		}
}

void main()
{
	clrscr();

	int n,ch;

	cout<<"Enter Number of Students: ";
	cin>>n;
	cout<<"\nEnter Details:";
	input(n);

	clrscr();

	cout<<"Enter 1 to show according to year";
	cout<<"\nEnter 2 to show according to roll no\n\n";

	cin>>ch;

	sort(n,ch);
	show(n);

	getch();
}

