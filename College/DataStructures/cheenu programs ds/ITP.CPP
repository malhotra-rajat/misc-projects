// the program is used to convert a infix expression to a postfix expression

#include<iostream.h>
#include<stdio.h>
#include<conio.h>
#include<string.h>
#include<stdlib.h>



const int size =50;
char infix[size],postfix[size],stack[size];
int top=-1;

int precedence(char ch);   // function to get the precedence of the operator
char pop();  //function to pop an element from the stack
char topelement();  // returns the top element of the stack
void push(char ch);  // pushes an element into the stack



void main()
{
     char ele,elem,st[2];
     int prep,pre,popped,j=0;
     strcpy(postfix," ");
     clrscr();
     cout<<"Enter the infix expression: ";

     gets(infix);

     for(int i=0;infix[i]!=0;i++)
     {
	if(infix[i]!='('&&infix[i]!=')'&&infix[i]!='^'&&infix[i]!='*'&&infix[i]!='/'&&infix[i]!='+'&&infix[i]!='-')
		postfix[j++]=infix[i];
	else if(infix[i]=='(')
	{
		elem=infix[i];
		push(elem);
	}
	else if(infix[i]==')')
	{
		popped = pop();
		while(popped != '(')
			postfix[j++]=popped;
	}
	else
	{
		elem=infix[i];
		pre=precedence(elem);//stores the precedence of operator coming frm infix
		ele=topelement();
		prep=precedence(ele);//stores the precedence of operator at the top of the stack

		if(pre > prep)
			push(elem);
		else
		{
			while(prep >= pre)
			{
				if(ele=='#')
					break;
				popped=pop();
				ele=topelement();
				postfix[j++]=popped;
				prep=precedence(ele);
			}
			push(elem);
		}
	}
     }

     while((popped=pop())!='#')
	postfix[j++]=popped;
     postfix[j]='\0';

     cout<<"\n\nThe postfix expression is: "<<postfix<<endl;

     getch();

}

int precedence(char ch)
{
       switch(ch)
	  {
	       case '^' : return 5;
               case '/' : return 4;
               case '*' : return 4;                                            
               case '+' : return 3;
               case '-' : return 3;
               default  : return 0;
          }
}

char pop()                  //function to pop the element from the stack
{
     char ret;
     if(top!=-1)
       {  ret =stack[top];
          top--;
          return ret;
       }
     else
        return '#';
}
                         
char topelement()          // function to return top element from the stack without popping
{     
      char ch;
      if(top!=-1)
        ch=stack[top];
      else
         ch='#';
       return ch;
}

void push(char ch)          // function to push an element in the stack
{
     if(top!=size-1)
         {
            top++;
            stack[top]= ch;
         }
}         
                                
