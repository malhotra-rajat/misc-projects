#include<iostream.h>
#include<conio.h>
#include<process.h>

struct node
{
	int data;
	node *link;
};

node *rear = NULL;
node *front = NULL;

void insert (int val)
{
	node *ptr;
	ptr = new node;
	ptr -> data = val;
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
		cout<<"\n\nThe deleted data is: "<<ptr -> data;
		delete ptr;
	}
}
void display()
{
	node *p1 = front;
	while (p1 != NULL)
	{
		cout<<p1->data<<" -> ";
		p1 = p1 -> link;
	}
}

void main()
{
	cout<<"*********************************************";
	cout<<"\nProgram to implement a queue as a linked list";
	cout<<"\n*********************************************";

	int choice;

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
			case 1: int value;
				cout<<"\nEnter value for insertion: ";
				cin>>value;
				insert (value);
				cout<<" \n\nThe queue (front to rear) now is : \n";
				display();


				break;

			case 2: remove();

				cout<<"\n\nThe queue (front to rear) now is : \n";
				display();

				break;

			case 3: break;

			default	: cout<<"\n\nInvalid choice!!! Program terminated!";
				  getch();
				  exit(0);

		}
	} while ( choice!=3 );
}