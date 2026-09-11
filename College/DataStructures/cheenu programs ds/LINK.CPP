#include<stdio.h>
#include<conio.h>
#include<iostream.h>
#include<stdlib.h>


void traverse();
void insert_at_beginning (int item);
void insert_at_loc (int loc1, int item);
void insert_at_end (int item);
void delete_from_beginning();
void delete_from_end();
void delete_at_loc(int loc1);


struct node
{
	int info;
	node *next;

};
node *start = NULL;




void main()
{
	int choice, element, loc;

	while (1)
	{
		clrscr();
		printf ("1. Insert at beginning\n");
		printf ("2. Insert at end\n");
		printf ("3. Insert at location\n");
		printf ("4. Traverse\n");
		printf ("5. Delete from beginning\n");
		printf ("6. Delete from end\n");
		printf ("7. Delete at location\n");
		printf ("8. Exit\n\n");

		printf ("Enter your choice (1-8) : ");
		scanf ("%d", &choice);

		clrscr();

		switch (choice)
		{
			case 1: printf ("\nEnter element: ");
				scanf ("%d", &element);
				insert_at_beginning (element);

				break;

			case 2: printf ("\nEnter element: ");
				scanf ("%d", &element);
				insert_at_end (element);
				break;

			case 3: printf ("\nEnter location: ");
				scanf ("%d", &loc);
				printf ("\nEnter element: ");
				scanf ("%d", &element);
				insert_at_loc (loc, element);
				break;

			case 4: if (start == NULL)
					printf ("\nList is empty...");
				else
					traverse();
				printf ("\n\nPress any key to continue...");
				getch();
				break;

			case 5: delete_from_beginning();
				break;

			case 6: delete_from_end();
				break;

			case 7: printf ("\nEnter location: ");
				scanf ("%d", &loc);
				delete_at_loc (loc);
				break;
			case 8: exit(0);
		}
	}
}

void traverse ()
{
	node *ptr = start;

	while (ptr!=NULL)
	{
		cout<<ptr->info<<" -> ";
		ptr = ptr->next;
	}
}
void insert_at_beginning(int item)
{
	node *ptr;
	ptr = (node*) malloc (sizeof (node) );
	ptr->info = item;

	if (start == NULL)
		ptr->next = NULL;
	else
		ptr->next = start;
	start = ptr;
}
void insert_at_end (int item)
{
	node *ptr, *loc;
	ptr = (node*) malloc (sizeof (node));
	ptr ->info = item;
	ptr ->next = NULL;

	if (start == NULL)
		start = ptr;
	else
	{
		loc = start;
		while (loc ->next != NULL)
		loc = loc ->next;
		loc ->next = ptr;
	}
}
void insert_at_loc (int loc1, int item)
{
	node *ptr,*temp1=start;
	for (int i = 0; i<loc1-2; i++)
		temp1 = temp1 -> next;

	ptr = (node*) malloc (sizeof (node) );
	ptr -> info = item;

	ptr->next=temp1->next;
	temp1->next=ptr;
}
void delete_from_beginning()
{
	node *ptr;
	if (start == NULL)
		return;
	else
	{
		ptr = start;
		cout<<"\n\nThe element deleted is: "<<ptr->info;
		start = start -> next;
		free (ptr);
		getch();
	}

}

void delete_from_end()
{
	node *ptr, *loc;
	if (start == NULL)
		return;
	else if (start -> next == NULL)
	{
		ptr = start;
		start = NULL;
		cout<<"\n\nThe element deleted is: "<<ptr->info;
		free (ptr);
		getch();
	}
	else
	{
		loc = start;
		ptr = start ->next;
		while (ptr->next != NULL)
		{
			loc = ptr;
			ptr = ptr ->next;
		}
		loc ->next = NULL;
		cout<<"\n\nThe element deleted is: "<<ptr->info;
		free (ptr);
		getch();
	}
}

void delete_at_loc(int loc1)
{
	node *ptr=start, *loc=start;
	for (int i = 0; i<loc1-1; i++)
	{
		ptr = loc;
		loc = loc -> next;
	}
	ptr ->next = loc->next;
	cout<<"\n\nThe element deleted is: "<<ptr->info;
	free (loc);
	getch();
}


