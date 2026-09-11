#include<fstream.h>
#include<conio.h>
#include<stdlib.h>

void file_creator(int size,char file_name[])
{
	ofstream fout;
	fout.open(file_name);
	if(!fout)
	{
		cout<<file_name<<" cannot be opened!!";
		exit(0);
	}

	for(int i=0;i<size;i++)
	{
		int no=random(32767);
		fout<<no<<"\n";
	}
	cout<<file_name<<" created with "<<size<<" elements.\n";
	fout.close();
}

void file_read(char file_name[])
{
	ifstream fin;
	fin.open(file_name);
	if(!fin)
	{
		cout<<file_name<<" cannot be opened!!";
		exit(0);
	}

	while(fin)
	{
		int no;
		fin>>no;
		cout<<no<<" ";
	}
}

void main()
{
	clrscr();
	file_creator(5000,"FILE1.txt");
	file_creator(10000,"FILE2.txt");
	file_creator(15000,"FILE3.txt");
	file_creator(20000,"FILE4.txt");

	file_read("FILE1.txt");
	cout<<"\n";
	getch();

	file_read("FILE2.txt");
	cout<<"\n";
	getch();

	file_read("FILE3.txt");
	cout<<"\n";
	getch();

	file_read("FILE4.txt");
	getch();
}