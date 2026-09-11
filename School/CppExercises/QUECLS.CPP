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

class queue
{
	node *front, *rear;


	public:

	queue()
	{
		front = NULL;
		rear = NULL;
	}
	void insert (int val, char a[20])
	{
		node *ptr;
		ptr = new node;
		ptr -> roll_no = val;
		strcpy (ptr -> name,a);
		ptr -> link = NULL;

		if (front == NULL)
		{
			front = rear = ptr;
		}
		else
		{
			rear -> link = ptr;
			rear = ptr;
		}

	}

	void remove()
	{
		if (front == NULL)
		{
			cout<<"\n\nUnderflow!!!";
		}
		else
		{
			node *ptr;
			ptr = front;
			front = front -> link;
			cout<<"\n\nThe deleted data is: \n\n";
			cout<<"Roll No: "<<ptr -> roll_no;
			cout<<"\tName: "<<ptr -> name;
			delete ptr;
		}
	}

	void display();


};

void queue :: display()
{
	node *ptr = front;
	cout<<"\n\nThe queue is: ";
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
	cout<<"\nProgram to implement a queue as a linked list using a class";
	cout<<"\n*********************************************";

	int choice;

	queue a;

	cout<<"\n\n1. Insert";
	cout<<"\n2. Delete";
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
				a.insert (rl, nm);

				a.display();


				break;

			case 2: a.remove();


				a.display();


				break;

			case 3: break;

			default : cout<<"\n\nInvalid choice!!! Program terminated!";
				  getch();
				  exit(0);

		}
	} while ( choice!=3 );
}