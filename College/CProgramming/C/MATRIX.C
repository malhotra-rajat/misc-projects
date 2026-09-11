#include<stdio.h>
#include<conio.h>

int A[10][10], B[10][10], C[10][10];
int i, j;
int m, n, o, p;

void inputA();
void inputAB();
void displayC();
void displayA();
void add();
void subtract();
void trace();
void transpose();
void multiply();


void main()
{
	int choice;

	clrscr();

	printf ("----\nMenu\n----\n\n");

	printf ("1. Addition of two matrices\n");
	printf ("2. Subtaction of two matrices\n");
	printf ("3. Trace of a matrix\n");
	printf ("4. Transpose of a matrix\n");
	printf ("5. Product of two matrices\n\n");

	printf ("Enter your choice: ");
	scanf("%d", &choice);
	printf ("\n");

	switch (choice)
	{
		case 1:  inputAB();
			 add();
			 displayC();
			 break;

		case 2:  inputAB();
			 subtract();
			 displayC();
			 break;

		case 3:  inputA();
			 trace();
			 break;

		case 4:  inputA();
			 transpose();
			 displayC();
			 break;

		case 5:  inputAB();
			 multiply();
			 displayC();
			 break;

		default: printf ("Wrong choice....Goodbye");


	}
	printf ("\n\nEnter any key to exit...");
	getch();
}

void inputA()
{

	printf ("Enter the number of rows: ");
	scanf ("%d", &m);
	printf ("\nEnter the number of columns: ");
	scanf ("%d", &n);
	printf ("\nEnter the matrix: \n\n");

	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			scanf ("%d",&A[i][j]);

		}
	}
}

void inputAB()
{

	printf ("\nEnter the number of rows: ");
	scanf ("%d", &m);
	printf ("\nEnter the number of columns: ");
	scanf ("%d", &n);


	printf ("\n1st matrix");
	printf ("\n----------");
	printf ("\nEnter the matrix: \n\n");

	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			scanf ("%d",&A[i][j]);
		}
	}

	printf ("\n2nd matrix");
	printf ("\n----------");

	printf ("\nEnter the matrix: \n\n");
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			scanf ("%d",&B[i][j]);
		}
	}

}

void displayC()
{
	printf ("\n\nThe matrix is: \n\n");
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			printf ("%d ",C[i][j]);
		}
		printf ("\n");
	}
}

void displayA()
{
	printf ("\nThe matrix is: \n\n");
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			printf ("%d ",A[i][j]);
		}
		printf ("\n");
	}
}




void add()
{
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			C[i][j] = A[i][j] + B[i][j];
		}

	}
}

void subtract()
{
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			C[i][j] = A[i][j] - B[i][j];
		}

	}
}

void trace()
{
	int trace = 0;

	for (i=0; i<m; i++)
	{
		trace = trace + A[i][i];

	}
	printf ("\nTrace is: %d", trace);
}

void transpose()
{
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			C[i][j] = A[j][i];
		}

	}

}

void multiply()
{
	int sum, k;
	for (i=0; i<m; i++)
	{
		for (j=0; j<n; j++)
		{
			sum = 0;
			for (k=0; k<=2; k++)
			{
				sum = sum + A[i][k] * B[k][j];
				C[i][j] = sum;
			}
		}
	}
}

