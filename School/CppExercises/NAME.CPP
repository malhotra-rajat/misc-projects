#include<graphics.h>
#include<conio.h>
#include<dos.h>
#include<process.h>

void set_graph()
{
	int driver=DETECT,mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
}
	float x;
	float y;
	float z=0.2;



void main()
{
	set_graph();

	setbkcolor(WHITE);
	setcolor(RED);



       if (getch()==13)
       {

	do
	{

	  //letter p
	   for ( x=25, y=100; y<400; )
	   {
		y+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;

	   }


	   for (x=25, y=100;x<85;)
	   {
		x+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (;y<200;)
	   {

		y+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (;x>25;)
	   {

		x-=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	    //letter r
	   for ( x=115, y=100; y<400; )
	   {
		y+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=115, y=100;x<175;)
	   {
		x+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (;y<200;)
	   {

		y+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (;x>115;)
	   {
		x-=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (; x<175,y<400;)
	   {
		x=x+0.6;
		y+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   //letter o
	   for ( x=205, y=100; y<400; )
	   {
		y+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=205, y=100;x<265;)
	   {
		x+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (  y=100; y<400; )
	   {
		y+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for ( ; x>205; )
	   {
		x-=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   //the letter j

	   for ( x=295, y=100; x<355; )
	   {
		x+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for ( x=325, y=100; y<400; )
	   {
		y+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for ( ; x>295; )
	   {
		x-=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for ( ; y>350; )
	   {
		y-=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   //the letter e
	   for (x=385 , y=100; y<400; )
	   {
		y+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=385,y=100 ; x<445; )
	   {
		x+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=385,y=250 ; x<445; )
	   {
		x+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=385,y=400 ; x<445; )
	   {
		x+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   //letter c
	   for (x=475,y=100 ; x<535; )
	   {
		x+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=475,y=400 ; x<535; )
	   {
		x+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=475, y=100; y<400; )
	   {
		y+=2;
		delay(1);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   //letter t

	   for (x=560,y=100; x<625; )
	   {
		x+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }

	   for (x=595,y=100; y<400; )
	   {
		y+=2;
		delay(8);
		circle(x,y,z);
		if ( kbhit() )
		break;
	   }



     } while ( !kbhit() );

    }
}



