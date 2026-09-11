#include<conio.h>
#include<graphics.h>
#include<process.h>
#include<dos.h>
#include<stdlib.h>
#include<math.h>

// GAME MADE BY RAJAT MALHOTRA OF CLASS XI-B

void set_graph()  //function for setting up graphics mode
{
	int driver=DETECT,mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");

}
void background()  //function for displaying the background

{
	int maxx=getmaxx();
	int maxy=getmaxy();
	setbkcolor(WHITE);
	setcolor(RED);
	rectangle(8,8,maxx-8,maxy-8);
	settextstyle(5,HORIZ_DIR,1);
	setcolor(BLUE);
	outtextxy((maxx/2)-15,1,"PRESS THE ENTER KEY TO EXIT");
}


void intro()    //function for the introduction screen
{

	int maxy=getmaxy();
	setbkcolor(WHITE);
	setcolor(RED);
	settextstyle(7,HORIZ_DIR,1);
	setcolor(1);
	outtextxy(220,(maxy/2)-200,"ALIENATED ALIEN!!!!!!!!");
	setcolor(RED);
	outtextxy(15,(maxy/2)-100,"This game is about an alien who has been left unintentionally ");
	outtextxy(15,(maxy/2)-75,"by the people of planet UNKNOWN.");
	outtextxy(15,(maxy/2)-50,"He is starving. Please guide him to the food and save him.");
	settextstyle(7,HORIZ_DIR,3);
	outtextxy(15,(maxy/2),"Press the directional keys on the keyboard to ");
	outtextxy(15,(maxy/2)+25,"play.");
	outtextxy(5,(maxy/2)+70,"Score 310 points before the countdown becomes zero ");
	outtextxy(5,(maxy/2)+100,"and become the ultimate champion.");
	outtextxy(15,(maxy/2)+150,"Press any key to start the game.");
	getch();
}

void character(int x,int y,int z)   //function for displaying the character

{
	setcolor(RED);
	circle(x,y,z);
	setcolor(RED);
	circle(x-8,y-5,z-20);
	setcolor(RED);
	circle(x+9,y-5,z-20);
	setcolor(RED);
	circle(x,y+10,z-20);
	setcolor(RED);
	circle(x-32,y-5,z-15);
	setcolor(RED);
	circle(x+32,y-5,z-15);
	setcolor(RED);
	line(x-27,y-5,x-20,y-5);
	setcolor(RED);
	line(x+20,y-5,x+27,y-5);
}

void show_food(int p,int m)   //function for displaying the food
{
	const int c=2;
	setcolor(GREEN);
	circle(p,m,c);
}

void countdown(int a)    //function for displaying the countdown
{
	char string_a[8];
	itoa(a,string_a,10);
	setcolor(BLUE);
	outtextxy(200,1,string_a);
}

void ultimate()   //function for displaying the text when
		  //someone beats the game
{
	int maxx=getmaxx();
	int maxy=getmaxy();
	cleardevice();
	setcolor(RED);
	outtextxy((maxx/2)-150,(maxy/2)-20,"CONGRATUALTIONS!");
	outtextxy((maxx/2)-150,(maxy/2)+20,"YOU HAVE BECOME THE ULTIMATE CHAMPION.");
	outtextxy((maxx/2)-150,(maxy/2)+120,"Press any key to exit");
	delay(4000);
	getch();
	exit(0);
}

void no_ultimate(char string4[8], int score)
{

	int maxx=getmaxx();
	int maxy=getmaxy();
	cleardevice();
	setcolor(RED);
	itoa(score,string4,10);
	settextstyle(8,HORIZ_DIR,2);
	outtextxy((maxx/2)-120,(maxy/2)-100,"CONGRATULATIONS!");
	outtextxy((maxx/2)-220,(maxy/2)-40,"You scored");
	setcolor(BLUE);
	outtextxy((maxx/2)-70,(maxy/2)-40,string4);
	setcolor(RED);
	outtextxy((maxx/2)-10,(maxy/2)-40,"points and saved");
	outtextxy((maxx/2)-150,(maxy/2),"him from dying.");
	outtextxy((maxx/2)-230,(maxy/2)+40,"Alas! You are not the ultimate champion.");
	outtextxy((maxx/2)-150,(maxy/2)+120,"Press any key to exit");
	delay(4000);
	getch();
	exit(0);
}

