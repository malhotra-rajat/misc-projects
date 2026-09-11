#include<stdio.h>
#include<conio.h>
#include<math.h>

int x, y, a, b, u, v;


void add();
void subtract();
void divide();
void multiply();
void display();


void main()
{
	int choice;

	clrscr();

	printf ("Enter the first complex number (x + yi): ");
	scanf("%d%d",&x,&y);
	printf ("\nEnter the second complex number (a + bi): ");
	scanf("%d%d",&a,&b);


	printf ("\n\n1. Add\n");
	printf ("2. Subtract\n");
	printf ("3. Divide\n");
	printf ("4. Multiply");

	printf ("\n\nEnter your choice: ");
	scanf ("%d", &choice);



	switch(choice)
	{
		case 1: add();
			display();
			break;
		case 2: subtract();
			display();
			break;
		case 3: multiply();
			display();
			break;
		case 4: divide();
			display();
			break;

		default: printf("Invalid choice... Goodbye...");
	}
	printf ("\n\nPress any key to exit...");
	getch();
}

void add()
{
	u = x + a;
	v = y + b;
}
void subtract()
{
	u = x - a;
	v = y - b;
}

void divide()
{
	u = ((x*a) + (b*y)) / (pow (a,2) + pow (b,2));
	v = ((a*y) - (b*x)) / (pow (a,2) + pow (b,2));;
}

void multiply()
{
	u = (a*x) - (b*y);
	v = (x*b) + (a*y);
}

void display()
{
	printf ("\n\nThe result is: %d + %di", u, v);
}








