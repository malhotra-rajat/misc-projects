/* Q. Write a program to translate a given point in 2-D coordinates. */

#include<stdio.h>
#include<conio.h>
#include<graphics.h>

void translate(int *x,int *y,int tx,int ty)
{
	*x=*x+tx;
	*y=*y+ty;
}

void main()
{
	int gd=DETECT,gm;
	int x,y;
	int tx,ty;
	clrscr();

	initgraph(&gd,&gm,"C:\\tc\\bgi");
	printf("Coordinate X= ");
	scanf("%d",&x);
	printf("Coordinate Y= ");
	scanf("%d",&y);

	putpixel(x,y,WHITE);

	printf("Translation factor, Tx= ");
	scanf("%d",&tx);
	printf("Translation factor, Ty= ");
	scanf("%d",&ty);

	translate(&x,&y,tx,ty);

	putpixel(x,y,WHITE);

	getch();
}