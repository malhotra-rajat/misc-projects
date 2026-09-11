#include<iostream.h>
#include<conio.h>
#include<process.h>


struct node
{
	int data;
	node *link;
};

node *top = NULL;

void push (int val)
{
	node *ptr;
	ptr = new node;
	ptr -> link = top;

	top = ptr;
	ptr -> data = val;
}
void pop()
{
	if (top == NULL)
	{
		cout<<"\n\nUnderflow!!!";
	}
	else
	{
		node *ptr;
		ptr = top;
		top = top -> link;
		cout<<"\n\nThe deleted data is: "<<ptr -> data;
		delete ptr;
	}
}
void display()
{

	node *ptr = top ;
	while (ptr != NULL)
	{

		cout<<ptr->data<<" -> ";
		ptr = ptr -> link;
	}
}

void main()
{
	cout<<"*********************************************";
	cout<<"\nProgram to implement a stack as a linked list";
	cout<<"\n*********************************************";

	int choice;

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
			case 1: int value;
				cout<<"\nEnter value for insertion: ";
				cin>>value;
				push (value);
				cout<<" \n\nThe stack now is : \n";
				display();


				break;

			case 2: pop();

				cout<<"\n\nThe stack now is : \n";
				display();


				break;

			case 3: break;

			default : cout<<"\n\nInvalid choice!!! Program terminated!";
				  getch();
				  exit(0);

		}
	} while ( choice!=3 );
}