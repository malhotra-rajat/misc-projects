// Program to perform push and pop operations on stack

#include<iostream.h>
#include<conio.h>

void push(int stk[], int size, int top, int data)
{
	if (top == size - 1)
	{
		cout<<"OVERFLOW !!! ";
	}

	else if (top == -1)
	{
		top = 0;
		stk [top] = data;
	}
	else
	{
		top = top + 1;
		stk [top] = data;
	}
}

int pop(int stack[], int top)
{

	int data;

	if (top == -1)
	{
		cout<<"UNDERFLOW !!! ";
		return -1;
	}

	else
	{
		data = stack [top];
		top = top - 1;
		return data;
	}
}



void display(int stack[], int top)
{
	cout<<"\n\nThe stack is: \n\n";

	for (int i = top; i>=0; i--)
	{
		cout<<stack[i]<<"\n\n";
	}

}


void main()
{
	int stack [20];
	int size;
	int top = -1;
	int choice;

	clrscr();

	cout<<"Enter the no. of elements in the stack: ";
	cin>>size;

	cout<<"\n\n1. Push";
	cout<<"\n2. Pop";
	cout<<"\n3. Display Stack";

	cout<<"\n\nEnter your choice: ";
	cin>>choice;

	switch (choice)
	{
		case 1: int data;
			cout<<"\n\nEnter the data: ";
			cin>>data;
			push(stack, size, top, data);
			display (stack, top);
			break;

		case 2: cout<<"\n\nThe element deleted is"<<pop (stack, top);
			display (stack, top);
			break;

		case 3: display (stack, top);
			break;

		default : cout<<"\n\nInvalid choice! Program terminated";
	}

	getch();
}






