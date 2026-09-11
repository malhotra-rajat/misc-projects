#include<graphics.h>
#include<conio.h>
#include<dos.h>
#include<iostream.h>



void set_graph()  //function for setting up graphics mode
{
	int driver=DETECT,mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");

}

void main()
{
	set_graph();
	int x=200,y=200;
	setbkcolor(WHITE);
	setcolor(RED);

	beg:

	circle (x, y, 20);

	 getch();


	if ( kbhit() )
	{
		char k = getch();


		if (k == 77 )
		{
			for (;;)
		{


				cleardevice();

				circle (x, y, 20);

				x+=8;

				delay(30);

				if (x>600||y>400)

					break;

				if ( kbhit () )

					goto beg;

		  }
		}
		else if (k == 75 )
		{
			for (;;)
		{


				cleardevice();

				circle (x, y, 20);

				x-=8;

				delay(30);

				if (x>600||y>400)

					break;
				if (kbhit () )

					goto beg;
		     }
		   }

		   else if (k == 72 )
		{
			for (;;)
		{


				cleardevice();

				circle (x, y, 20);

				y-=8;

				delay(30);

				if (x>600||y>400)

					break;
				if (kbhit () )

					goto beg;
			      }
		   }

		   else if (k == 80 )
		{
			for (;;)
		{


				cleardevice();

				circle (x, y, 20);

				y+=8;

				delay(30);

				if (x>600||y>400)

					break;
				if (kbhit () )

					goto beg;
		    }
		   }






	   }

}

