#include<stdio.h>
#include<conio.h>
#include<string.h>


void main()
{
	char str [100], substr [100];
	int i, j,l, x=0, loc, m,g, o;

	clrscr();

	printf ("Enter a string: ");
	gets (str);

	printf ("Enter the substring you wish to delete from the entered string: ");
	gets (substr);

	l = strlen (substr);
	m = strlen (str);

	for (i=0, j=0; str[i]!='\0'; i++)
	{
		if (substr [0] == str [i])
		{


			while (j<l)
			{
				if (substr [j] == str [i])
				{
				       x = x + 1;

				}
				j=j+1;
				i=i+1;
			}

		}
		if (x==l)
		break;
	}


	loc = i - l;

	if (x==l)
	{
		printf ("\n\nSubstring found");

		for (g=1; g<=l; g++)
		{
			o=loc;
			while (o < m)
			{
				str [o] = str [o+1];
				o = o+1;
			}
			m=m-1;
		}
		printf("\n\nThe new string is: %s", str);
	}


	else
	{
		printf ("\nSubstring not found");
	}



	getch();
}
























