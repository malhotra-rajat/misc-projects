#include<stdio.h>
#include<stdlib.h>
#include<conio.h>

typedef struct nodeType
{
	char info;
	struct nodeType *next;
}stack;

typedef enum {false, true} boolean;

void createStack (stack **ps);
boolean isEmpty (stack *ps);
void push (stack **ps, char value);
char pop (stack **ps);
char peek (stack *top);
void disposeStack (stack **ps);
void infixToPostfix (char *source, char *target);
int getPriority (char op);

void main()
{
	char *infixExp = "", *postfixExp = "";
	clrscr();

	printf ("\nEnter arithemtic expression in infix notation \n\n");
	gets (infixExp);
	infixToPostfix (infixExp, postfixExp);
	printf ("\nEquivalent expression in postfix notation is\n\n");
	puts (postfixExp);
	getch();
}

void infixToPostfix (char *source, char *target)
{
	char *s, *t;
	stack *top;
	createStack (&top);
	s = source;
	t = target;

	while (*s)
	{
		if ( (*s == ' ') || (*s == '\t') )  //skip white spaces
		{
			s++;
			continue;
		}
		else if (*s == '(' )
		{
			push (&top, *s);
			s++;

		}
		else if (*s== ')' )
		{
			while ( (!isEmpty (top)) && (peek(top) != '('  ))
			{
				*t = pop (&top);
				t++;
				*t = ' '; //add one space
				t++;
			}
			if (isEmpty(top) )
			{
				printf ("\nError: Incorrect Expression\n");
				disposeStack (&top);
				exit(1);
			}
			pop (&top); //remove left parenthesis from stack
			s++;
		}
		else if ( isdigit (*s) || isalpha (*s) )
		{
			*t = *s;
			t++;
			*t = ' '; // add one space
			t++;
			s++;
		}
		else if (*s=='+' || *s== '-' ||*s=='*' ||*s=='/' ||*s=='%')
		{
			while ( (!isEmpty (top))  && (peek(top)!= '(')
			&& (getPriority (peek(top)) >= getPriority (*s) ))
			{
				*t = pop (&top);
				t++;
				*t = ' '; //add one space
				t++;
			}
			push (&top, *s);
			s++;
		}
		else
		{
			printf ("\nError: Incorrect element rin expression\n");
			disposeStack (&top);
			exit(1);
		}
	}
	while ( (!isEmpty (top)) && (peek (top) != '(' ))
	{
		*t = pop(&top);
		t++;
		*t = ' '; //add one space
		t++;
	}
	if (peek(top) == '(' )
	{
		printf ("\nError: Incorrect expression \n");
		disposeStack(&top);
		exit (1);
	}
	*t = '\0'; // terminate the string by null character
}
int getPriority (char op)
{
	int priority;

	if (op == '/' || op == '*' || op == '%')
		priority = 1;
	else if (op == '+' || op == '(' )
		priority = 0;

	return priority;
}

void createStack ( stack **top)
{
	*top = NULL;
}
boolean isEmpty (stack *top)
{
	if (top==NULL)
		return true;
	else
		return false;
}
void push (stack **top, char value)
{
	stack *ptr;
	ptr = (stack*)malloc (sizeof (stack));

	if (ptr == NULL)
	{
		printf ("\nUnable to allocate to memory for new node....");
		printf ("Press any key to exit...");
		getch();
		return;
	}
	ptr ->info = value;
	ptr ->next = *top;
	*top = ptr;
}
char pop (stack **top)
{
	char temp;
	stack *ptr;
	temp = (*top) ->info;
	ptr = *top;
	*top = (*top)->next;
	free(ptr);
	return temp;
}
char peek (stack *top)
{
	return top->info;
}
void disposeStack (stack **top)
{
	stack *ptr;
	while (*top != NULL)
	{
		ptr = *top;
		*top = (*top) ->next;
		free (ptr);
	}
}
