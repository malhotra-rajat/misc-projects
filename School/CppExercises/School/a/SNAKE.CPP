#include<conio.h>
#include<dos.h>
#include<graphics.h>
#include<stdlib.h>
#include<fstream.h>
#include<graphics.h>
#include<stdio.h>
#include<ctype.h>
#include<string.h>
#include"csnake.h"

int rows=25,columns=25;
int cell_size=12;
int board_left,board_top;	//We will put value after graphics mode is initialized
int isfood,foodx,foody,food_score;	//Food variables
int board_width=rows*cell_size,board_height=columns*cell_size,midx,midy;	//Some handy variables
int total_score[2],highest_score=0;
char h_name[20];
int players=2,pcomputer;	//no. of players and is he computer
int end[2]={1,1};		//have the snake ended (2 as 2 snakes)
int speed=7,limit_computer=1;	//speed(inverse), computer skill
int style=1;

const int MAX=6;		//Maximum menu objects

void start_playing();
void default_data(SNAKE[]);
void check_for_end(SNAKE[]);
void putfood(SNAKE[]);
void display_score();
void end_game();
void pause_game();
void move_pcomputer(SNAKE&);
void comment(int);
void writetext(int,int);
int menu();
void skill_menu();
void show_help();
void clearbyline();
void menu_handler();

void main()
	{
	int d=DETECT,m;
	initgraph(&d,&m,"c:\\tc\\bgi");
	randomize();
	setbkcolor(0);
	midx=getmaxx()/2;
	midy=getmaxy()/2;
	board_left=midx-board_width/2;
	board_top=midy-board_height/2;
	start_playing();
	}

void start_playing()
	{
	clearviewport();
showmenu:
	int sel=menu();
	switch(sel)
		{
		case 0:
			players=1;
		break;
		case 1:
			players=2;
			pcomputer=0;
		break;
		case 2:
			players=2;
			pcomputer=1;
		break;
		case 3:
			skill_menu();
			goto showmenu;
		case 4:
			setcolor(YELLOW);
			settextstyle(6,0,5);
			outtextxy(0,10,"Enter (1-classic, 2-modern): ");
			char ch=getch();
			while(ch!='1' && ch!='2')
				ch=getch();
			if(ch=='1')
				{
				outtextxy(0,70,"Classic");
				style=0;
				}
			else
				{
				outtextxy(0,70,"Modern");
				style=1;
				}
			ofstream o1("style.snk");
			o1<<style;
			o1.close();
			getch();
			clearviewport();

			goto showmenu;
		case 5:
			show_help();
			goto showmenu;
		case 6:
			closegraph();
			exit(0);
		}
	SNAKE s[2];
	s[0].initialize(style,LIGHTGREEN,GREEN,3,board_left,board_top,cell_size);
	if(players==2)
		s[1].initialize(style,LIGHTBLUE,BLUE,3,board_left,board_top,cell_size);

	default_data(s);

	comment(1);	//i.e. press enter to continue
	int pressed;
	comment(0);
	int moved[2];
	isfood=0;
	while(1)
		{
		moved[0]=0; moved[1]=0;
		putfood(s);
		for(int wait=0;wait<10;wait++)
			{
			delay(speed);
			if(kbhit())
				{
				sound(500);
				pressed=getch();
				if(pressed=='`') {nosound();closegraph();exit(0);}
//				if(pressed=='2') {players=2;pcomputer=0;end[1]=0;snake_size[1]=3;}
				if(!moved[0])
					{
					//right
					if(pressed==77)
						moved[0]=s[0].setdirection(1);
					//left
					else if(pressed==75)
						moved[0]=s[0].setdirection(3);
					//down
					else if(pressed==80)
						moved[0]=s[0].setdirection(2);	//if has moved then will return
					//up
					else if(pressed==72)
						moved[0]=s[0].setdirection(0);
					}
				if(pressed==32) pause_game();
				pressed=tolower(pressed);
				if(players==2&&!pcomputer)
					{
					if(!moved[1])
						{
						if(pressed=='d')
							moved[1]=s[1].setdirection(1);
						else if(pressed=='a')
							moved[1]=s[1].setdirection(3);
						else if(pressed=='s')
							moved[1]=s[1].setdirection(2);
						else if(pressed=='w')
							moved[1]=s[1].setdirection(0);
						}
					}

				}
			if(pcomputer==1 && !moved[1] && !end[1]) {move_pcomputer(s[1]);moved[1]=1;}
			}
		nosound();
		for(int i=0;i<players;i++)
			{
			if(s[i].x[0]==foodx && s[i].y[0]==foody)
				{
				sound(1000);
				delay(50);
				nosound();
				isfood=0;
				if(style==0)
					{
					int cx=board_left+cell_size*foodx+cell_size/2;
					int cy=board_top+cell_size*foody+cell_size/2;
					setcolor(s[i].color);
					circle(cx,cy,cell_size/2-1);
					setfillstyle(1,s[i].fillcolor);
					floodfill(cx,cy,s[i].color);
					}
				s[i].size++;
				total_score[i]+=food_score;
				display_score();
				}
			if(!end[i])	//player not died
				{
				s[i].move();
				s[i].display();
				}
			}
		check_for_end(s);
		if(food_score>1)
			food_score--;
		}
	}

