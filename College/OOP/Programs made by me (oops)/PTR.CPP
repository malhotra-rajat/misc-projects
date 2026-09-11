#include<iostream.h>
#include<conio.h>



class A
{
	int a;
	static int count;


	public:

	A()
	{
		++count;
	}

	static int get_count()
	{
		return count;
	}

};




void  main()
{
	A *ptr[10];
	int n = 0;
	do
	{
		ptr[n]  = new A;

		n++;

	}while ((ptr[0] -> get_count() )<=2 );

	getch();
}

