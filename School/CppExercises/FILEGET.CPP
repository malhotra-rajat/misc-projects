#include<fstream.h>
#include<conio.h>


void main()
{
	clrscr();

	fstream file;

	file.open ("std.dat", ios::out, ios::binary);


	cout<<"Enter the contents of the file\n";

	char c;
	int x;
	do
	{
		cin>>c;

		file.put(c);

		x=c;


	} while (x!=13);

	file.close();

	file.open ("std.dat", ios::in, ios::binary);

	char a;

	cout<<"\nThe contents of the file are: ";


	while ( ! file.eof() )
	{
		file. get (a);

		cout<<a;
	}

	getch();
}


