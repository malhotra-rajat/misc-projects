/* Q. Write a program to draw a line using Bresenhams algorithm. */

#include<stdio.h>
#include<conio.h>
#include<graphics.h>
#include<math.h>

void lineBresenham(int x1,int y1,int x2,int y2)
{
	int dx,dy,x,y,xEnd,d;

	dx=abs(x2-x1);
	dy=abs(y2-y1);
	d=(2*dy)-dx;

	if(x1>x2)
	{
		x=x2;
		y=y2;
		xEnd=x1;
	}
	else
	{
		x=x1;
		y=y1;
		xEnd=x2;
	}

	while(x<=xEnd)
	{
		putpixel(x,y,WHITE);
		if(d<0)
		{
			x++;
			d=d+(2*dy);
		}
		else
		{
			x++;
			y++;
			d=d+2*(dy-dx);
		}
	}
}

void main()
{
	int x1,y1,x2,y2;
	int gd=DETECT,gm;
	clrscr();

	printf("x1= ");
	scanf("%d",&x1);
	printf("y1= ");
	scanf("%d",&y1);
	printf("x2= ");
	scanf("%d",&x2);
	printf("y2= ");
	scanf("%d",&y2);

	initgraph(&gd,&gm,"C:\\tc\\bgi");
	lineBresenham(x1,y1,x2,y2);

	getch();
}