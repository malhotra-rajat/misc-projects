#include<iostream.h>
#include<conio.h>
#include<process.h>

const int size = 10;
int queue [size];
int front = -1, rear = -1;

int insert (int data)
{
	if (rear == size - 1)
	{
		return -1;
	}
	else if (rear == -1)
	{
		front = rear = 0;
		queue [rear] = data;
	}
	else
	{
		rear++;
		queue [rear] = data;
	}
	return 0;
}

int remove()
{
	int ret;
	if (front == -1)
	{
		return -1;
	}
	else
	{
		ret = queue [front];

		if (front == rear)

			front = rear = -1;
		else
			front++;
	}


	return ret;
}
void display ()
{
	if (front == -1)
	{
		cout<<"The queue is empty";
	}
	else
	{
		for (int i = front; i<rear ; i++)
		cout<<queue[i]<<"  <-\t";
		cout<<queue[rear]<<endl;
	}
}

void main()
{
	cout<<"****************************************";
	cout<<"\nProgram to implement a queue as an array";
	cout<<"\n****************************************";

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
				result = insert (value);

				if ( result == -1)

					cout<<"\nOVERFLOW!!! \n";

				else
				{
					cout<<" \nNow the queue (Front....to....rear) is : \n";
					display();
				}

				break;

			case 2: result = remove();

				if ( result == -1)

					cout<<"\nUNDERFLOW!!!\n";

				else
				{
					cout<<"\nElement deleted is : "<<result<<endl;
					cout<<"\nNow the queue (Front....to....rear) is : \n";
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

