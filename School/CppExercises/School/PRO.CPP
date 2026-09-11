#include<iostream.h>
#include<conio.h>
#include<graphics.h>
#include<dos.h>
#include<process.h>
#include<stdlib.h>
#include<math.h>
#include<stdio.h>
#include<time.h>

void set_graph()
{
	int driver=DETECT,mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
}

void background()
{
	int maxx=getmaxx();
	int maxy=getmaxy();
	setbkcolor(7);
	setcolor(RED);
	rectangle(8,8,maxx-8,maxy-8);
	settextstyle(5,HORIZ_DIR,1);
	outtextxy((maxx/2)-15,1,"PRESS THE ENTER KEY TO EXIT");
}

void intro()
{
	int maxy=getmaxy();
	setbkcolor(WHITE);
	setcolor(RED);
	settextstyle(7,HORIZ_DIR,1);
	outtextxy(250,(maxy/2)-200,"Hello!");
	outtextxy(15,(maxy/2)-150,"The game you are about to play is created by Rajat Malhotra ");
	outtextxy(15,(maxy/2)-125,"of Class XI-B.");
	outtextxy(15,(maxy/2)-50,"The game is about an alien (a.k.a THE DUDE) with no legs & ");
	outtextxy(15,(maxy/2)-25,"no hands who has been left by the people of planet A**.");
	outtextxy(15,(maxy/2),"He is starving. Please guide him to the food and save him.");
	settextstyle(7,HORIZ_DIR,3);
	outtextxy(15,(maxy/2)+50,"Press the keys A-D and W-S for left-right ");
	outtextxy(15,(maxy/2)+75,"and up-down movement respectively.");
	outtextxy(15,(maxy/2)+120,"Score 230 points & become the ultimate champion.");
	outtextxy(15,(maxy/2)+150,"Press any key to start the game.");
	getch();
}

void character(int x,int y,int z)
{
       circle(x,y,z);
       setcolor(4);
       circle(x-8,y-5,z-20);
       circle(x+9,y-5,z-20);
       circle(x,y+10,z-20);
       circle(x-32,y-5,z-15);
       circle(x+32,y-5,z-15);
       line(x-27,y-5,x-20,y-5);
       line(x+20,y-5,x+27,y-5);
}

void show_food(int p,int m)
{

	const int c=2;
	circle(p,m,c);
}



void countdown(int a)
{
	char stringa[8];
	itoa(a,stringa,10);
	outtextxy(200,1,stringa);
}



