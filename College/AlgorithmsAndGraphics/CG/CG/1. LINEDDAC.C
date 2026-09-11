/* Q. Write a program to draw a line using Digital Differential Analyzer(DDA) algorithm. */

#include<stdio.h>
#include<conio.h>
#include<graphics.h>
#include<math.h>

int round(float f)
{
	int i;
	i=f+0.5;
	return i;
}

void lineDDA(int x1,int y1,int x2,int y2)
{
	float dx,dy;
	float steps,xInc,yInc,x,y;
	int k;

	dx=x2-x1;
	dy=y2-y1;

	if(abs(dx)>abs(dy))
	steps=abs(dx);
	else
	steps=abs(dy);

	xInc=dx/steps;
	yInc=dy/steps;
	x=x1;
	y=y1;

	for(k=0;k<=steps;k++)
	{
		putpixel(round(x),round(y),WHITE);
		x=x+xInc;
		y=y+yInc;
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
	lineDDA(x1,y1,x2,y2);

	getch();
}