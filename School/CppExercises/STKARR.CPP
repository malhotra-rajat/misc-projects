#include<iostream.h>
#include<conio.h>
#include<process.h>

const int size = 10;
int stack [size];
int top = -1;

int push (int data)
{
	if (top == size - 1)
	{
		return -1;
	}
	else
	{
		top++;
		stack [top] = data;
		return 0;
	}
}

int pop ()
{
	int ret;
	if (top == -1)
	{
		return -1;
	}
	else
	{
		ret = stack [top];
		top--;
		return ret;
	}
}
void display ()
{
	if (top == -1)
	{
		cout<<"The stack is empty";
	}
	else
	{
		cout<<stack[top]<<" <--\n";
		for (int i = top-1; i>=0; i--)
		cout<< stack[i] <<endl;
	}
}

void main()
{
	cout<<"****************************************";
	cout<<"\nProgram to implement a stack as an array";
	cout<<"\n****************************************";

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
				result = push (value);

				if ( result == -1)

					cout<<"\nOVERFLOW!!! \n";

				else
				{
					cout<<" \nThe stack now is : \n";
					display();
				}

				break;

			case 2: result = pop();

				if ( result == -1)

					cout<<"\nUNDERFLOW!!!\n";

				else
				{
					cout<<"\nElement deleted is : "<<result<<endl;
					cout<<"\nThe stack now is : \n";
					display();
				}

				break;

			case 3: break;

			default : cout<<"\n\nInvalid choice!!! Program terminated!";
				  getch();
				  exit(0);

		}
	} while ( choice!=3 );

}





