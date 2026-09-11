#include<fstream.h>
#include<conio.h>
#include<stdio.h>


class clas
{
	int a ,b;

	public:

	void get()
	{
		cout<<"\nEnter a , b: ";
		cin>>a>>b;
	}

	void display()
	{
		cout<<"\na & b are: ";
		cout<<a<<" "<<b;
	}

	int ret()
	{
		return b;
	}

};


void disp();
void mod();
void del();
void add();
void search();


void main()
{
	clrscr();

	add();

       mod();






	getch();
}


void disp()
{

	ifstream file ("a.dat", ios::in | ios::binary);

	clas d;

	file.read ( (char*)&d , sizeof (d) );
	while ( file )
	{
		d.display();
		file.read ( (char*)&d, sizeof (d) );

	}

	file.close();
}

void add()
{
	clas d;

	char ans;



	ofstream file;

	file.open("a.dat", ios:: out | ios::binary | ios::trunc);

	do
	{
		d.get();
		file.write ( (char*)&d , sizeof (d) );
		cout<<"\n\nEnter more? (y,n) ";
		cin>>ans;

	} while (ans!='n');

	file.close();
}


void del()
{

	ifstream file;
	ofstream file1;

       file.open ("a.dat", ios::in | ios::binary);

       file1.open ("a1.dat", ios::out | ios::binary);

	clas d;
       int del;

       cout<<"\n\nEnter the value of b of the record u wish to delete: ";
       cin>>del;


       int found =0;



       file.read ( (char*)&d , sizeof (d) );

       while (file)
       {
	if (  del != d.ret() )
		file1.write ( (char*)&d , sizeof (d) );
	else
		found = 1;

		file.read ( (char*)&d , sizeof (d) );
	}

	file.close();

	file1.close();


	if (found == 0)

	cout<<"\n\nrecord not found";

	else

	cout<<"\n\nRecord deleted";


	remove ("a.dat");
	rename ("a1.dat", "a.dat");

}

void mod()
{
	int key;
	cout<<"\nEnter the b of the record u wish to modify: ";
	cin>>key;


	clas d;

	ifstream file;
	ofstream file1;

	file.open ("a.dat", ios::in | ios::binary);

	file1.open ("a1.dat", ios::out | ios::binary);


       int fou=0;


       file.read ( (char*)&d , sizeof (d) );



       while (file)
       {
	if (  key == d.ret() )
	{
		d.get();
		file1.write ( (char*)&d , sizeof (d) );
		fou=1;
	}
	else
	{
		file1.write ( (char*)&d , sizeof (d) );
	}

		file.read ( (char*)&d , sizeof (d) );
	}

	file.close();
	file1.close();

	if (fou == 1)
	cout<<"\nRecord updated";

	else

	cout<<"\nRecord not found";


	remove ("a.dat");
	rename ("a1.dat", "a.dat");
}
void search()
{
	ifstream file ("a.dat", ios::in | ios::binary);


	clas d;

	int user;
	cout<<"\n\n\nEnter the value of b u wish to search ";
	cin>>user;

	file.read ( (char*)&d , sizeof (d) );

	int found = 0;
	while (file)
	{
		if ( d.ret() ==  user )
		{
			d.display();
			found = 1;
			break;
		}

			file.read ( (char*)&d , sizeof (d) );

       }

       file.close();

       if (found == 0)
       cout<<"\nrecord not found";

       file.close();

}



















