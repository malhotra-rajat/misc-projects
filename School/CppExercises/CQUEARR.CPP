#include<iostream.h>
#include<conio.h>
#include<process.h>

const int size = 3;
int cqueue [size];
int front = -1, rear = -1;

int insert (int data)
{
	if (front == rear+1 || front == 0 && rear == size-1)
	{
		return -1;
	}
	else if (front == -1)
	{
		front = rear = 0;
		cqueue [rear] = data;
	}
	else
	{
		if (rear == size -1)
		{
			rear = 0;
		}
		else
		{
			rear++;
		}
		cqueue [rear] = data;
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
		ret = cqueue [front];

		if (front == rear)

			front = rear = -1;
		else
		{
			if (front == size-1)
			{
				front = 0;
			}
			else
			{
				front = front + 1;
			}
		}

	}


	return ret;
}
void display ()
{
	int i = 0;

	if (front == -1 )
	{
		cout<<"The queue is empty";
	}
	if ( rear >= front )
	{
		for ( i=front; i<rear; i++)
		cout<<cqueue[i]<<" <- ";
		cout<<cqueue[rear]<<endl;
	}
	else
	{
		for (i = 0; i<rear; i++)
			cout<<cqueue[i]<<" <- ";
			cout<<cqueue[rear];
		for ( i= front; i<size; i++)
			 cout<<" <- "<<cqueue[i];
		cout<<"\t...wrap around...";
	}



}

void main()
{
	cout<<"*************************************************";
	cout<<"\nProgram to implement a circular queue as an array";
	cout<<"\n*************************************************";

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
					cout<<" \nNow the circular queue (Front....to....rear) is : \n";
					display();
				}

				break;

			case 2: result = remove();

				if ( result == -1)

					cout<<"\nUNDERFLOW!!!\n";

				else
				{
					cout<<"\nElement deleted is : "<<result<<endl;
					cout<<"\nNow the circular queue (Front....to....rear) is : \n";
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

