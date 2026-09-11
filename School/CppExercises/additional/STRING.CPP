//PROGRAM TO SHOW STRING OPERATIONS

#include<iostream.h>
#include<conio.h>
#include<stdio.h>


int stringlen(char*);
char* stringcpy(char* , char*);
char* stringcat(char*, char*);
char* stringrev(char*);
int stringcomp(char*, char*);

void main()
{
	char *a, *b;
	int ch;
	clrscr();


	cout<<"\nEnter 1 to find string length";
	cout<<"\nEnter 2 to compare strings";
	cout<<"\nEnter 3 to copy a string";
	cout<<"\nEnter 4 to reverse string";
	cout<<"\nEnter 5 to concatenate\n\n";

	cin>>ch;

	switch(ch)
	{
		case 1: cout<<"\nEnter a string: ";
			gets(a);

			cout<<"\n\nLength of the string is: "<<stringlen(a);
			break;

		case 2: cout<<"\n\nEnter first string: ";
			gets(a);
			cout<<"\nEnter second string: ";
			gets (b);

			if ( (stringcomp(a,b)) > 0 )
			{
				cout<<"\n\nFirst string is larger";
			}
			else if ( (stringcomp(a,b)) < 0 )
			{
				cout<<"\n\nSecond string is larger";
			}
			else
			{
				cout<<"\n\nBoth strings are equal";
			}
			break;

		case 3: cout<<"\n\nEnter first string: ";
			gets(a);
			cout<<"\n\nEnter second string: ";
			gets(b);

			stringcpy (a, b);
			cout<<"\n\nThe first string now is: ";
			puts (a);
			break;

		case 4: cout<<"\nEnter a string: ";
			gets (a);
			cout<<"\n\nThe reversed string is: ";
			puts ( stringrev(a) );
			break;

		case 5: cout<<"\n\nEnter first string: ";
			gets(a);
			cout<<"\n\nEnter second string: ";
			gets(b);

			cout<<"\n\nThe concatenated string is: ";

			puts (stringcat (a,b) );
			break;
		default: cout<<"\n\nWrong Choice ";
	}
	getch();
}

int stringlen(char a[])
{
	for(int i=0;a[i]!='\0';i++); //calculating
	return i;
}

int stringcomp(char a[], char b[])
{
	int i,j;
	int count = 0;

	i=stringlen(a);

	j=stringlen(b);

	if(i>j)
		return	1;
	else if (i<j)
		return  -1;
	else
	{
		for (int z = 0; z<i; z++)
		{
			if(a[i] == b[i])
			{
				count++;
			}
		}
		if (count == i)
		{
			cout<<"Equal";
			return 0;
		}
		else
		{
			cout<<"Not Equal";
			return 50;
		}
	}

}

char* stringcpy(char a[], char b[])
{

	for(int i=0;i<stringlen(b);i++)
		a[i]=b[i];
	return a;

}

char* stringrev(char a[])
{
	int j,k;
	char temp;

	j=stringlen(a);
	k=j;

	for(int i=0;i<j/2;i++,k--)
	{
		temp=a[i];
		a[i]=a[k-1];
		a[k-1]=temp;
	}

	return a;
}

char* stringcat(char a[], char b[])
{
	int str_a= stringlen(a) - 1;

	for(int i=0;i<stringlen(b);i++)
	{
		a[str_a + i] = b [i];
	}

	return a;
}

