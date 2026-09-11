#include<iostream.h>
#include<conio.h>
#include<graphics.h>
#include<dos.h>
#include<process.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<time.h>
#include<string.h>

void set_graph()
{
	int driver=DETECT,mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
}

void intro()
{
	int maxy=getmaxy();
	setbkcolor(14);
	setcolor(RED);
	settextstyle(4,HORIZ_DIR,4);
	outtextxy(150,(maxy/2)-200,"CAFE COFFEE DAY");
	outtextxy(200,(maxy/2)-50,"PASSWORD");
	rectangle(200,(maxy/2),390,(maxy/2+30));

}

void emp_screen()
{
	int maxy=getmaxy();
	setbkcolor(14);
	setcolor(RED);
	settextstyle(4,HORIZ_DIR,4);
	outtextxy(150,(maxy/2)-200,"CAFE COFFEE DAY");

	outtextxy(230,(maxy/2)-50,"Create Bill");
	outtextxy(230,(maxy/2), "Instructions");


}

void owner_screen()
{
	int maxy=getmaxy();
	setbkcolor(14);
	setcolor(RED);
	settextstyle(4,HORIZ_DIR,4);
	outtextxy(150,(maxy/2)-200,"CAFE COFFEE DAY");
	outtextxy(180,(maxy/2)-50,"Change Password");
	outtextxy(180,(maxy/2)-10,"View Rate List");
	outtextxy(180,(maxy/2)+30,"ABC");

}


void password()
{
	int maxy=getmaxy();
	char pw[10]={"owner"};
	char pwi[10]={"employee"};

	int i=0;

	char p[10],x;

	intro();

	for(;;)

	{
	   x=getch();

	   if( x!=13 && x!=8 && i<9 && x!=72 && x!=75 && x!=77 && x!=80)
	   {
	     p[i]=x;
	     i++;
	     outtextxy(185+(20*i),(maxy/2)-10 ,"*");
	   }

	   else

	   if( x==8 && i>0)
	   {
		cleardevice();
		intro();
		i--;

		for(int f=0 ; f<i ;)
		{
			f++;
			outtextxy(185+(20*f),(maxy/2)-10 ,"*");
		}
	   }

	   else

	   if(x==13)
	   {
		p[i]='\0';
		break;
	    }
	    else
	    {}

	}

       if ( strcmpi(p,pw) == 0 )
       {
	  outtextxy(200,maxy/2+50,"Done");
	  outtextxy(200,maxy/2+100,"Owner");
       }
       else

       if ( strcmpi(p,pwi) == 0 )
       {
	   outtextxy(200,maxy/2+50,"Done");
	   outtextxy(200,maxy/2+100,"Employee");
       }
       else

	  outtextxy(60,maxy/2+50,"Wrong Password! Program Terminated");

}

/*
void add_item ()
{
	ofstream ofile ("Rate_list.dat", ios::out | ios::binary);
	item a1;




	char ans;
	do
	{
		a1.get_data();
		ofile.write( (char*)&a1 , sizeof(a1) );
		cout<<"Add more items ? (y/n) ";
		cin>>ans;
	}while (ans!='n');

	ofile.close();



}

void display_rlist()
{

      ifstream ifile("Rate_list.dat", ios::in | ios::binary);
      item a1;

	 file.read( (char*)&a1 , sizeof(a1) );
      while ( !ifile.eof() )

	{
		a1.show_data();
		file.read( (char*)&a1 , sizeof(a1) );

	}

	ifile.close();
}

void delete_item()
{
       ifstream ifile("Rate_list.dat", ios::in | ios::binary);
       fstream  file ("mod_rate.dat", ios::trunc | ios:: out | ios:: in | ios::binary);

       int key;
       item a1;


       cout<<"Enter the code of the item to be deleted: ";
       cin>>key;

       ifile.read ( (char*)&a1, sizeof (a1) );

       int found = 0;

       while ( !ifile.eof() )
       {
	     if (key != a1.ret_code() )
	     {
		file.write( (char*)&a1 , sizeof(a1) );
	     }

	     else

	     found = 1;



       }

       if (found == 1)

       cout<<"\nItem deleted ! ";

       cout<<"New rate list is ";
       gap(2);

       display_rlist();

}


void modify_item ()
{

	 fstream  file ("rate_list.dat", | ios:: out | ios:: in | ios::binary);

	int key;
       item a1;


       cout<<"Enter the code of the item to be modified: ";
       cin>>key;

       file.read ( (char*)&a1, sizeof (a1) );

       int found = 0;

       while ( !ifile.eof() )
       {

	     if (key == a1.ret_code() )
	     {
		a1.get_data();
		file.write( (char*)&a1 , sizeof(a1) );
	     }

       }



       cout<<"New rate list is ";
       gap(2);

       display_rlist();


}
*/

void cursor()
{
     owner_screen();
     int maxy = getmaxy();
     int y=(maxy/2)-10;
     int k;
     do
	   {
		k = getch();

		 if ( k == 72 )
		 {
			cleardevice();
			if ( y > (maxy/2)-50 )
			y=y-40;
			outtextxy(150,y,"*");
			owner_screen();


		 }

		else if ( k == 80 )

		 {
			cleardevice();
			if ( y < (maxy/2)+30 )
			y=y+40;
			outtextxy(150,y,"*");
			owner_screen();


		  }
		  else{}




    }	while(k !=27);
}

void main()
{

	set_graph();
	password();
	getch();

}