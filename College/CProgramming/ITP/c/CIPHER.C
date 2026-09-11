#include<stdio.h>
#include<conio.h>
#include<math.h>


void main()
{
	char str[100];
	int i;
	clrscr();
	printf ("Enter a string: \n\n");
	gets (str);

	for (i=0; str [i] != '\0'; i++)
	{
		str [i] = str [i] + 2;
	}
	printf ("\nThe ciphered string is: \n\n%s", str);

	getch();
}


