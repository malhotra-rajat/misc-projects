//PROGRAM TO SHOW STRING OPERATIONS

#include<iostream.h>
#include<conio.h>

int stringlen(char a[]);
void stringcpy(char a[]);
void stringcat(char a[]);
void stringrev(char a[]);
void stringcomp(char a[]);

void main()
{
	char a[20];
	int ch;
	clrscr();

	cout<<"Enter a string: ";
	cin>>a;

	cout<<"\nEnter 1 to find string length";
	cout<<"\nEnter 2 to compare strings";
	cout<<"\nEnter 3 to copy a string";
	cout<<"\nEnter 4 to reverse string";
	cout<<"\nEnter 5 to concatenate\n\n";

	cin>>ch;

	switch(ch)
	{
		case 1: cout<<"\n\nLength of the string is: "<<stringlen(a);
			break;

		case 2: stringcomp(a);
			break;

		case 3: stringcpy(a);
			break;

		case 4: stringrev(a);
			break;

		case 5: stringcat(a);
			break;

		default: cout<<"Wrong Choice ";
	}
	getch();
}

int stringlen(char a[])
{
	for(int i=0;a[i]!='\0';i++); //calculating
	return i;
}

void stringcomp(char a[])
{
	int i,j;
	char b[20];

	i=stringlen(a);

	cout<<"\n\nEnter Another String: ";
	cin>>b;

	j=stringlen(b);

	if(i>j)
		cout<<"First String is larger ";
	else
		cout<<"Second String is larger ";
}

void stringcpy(char a[])
{
	char b[20];

	for(int i=0;i<stringlen(a);i++)
		b[i]=a[i];

	cout<<"\n\nCopied String is: ";

	for(i=0;i<stringlen(a);i++)
		cout<<b[i];
}

void stringrev(char a[])
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

	cout<<"\n\nReversed String is: "<<a;
}

void stringcat(char a[])
{
	char b[20],c[40];

	cout<<"\n\nEnter Another String: ";
	cin>>b;

	for(int i=0;i<stringlen(a);i++)
	{
		c[i]=a[i];
	}

	for(int j=0,k=i;j<stringlen(a);k++,j++)
	{
		c[k]=b[j];        //copying another string
	}

	cout<<"\n\nConcatenated string is: "<<c;
}