void main()
{
	set_graph();
	intro();
	cleardevice();
	background();

	char direction;
	const char right_key='d',left_key='a',up_key='w',down_key='s';
	char keypress;

	int i=getmaxx()/2,j=getmaxy()/2,r=20;
	const int speed=18;
	int x=300,y=300;
	int o=90;
	int score=0;
	int m=500;
	int g=getmaxx()/2;
	int h=getmaxy()/2;

	character(i,j,r);

	while(keypress!='\r')
	{
	if (kbhit())
		{
			keypress=(char)getch();
			direction=keypress;
			switch(direction)

		       {

			case(left_key):
			cleardevice();
			character(i,j,r);
			i=i-speed;
			background();
			show_food(x,y);
			float d=sqrt(pow(x-i,2)+pow(y-j,2));
			if(d<20)
			{
				sound(o);
				x=20+random(550);
				y=50+random(400);
				nosound();
				score+=10;

			}
			char string[8];
			itoa(score, string, 10);
			settextstyle(6,HORIZ_DIR,2);
			outtextxy(15,1,"SCORE: ");
			outtextxy(80,1,string);

			countdown(m);
			m--;
			if(m==0&&score>=230)
			{
			outtextxy(g-150,h-20,"CONGRATULATIONS");
			outtextxy(g-150,h+20,"YOU HAVE BECOME THE ULTIMATE CHAMPION");
			outtextxy(g-150,h+120,"Press any key to exit");
			getch();
			exit(0);
			}
			if(score>0&&m==0)
			{

			int k=getmaxx()/2;
			int l=getmaxy()/2;
			cleardevice();
			char string4[8];
			itoa(score, string4, 10);
			settextstyle(8,HORIZ_DIR,2);
			outtextxy(k-120,l-100,"CONGRATULATIONS!");
			outtextxy(k-220,l-40,"You scored ");
			outtextxy(k-70,l-40,string4);
			outtextxy(k-10,l-40,"points and saved ");
			outtextxy(k-150,l,"THE DUDE from dying.");
			outtextxy(k-200,l+40,"Alas! You are not the ultimate champion");
			outtextxy(k-150,l+120,"Press any key to exit");
			getch();
			exit(0);
			}



			break;


			case(right_key):
			cleardevice();
			character(i,j,r);
			i=i+speed;
			background();
			show_food(x,y);
			float d1=sqrt(pow(x-i,2)+pow(y-j,2));
			if(d1<20)
			{
				sound(o);
				x=20+random(550);
				y=50+random(400);
				nosound();
				score+=10;

			}
			char string1[8];

			itoa(score, string1, 10);
			settextstyle(6,HORIZ_DIR,2);
			outtextxy(15,1,"SCORE: ");
			outtextxy(80,1,string1);

			countdown(m);
			m--;
			if(m==0&&score>=230)
			{
			outtextxy(g-150,h-20,"CONGRATULATIONS");
			outtextxy(g-150,h+20,"YOU HAVE BECOME THE ULTIMATE CHAMPION");
			outtextxy(g-150,h+120,"Press any key to exit");
			getch();
			exit(0);
			}
			if(score>0&&m==0)
			{

			int k=getmaxx()/2;
			int l=getmaxy()/2;
			cleardevice();
			char string4[8];
			itoa(score, string4, 10);
			settextstyle(8,HORIZ_DIR,2);
			outtextxy(k-120,l-100,"CONGRATULATIONS!");
			outtextxy(k-220,l-40,"You scored ");
			outtextxy(k-70,l-40,string4);
			outtextxy(k-10,l-40,"points and saved ");
			outtextxy(k-150,l,"THE DUDE from dying.");
			outtextxy(k-200,l+40,"Alas! You are not the ultimate champion");
			outtextxy(k-150,l+120,"Press any key to exit");
			getch();
			exit(0);
			}

			break;


			case(up_key):
			cleardevice();
			character(i,j,r);
			j=j-speed;
			background();
			show_food(x,y);
			float d2=sqrt(pow(x-i,2)+pow(y-j,2));
			if(d2<20)
			{
				sound(o);
				x=20+random(550);
				y=50+random(400);
				nosound();
				score+=10;

			}
			char string2[8];
			itoa(score, string2, 10);
			settextstyle(6,HORIZ_DIR,2);
			outtextxy(15,1,"SCORE: ");
			outtextxy(80,1,string2);

			countdown(m);
			m--;
			if(m==0&&score>=230)
			{
			outtextxy(g-150,h-20,"CONGRATULATIONS");
			outtextxy(g-150,h+20,"YOU HAVE BECOME THE ULTIMATE CHAMPION");
			outtextxy(g-150,h+120,"Press any key to exit");
			getch();
			exit(0);
			}
			if(score>0&&m==0)
			{
			int k=getmaxx()/2;
			int l=getmaxy()/2;
			cleardevice();
			char string4[8];
			itoa(score, string4, 10);
			settextstyle(8,HORIZ_DIR,2);
			outtextxy(k-120,l-100,"CONGRATULATIONS!");
			outtextxy(k-220,l-40,"You scored ");
			outtextxy(k-70,l-40,string4);
			outtextxy(k-10,l-40,"points and saved ");
			outtextxy(k-150,l,"THE DUDE from dying.");
			outtextxy(k-200,l+40,"Alas! You are not the ultimate champion");
			outtextxy(k-150,l+120,"Press any key to exit");
			getch();
			exit(0);
			}


			break;


			case(down_key):
			cleardevice();
			character(i,j,r);
			background();
			j=j+speed;
			show_food(x,y);
			float d3=sqrt(pow(x-i,2)+pow(y-j,2));
			if(d3<21)
			{
				sound(o);
				x=20+random(550);
				y=50+random(400);
				nosound();
				score+=10;
			}
			char string3[8];
			itoa(score, string3, 10);
			settextstyle(6,HORIZ_DIR,2);
			outtextxy(15,1,"SCORE: ");
			outtextxy(80,1,(string3));

			countdown(m);
			m--;
			if(m==0&&score>=230)
			{
			outtextxy(g-150,h-20,"CONGRATULATIONS");
			outtextxy(g-150,h+20,"YOU HAVE BECOME THE ULTIMATE CHAMPION");
			outtextxy(g-150,h+120,"Press any key to exit");
			getch();
			exit(0);
			}
			if(score>0&&m==0)
			{
			int k=getmaxx()/2;
			int l=getmaxy()/2;
			cleardevice();
			char string4[8];
			itoa(score, string4, 10);
			settextstyle(8,HORIZ_DIR,2);
			outtextxy(k-120,l-100,"CONGRATULATIONS!");
			outtextxy(k-220,l-40,"You scored ");
			outtextxy(k-70,l-40,string4);
			outtextxy(k-10,l-40,"points and saved ");
			outtextxy(k-150,l,"THE DUDE from dying.");
			outtextxy(k-200,l+40,"Alas! You are not the ultimate champion");
			outtextxy(k-150,l+120,"Press any key to exit");
			getch();
			exit(0);
			}

			break;


		       }


		}


	}


			int k=getmaxx()/2;
			int l=getmaxy()/2;
			cleardevice();
			char string5[8];
			itoa(score, string5, 10);
			outtextxy(k-120,l-100,"CONGRATULATIONS!");
			outtextxy(k-220,l-40,"You scored ");
			outtextxy(k-70,l-40,string5);
			outtextxy(k-10,l-40,"points and saved ");
			outtextxy(k-150,l,"THE DUDE from dying.");
			outtextxy(k-200,l+40,"Alas! You are not the ultimate champion");
			outtextxy(k-150,l+120,"Press any key to exit");
			getch();
			exit(0);



}


