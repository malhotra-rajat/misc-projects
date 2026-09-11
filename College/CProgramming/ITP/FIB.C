 #include<stdio.h>
 #include<conio.h>

 int fib (int n);
 void main()
 {
  clrscr();

  getch();

 }
  int fib (int n)
  {
      if ( n == 1 || n == 0)
	  return n;
      else
	 return  fib(n-1) + fib(n-2) ;
  }