void default_data(SNAKE *s)
	{

	ifstream h_s("SCORE.SNK");
	char hh[5];
	h_s>>highest_score;
	h_s.getline(h_name,20);
	h_s.close();

	int sel;
	ifstream o1("skill.snk");
	o1>>sel;
	o1.close();
	switch(sel)
		{
		case 0:
			speed=11; limit_computer=3;
		break;
		case 1:
			speed=9; limit_computer=2;
		break;
		case 2:
			speed=7; limit_computer=1;
		break;
		case 3:
			speed=5; limit_computer=0;
		break;
		case 4:
			speed=3; limit_computer=-1;
		}

	ifstream o2("style.snk");
	o2>>style;
	o2.close();
	isfood=0;
	s[1].x[0]=1; s[1].x[1]=1; s[1].x[2]=1;
	total_score[0]=0,total_score[1]=0,food_score=0;

	setcolor(LIGHTGRAY);
	setlinestyle(1,0,1);
	rectangle(board_left-1,board_top-1,board_left+board_width+1,board_top+board_height+1);
	setlinestyle(0,0,3);
	rectangle(board_left-cell_size,board_top-cell_size,board_left+board_width+cell_size,board_top+board_height+cell_size);
	setlinestyle(0,0,1);
	display_score();
	end[0]=0;
	if(players==2) end[1]=0;
	}


void check_for_end(SNAKE s[])
	{
	for(int j=0;j<players;j++)
		{
		if(end[j]) continue;	//if the player has died no need of doing all
		int i;
		if(players==2)
			{
			//if one player died now and other has scored more than him end
			if(end[0]==1 && total_score[1]>total_score[0]) end_game();
			if(end[1]==1 && total_score[0]>total_score[1]) end_game();
			}
		end[j]=s[j].out_of_boundary(rows,columns);
		if(!end[j])
			end[j]=s[j].check_bite_itself();
		if(end[0] && end[1])
			end_game();
		}                   //ended loop
	}

void putfood(SNAKE s[])
	{
	static int moves=0;
//	static int l_x,l_y;
	if(!isfood)
		{
		moves=0;
	    randomm:
		foodx=random(rows),foody=rand()%columns;
		while(s[0].isat(foodx,foody) || s[1].isat(foodx,foody))
			{
			foodx=random(rows);
			foody=random(columns);
			}
		isfood=1;
		food_score=50;
		}
	setcolor(RED);
	int cx=board_left+foodx*cell_size+cell_size/2;
	int cy=board_top+foody*cell_size+cell_size/2;
	circle(cx,cy,cell_size/2-1);
	setfillstyle(1,RED);
	floodfill(cx,cy,getcolor());

	moves++;
	}

void display_score()
	{
	setcolor(WHITE);
	char t_score[2][5],f_score[5],h_score[5];
	itoa(total_score[0],t_score[0],10);
	itoa(total_score[1],t_score[1],10);
	itoa(highest_score,h_score,10);
//	itoa(food_score,f_score,10);
	setviewport(board_left,board_top+board_height+20,board_left+100,board_top+board_height+30,1);
	clearviewport();
	settextstyle(2,0,4);
	outtextxy(0,0,"Player 1:");
	outtextxy(textwidth("Player 1: "),0,t_score[0]);


	setviewport(midx-(textwidth("Highest:        ")+textwidth(h_name))/2,board_top+board_height+30,midx+(textwidth("Highest:        ")+textwidth(h_name))/2,board_top+board_height+40,1);
	clearviewport();
	outtext("Highest:");
	outtext(h_score);
	outtext(" by: ");
	outtext(h_name);

	if(players!=2) setcolor(getbkcolor());
	setviewport(board_left+board_width-60,board_top+board_height+20,board_width+board_left+100-80,board_top+board_height+30,1);
	clearviewport();
	settextstyle(2,0,4);
	outtextxy(0,0,"Player 2:");
	outtextxy(textwidth("Player 2: "),0,t_score[1]);

	setviewport(0,0,639,479,1);
	}

