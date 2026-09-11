#include<stdio.h>
#include<conio.h>
#include<ctype.h>

void main()
{
	char str[100];
	int i;
	int capitalization;
	clrscr();
	printf ("Enter a string: \n\n");
	gets (str);

	for (i=0; str [i] != '\0'; i++)
	{
		if (str [i] == '.')
		{
			if (isupper ( str[i+1] ))
			{
				capitalization = 1;
			}
			else
			{
				capitalization = 0;
			}
		}


	}

	if (islower (str[0]) )
	{
		capitalization = 0;
	}

	if (capitalization == 1)
	{
		printf ("\nThe string follows English Capitalization rules");
	}
	else
	{
		printf ("\nThe string does not follow English Capitalization rules");

	}
	getch();
}


