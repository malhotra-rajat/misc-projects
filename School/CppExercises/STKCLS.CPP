#include<iostream.h>
#include<conio.h>
#include<stdio.h>
#include<string.h>
#include<process.h>

struct node
{
	int roll_no;
	char name[20];
	node *link;
};

class stack
{
	node *top;

	public:

	stack()
	{
		top = NULL;
	}

	void push (int a, char b[])
	{
		node *ptr;
		ptr = new node;
		ptr -> link = top;
		top = ptr;
		top -> roll_no = a;
		strcpy ( top -> name, b);
	}

	void pop()
	{
		if (top == NULL)
		{
			cout<< "\nUnderflow ";
		}
		else
		{
			node *ptr;
			ptr = top;
			top = top -> link;
			cout<<"\nDeleted data is: \n";
			cout<<"\nRoll No: "<<ptr -> roll_no;
			cout<<"\nName: "<<ptr -> name;
			delete ptr;
		}
	}
	void display();


};

void stack :: display()
{
	node *ptr = top;
	cout<<"\n\nThe stack is: ";
	while ( ptr!=NULL )
	{
		cout<<"\nRoll No: "<<ptr -> roll_no;
		cout<<"\tName	: "<<ptr -> name;
		ptr = ptr -> link ;
	}
}


void main()
{
	cout<<"*********************************************";
	cout<<"\nProgram to implement a stack as a linked list using a class";
	cout<<"\n*********************************************";

	int choice;

	stack a;

	cout<<"\n\n1. Push";
	cout<<"\n2. Pop";
	cout<<"\n3. Exit";
	do
	{
		cout<<"\n\nEnter your choice: ";
		cin>>choice;

		int result;

		switch (choice)
		{
			case 1: int rl;
				char nm[20];
				cout<<"\nEnter roll no: ";
				cin>>rl;
				cout<<"\nEnter name: ";
				gets (nm);
				a.push (rl, nm);

				a.display();


				break;

			case 2: a.pop();


				a.display();


				break;

			case 3: break;

			default : cout<<"\n\nInvalid choice!!! Program terminated!";
				  getch();
				  exit(0);

		}
	} while ( choice!=3 );
}