void end_game()
	{
	int pl;

	if(players==1) pl=0;
	else if(total_score[0]>total_score[1]) pl=1;
	else if(total_score[0]<total_score[1] && !pcomputer) pl=2;
	else if(total_score[0]<total_score[1] && pcomputer)pl=3;
	else pl=4;

	setcolor(RED);
	static char wwins[5][20]={"GAME OVER!!","WINNER! Player 1!!","WINNER! Player 2!!","WINNER! COMPUTER!!","MATCH DRAWN"};

	settextstyle(3,0,4);
	outtextxy(midx-textwidth(wwins[pl])/2,0,wwins[pl]);

	for(int i=0;i<players;i++)
	{
	if(highest_score<=total_score[i])
		{
		settextstyle(4,0,4);
		setcolor(YELLOW);
		static char congrates[50]={"Congratulations!! You got the Highest!!"};

		int w=textwidth(congrates);

		setviewport(getmaxx()/2-w/2,getmaxy()/2-textheight("C")/2,getmaxx()/2+w/2,getmaxy()/2+textheight("C"),1);
		clearviewport();
		setviewport(0,0,639,479,1);
		outtextxy(getmaxx()/2-w/2,getmaxy()/2-textheight("C")/2,congrates);

		comment(1);
		clearviewport();
		gotoxy(1,1);
		cout<<"Enter your name: ";
		cin>>h_name;
		highest_score=total_score[i];
		ofstream h_s("SCORE.SNK");
		h_s<<highest_score<<' ';
		h_s.write(h_name,strlen(h_name));
		h_s.close();
		}
	}
	comment(1);
	clearbyline();
	start_playing();
	}

void pause_game()
	{
	nosound();
	comment(2);
	}

void move_pcomputer(SNAKE &s)
	{
	int lim;
	lim=limit_computer;
	if(s.x[0]<=0 || s.x[0]>=rows-1 || s.y[0]<=0 || s.y[0]>=columns-1) lim=0;

	int a=random(3);
	int moved=0;
	if(a>lim)	//!= so computer can even struck with wall
		{
		if(foodx>s.x[0])
			moved=s.setdirection(1);
		if(foodx<s.x[0] && !moved)
			moved=s.setdirection(3);
		if(foody>s.y[0] && !moved)
			moved=s.setdirection(2);
		if(foody<s.y[0] && !moved)
			moved=s.setdirection(0);
		}
	}

int menu()
	{
	static int sel=0;
	static char title[10]="SNAKE";
	writetext(1,sel);
	settextstyle(4,0,10);
	setcolor(6);
	outtextxy(midx-textwidth(title)/2,0,title);
getKey:
	settextstyle(4,0,10);
	int i=0;
	while(!kbhit())
		{
		i++;
		delay(50);
		if(i%10!=0) continue;
		setcolor(rand()%15+1);
		outtextxy(midx-textwidth(title)/2,0,title);
		}
	int pressed=getch();
	switch(pressed)
		{
		case 13:
		break;
		case 72:	//FOR UP
			if(sel!=0)
				{
				writetext(0,sel);
				sel--;
				writetext(1,sel);
				}
			goto getKey;

		case 80:	//FOR DOWN
			if(sel!=MAX)
				{
				writetext(0,sel);
				sel++;
				writetext(1,sel);
				}
			goto getKey;

		default:
			goto getKey;
		}
	clearbyline();
	return sel;
	}

void writetext(int color,int sel)
	{
	static char items[MAX+1][20]={"SINGLE PLAYER","DOUBLE PLAYER","V/S COMPUTER","SKILL","STYLE","HELP","EXIT"};

	static int sel_size=7,nor_size=1,space=50,font=3;

	settextstyle(font,0,nor_size);
	int bot_x=midx-textwidth(items[sel+1])/2;
	int bot_y=midy+space+textheight(items[sel+1])/2;
	int top_x=midx-textwidth(items[sel-1])/2;
	int top_y=midy-space-textheight(items[sel-1])/2;
	settextstyle(font,0,sel_size);
	int sel_x=midx-textwidth(items[sel])/2;
	int sel_y=midy-textheight(items[sel])/2;
	int sel_width=textwidth(items[sel]);
//	int sel_height=textheight(items[sel]);

	setcolor(getbkcolor());
	//WRITING TOP TEXT
	if(sel!=0)
		{
		if(color==1)
			setcolor(6);
		settextstyle(font,0,nor_size);
		outtextxy(top_x,top_y,items[sel-1]);
		}
	//WRITING SELECTED TEXT
		{
		if(color==1)
			setcolor(BLUE);
		settextstyle(font,0,sel_size);
		outtextxy(sel_x-3,sel_y-3,items[sel]);
		if(color==1)
			setcolor(WHITE);
		settextstyle(font,0,sel_size);
		outtextxy(sel_x,sel_y,items[sel]);
		for(int i=42;i<=48;i++)
			{

			if(color==1)
				{
				if(i==42||i==48)
					setcolor(LIGHTGRAY);
				else if(i==43||i==46)
					setcolor(WHITE);
				else if(i==45)
					setcolor(YELLOW);
				}
			line(0,sel_y+i,sel_x-10,sel_y+i);
			line(sel_x+sel_width,sel_y+i,getmaxx(),sel_y+i);
			}
		}

	//WRITING BOTTOM TEXT
	if(sel!=MAX)
		{
		if(color==1)
			setcolor(GREEN);
		settextstyle(font,0,nor_size);
		outtextxy(bot_x,bot_y,items[sel+1]);
		}
	}

