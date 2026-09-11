#include<fstream.h>
#include<graphics.h>
#include<conio.h>
#include<ctype.h>
#include<process.h>
#include<stdlib.h>
#include<dos.h>
#include<stdio.h>



void set_graph()
{
	int driver=DETECT,mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
}


class item
{
      float price;
      int code;

      public:

      void get()
      {
	cout<<"\n\nEnter the price: Rs.";
	cin>>price;
	cout<<"\nEnter the code: ";
	cin>>code;
      }

      void disp()
      {
	cout<<"Code: "<<code;
	cout<<"\t\tPrice: "<<price;
     }
      int ret_code()
      {
	return code;
      }

      int ret_price()
      {
	return price;
      }
};

class sales
{
	public:

	float sale;
	date d;
};


void save_sales()
{

	int bill=5;




	sales a;
	date  s;
	int found=1;

	getdate (&s);

	int x=s.da_mon;
	int y=s.da_day;
	int X,Y;
	ifstream file("sales.dat",ios::in|ios::binary);
	ofstream file3 ("sales1.dat", ios::out|ios::binary);

	file.read( (char*)& a,sizeof(a));

	X= a.d.da_mon;
	Y=a.d.da_day;
	while ( !file.eof() )
	{
		if ( a.d.da_year == s.da_year && x == X && y == Y)
		{
		      a.sale+=bill;
		      found = 0;
		      file3.write ( (char*)&a, sizeof(a) );
		}

		else
		{
		      file3.write ( (char*)&a, sizeof(a) );
		}

		file.read ( (char*)& a,sizeof(a) );

	}
	file.close();
	file3.close();

	sales h;

	if (found == 1)
	{
		file3.open ( "sales1.dat", ios::out | ios::app);

		h.d.da_year = s.da_year;
		h.d.da_mon = s.da_mon;
		h.d.da_mon = s.da_mon;
		h.sale = bill;

		file3.write ( (char*)&h, sizeof (h) );
		file3.close();
	}




	remove ("sales.dat");
	rename ("sales1.dat","sales.dat");


	ifstream file2("sales.dat",ios::in|ios::binary);

	sales b;

       file2.read((char*)& b,sizeof(b));
       while (!file2.eof())
       {

	file2.read((char*)& b,sizeof(b));


	cout<<"Sales   :	"<<b.sale<<endl;
	cout<<"Date    :	";

	int i = b.d.da_day;
	int j = b.d.da_mon;

	cout<<i<<"/";

	cout<<j<<"/";

	cout<<b.d.da_year;

	cout<<"\n\n";

       }


}

void displays()
{
	ifstream file ("abc.dat", ios::in | ios::binary);


	item d;



	cout<<"The contents of the file are: \n\n";


	file.read ( (char*)&d , sizeof (d) );
	while ( file )
	{
		d.disp();
		file.read ( (char*)&d, sizeof (d) );
	}

	file.close();
}

void add()
{
	item d;

	char ans;



	ofstream file ("abc.dat", ios:: app | ios::binary );

	do
	{
		d.get();
		file.write ( (char*)&d , sizeof (d) );
		cout<<"\n\nEnter more? (y,n) ";
		cin>>ans;

	} while (ans!='n');

	file.close();
}






void create_bill()
{
       int bill = 0;


	int emp_code;


	item d;

	ifstream file;


	for (;;)
	{       clrscr();
		cleardevice();

		int found=0;
		settextstyle(4,HORIZ_DIR,3);
		outtextxy (380, 400, "Total Bill:  Rs. ");

		//convert bill to string and display

		char string[10];
		itoa(bill,string,10);
		outtextxy (550, 400, string);


		setbkcolor(RED);
		settextstyle(4,HORIZ_DIR,4);
		outtextxy(150,20,"CAFE COFFEE DAY");
		line (0, 80, 640, 80);
		line (350, 80, 350, 500);
		line (350, 380, 680, 380);


		gotoxy ( 4, 8);


		cout<<"Enter the code of the item you";
		gotoxy ( 4, 9);
		cout<<"wish to add to the price list: ";
		cin>>emp_code;

		if (emp_code == 0 )
		break;

		file.open ( "abc.dat", ios::in | ios ::binary );

		file.read ( (char*)&d, sizeof(d) );

		while (  file )
		{

		if ( d.ret_code() == emp_code )
		{

			bill= bill + d.ret_price();
			found = 1;

			break;
		}




		file.read ( (char*)&d, sizeof(d) );

		}

	if ( found == 0 )
	{
		gotoxy ( 4, 11);
		cout<<"Item not found";
		getch();
	}

		file.close();


 }

}

void main()
{


	clrscr();


	save_sales();


	getch();


}






