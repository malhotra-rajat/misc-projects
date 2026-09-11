//TO SHOW STACK OPERATIONS

#include<iostream.h>
#include<stdio.h>
#include<conio.h>

class stack
{
	int stack[10];
	int top;

	public:
		int push();
		void pop();
		void traverse();

		stack()             //constructor
		{
			top=-1;
		}
}s;

int stack::push()
{
	int item;

	if(top==9)
	{
		cout<<"\n\nOverFlow!!! Stack Is Full";
		return 0;
	}
	else
	{
		cout<<"\n\nEnter the element to be inserted: ";
		cin>>item;
		top=top+1;
		stack[top]=item;
		return 1;
	}
}

void stack::pop()
{
	int item;

	if(top==-1)         //Empty Stack
	{
		cout<<"\n\nUnder Flow!!! Stack Is Empty ";
	}
	else
	{
		item=stack[top];
		top=top-1;

		cout<<item<<" is deleted";
	}
}

void stack::traverse()
{
	int i;

	if(top==-1)
		cout<<"\n\nThe stack is empty";
	else
	{
		cout<<"\n\nOn traversing...\n\n";

		for(i=0;i<=top;i++)
			cout<<stack[i]<<"-->";
	}
}

void main()
{
	int choice;
	char ch;

	do
	{
		clrscr();

		cout<<"1.Enter 1 to Push\n";
		cout<<"2.Enter 2 to Pop\n";
		cout<<"3.Enter 3 to Traverse\n";
		cout<<"\nEnter your choice: ";

		cin>>choice;

		switch(choice)
		{
			case 1: s.push();     //to push element
				s.traverse();
				break;
			case 2: s.pop();     //to pop element
				s.traverse();
				break;
			case 3: s.traverse(); //to see all elements
				break;
			default: cout<<"Wrong choice\n";
		}

		cout<<"\nWant To Continue ? (y/n):  ";
		cin>>ch;

		} while(ch=='y'||ch=='Y');

	getch();
}