void show_help()
	{
	clearviewport();
	setcolor(WHITE);
	settextstyle(3,0,1);
	outtextxy(0,0,"Player 1 Keys: UP, DOWN, LEFT, RIGHT");
	outtextxy(0,textheight("F"),"Player 2 Keys: w, s, a, d (respectively)");
	outtextxy(0,450,"Press any key to continue.......");
	setcolor(6);
	outtextxy(0,5*textheight("F"),"Created By: Himanshu Mishra");
	outtextxy(0,6*textheight("F"),"Class: XII - E");
	setcolor(GREEN);
	outtextxy(0,7*textheight("F"),"School: The Air Force School, Subroto Park, New Delhi-10, India");
	outtextxy(0,8*textheight("F"),"Email-ID: himanshu_mishra007@yahoo.co.in");

	getch();
	clearviewport();
	}

void clearbyline()
	{
	for(int y=0;y<=midy;y++)
		{
		delay(1);
		sound(400-y);
		setcolor(getbkcolor());
		line(0,y,getmaxx(),y);
		line(0,midy*2-y,getmaxx(),midy*2-y);
		}
	nosound();
	}

void skill_menu()
	{
	clearviewport();
	settextstyle(4,0,10);
	setcolor(GREEN);
	outtextxy(midx-textwidth("SKILL")/2,0,"SKILL");
	static char skills[5][20]={"EASIEST","EASY","NORMAL","HARD","HARDEST"};
	static int sel=2,last_selected;
	ifstream o("skill.snk");
	o>>sel;
	o.close();
	last_selected=sel;
	settextstyle(3,0,4);
	setcolor(6);
	for(int i=0;i<5;i++)
		{
		outtextxy(midx-textwidth(skills[i])/2,200+40*i,skills[i]);
		}
	setcolor(WHITE);
	outtextxy(midx-textwidth(skills[sel])/2,200+40*sel,skills[sel]);
getKey:
	int pressed=getch();
	switch(pressed)
		{
		case 27:
			sel=last_selected;
		break;
		case 13:
		break;
		case 72:	//FOR UP
			setcolor(6);
			outtextxy(midx-textwidth(skills[sel])/2,200+40*sel,skills[sel]);
			sel--;
			if(sel<0) sel=4;
			setcolor(WHITE);
			outtextxy(midx-textwidth(skills[sel])/2,200+40*sel,skills[sel]);
			goto getKey;

		case 80:	//FOR DOWN
			setcolor(6);
			outtextxy(midx-textwidth(skills[sel])/2,200+40*sel,skills[sel]);
			sel++;
			if(sel>4) sel=0;
			setcolor(WHITE);
			outtextxy(midx-textwidth(skills[sel])/2,200+40*sel,skills[sel]);
			goto getKey;
		default:
			goto getKey;
		}
	switch(sel)
		{
		case 0:
			speed=11; limit_computer=3;
		break;
		case 1:
			speed=9; limit_computer=2;
		break;
		case 2:
			speed=7; limit_computer=1;
		break;
		case 3:
			speed=5; limit_computer=0;
		break;
		case 4:
			speed=3; limit_computer=-1;
		}
	ofstream o1("skill.snk");
	o1<<sel;
	o1.close();
	clearbyline();
	}

void comment(int n)
	{
//	static last_n=0;
	char comments[5][50]={"","Press ENTER to continue...","Paused!! Press Space to resume.."};

	settextstyle(3,0,3);
	setcolor(BROWN);
	setviewport(midx-textwidth(comments[n])/2,getmaxy()-textheight(comments[n])-5,midx+textwidth(comments[n])/2,getmaxy()-5,1);
	outtext(comments[n]);
	int sel;
	if(n==1)
		{
		while(sel!=13)
			sel=getch();
		}
	else if(n==2)
		{
		sel=getch();
		while(sel!=32)
			sel=getch();
		}
	clearviewport();
	setviewport(0,0,639,479,1);
	}