//PROGRAM TO CALCULATE FACTORIAL USING POINTER TO FUNCTION

#include<iostream.h>
#include<conio.h>

int *fac(int &x)
{
int i,*f;
*f=1;

for(i=1;i<=x;i++)
{
*f=*f*i;
}

return f;
}

void main()
{
int no,*f;
clrscr();

cout<<"Enter a number to calculate its factorial: ";
cin>>no;

f=fac(no);
cout<<"Factorial of the given number is: "<<*f;
getch();
}
