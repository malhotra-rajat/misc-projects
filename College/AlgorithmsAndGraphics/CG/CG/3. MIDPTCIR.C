/* Q. Write a program to draw a circle using Mid Point circle algorithm. */

#include<stdio.h>
#include<conio.h>
#include<graphics.h>

void plotPoints(int centreX,int centreY,int x,int y)
{
	putpixel(centreX+x,centreY+y,WHITE);
	putpixel(centreX+x,centreY-y,WHITE);
	putpixel(centreX-x,centreY+y,WHITE);
	putpixel(centreX-x,centreY-y,WHITE);
	putpixel(centreX+y,centreY+x,WHITE);
	putpixel(centreX+y,centreY-x,WHITE);
	putpixel(centreX-y,centreY+x,WHITE);
	putpixel(centreX-y,centreY-x,WHITE);
}

void midPointCircle(int centreX,int centreY,int radius)
{
	int x,y,d;

	x=0;
	y=radius;
	d=1-radius;

	while(x<=y)
	{
		plotPoints(centreX,centreY,x,y);
		if(d<0)
		{
			d=d+(2*x)+3;
			x++;
		}
		else
		{
			d=d+(2*x)-(2*y)+5;
			x++;
			y--;
		}
	}
}

void main()
{
	int gd=DETECT,gm;
	int centreX,centreY,radius;
	clrscr();

	printf("Centre X= ");
	scanf("%d",&centreX);
	printf("Centre Y= ");
	scanf("%d",&centreY);
	printf("Radius= ");
	scanf("%d",&radius);

	initgraph(&gd,&gm,"C:\\tc\\bgi");
	midPointCircle(centreX,centreY,radius);

	getch();
}