void score1(char string[8], int score)
{
	itoa(score,string,10);
	settextstyle(6,HORIZ_DIR,2);
	setcolor(BLUE);
	outtextxy(15,1,"SCORE: ");
	setcolor(RED);
	outtextxy(80,1,string);
}


void main()
{
	set_graph();  //FOR SETTING UP GRAPHICS MODE
	intro();      //FOR SHOWING THE INTRODUCTION SCREEN
	cleardevice();
	background();

		//-----------------------------------
		//VARIABLES USED IN THE MAIN FUNCTION
		//-----------------------------------

	const char right_key=77, left_key=75, up_key=72, down_key=80;
	const int r=20;
	char keypress;
	int i=getmaxx()/2, j=getmaxy()/2;
	const int speed=18;
	int x=300,y=300;
	int score=0;
	int count=500;


	character(i,j,r);      //to display the character initially.
	char string_a[8];
	score1(string_a,score); //to display the score initially.
	show_food(x,y);        //to display the food initially.

	//MAIN GAME LOOP

	while(keypress!='\r')
	{
			keypress=(char)getch();
			switch(keypress)
			{

			 case left_key:



			  cleardevice();

			  i=i-speed;
			  character(i,j,r);
			  background();

			  //EVALUATIG THE SUM OF RADII OF THE TWO CIRCLES TO CHECK
			  //WHETHER THE CHARACTER HAS EATEN THE FOOD OR NOT.

			  float d=sqrt(pow(x-i,2)+pow(y-j,2));

			  if(d<20)
			  {
				x=20+random(550);
				y=50+random(400);
				score+=10;

			  }
			  show_food(x,y);
			  char string_b[8];
			  score1(string_b,score);

			  countdown(count);  //displaying the countdown
			  count--;

			  if(count==0&&score>=310)
			   ultimate();

			  if(score>0&&count==0)
			  {
			   char string4[8];
			   no_ultimate(string4,score);
			  }
			  break;




			 case right_key:




			  cleardevice();

			  i=i+speed;
			  character(i,j,r);
			  background();

			  float d1=sqrt(pow(x-i,2)+pow(y-j,2));
			  if(d1<20)
			  {
				x=20+random(550);
				y=50+random(400);
				score+=10;

			  }
			  show_food(x,y);
			  char string_c[8];
			  score1(string_c,score);

			  countdown(count);
			  count--;

			  if(count==0&&score>=310)
			   ultimate();

			  if(score>0&&count==0)
			  {
			   char string4[8];
			   no_ultimate(string4,score);
			  }
			  break;




			 case up_key:



			  cleardevice();

			  j=j-speed;
			  character(i,j,r);
			  background();

			  float d2=sqrt(pow(x-i,2)+pow(y-j,2));
			  if(d2<20)
			  {
				x=20+random(550);
				y=50+random(400);
				score+=10;

			  }
			  show_food(x,y);
			  char string_d[8];
			  score1(string_d,score);

			  countdown(count);
			  count--;

			  if(count==0&&score>=310)
			   ultimate();

			  if(score>0&&count==0)
			  {
			   char string4[8];
			   no_ultimate(string4,score);
			  }

			  break;



			 case down_key:



			  cleardevice();

			  background();
			  j=j+speed;
			  character(i,j,r);

			  float d3=sqrt(pow(x-i,2)+pow(y-j,2));
			  if(d3<20)
			  {
				x=20+random(550);
				y=50+random(400);
				score+=10;

			  }
			  show_food(x,y);
			  char string_e[8];
			  score1(string_e,score);

			  countdown(count);
			  count--;

			  if(count==0&&score>=310)
			   ultimate();

			  if(score>0&&count==0)
			  {
			   char string4[8];
			   no_ultimate(string4,score);
			   break;
			  }
		       }


	}


	char string4[8];
	no_ultimate(string4,score);   //TEXT TO DISPLAY IF THE USER PRESSES ENTER

}
