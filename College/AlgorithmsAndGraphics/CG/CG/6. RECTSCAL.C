/* Q. Write a program to scale a rectangle in 2-D coordinates. */

#include<stdio.h>
#include<conio.h>
#include<graphics.h>
#include<math.h>

void scale(int *x,int *y,int sx,int sy)
{
	*x=(*x) * sx;
	*y=(*y) * sy;
}

void main()
{
	int xMin,yMin,xMax,yMax;
	int gd=DETECT,gm;
	int sx,sy;
	clrscr();

	initgraph(&gd,&gm,"C:\\tc\\bgi");
	printf("xMin= ");
	scanf("%d",&xMin);
	printf("yMin= ");
	scanf("%d",&yMin);
	printf("xMax= ");
	scanf("%d",&xMax);
	printf("yMax= ");
	scanf("%d",&yMax);

	rectangle(xMin,yMin,xMax,yMax);

	printf("Scaling factor, Tx= ");
	scanf("%d",&sx);
	printf("Scaling factor, Ty= ");
	scanf("%d",&sy);

	scale(&xMax,&yMax,sx,sy);
	scale(&xMin,&yMin,sx,sy);

	rectangle(xMin,yMin,xMax,yMax);

	getch();
}