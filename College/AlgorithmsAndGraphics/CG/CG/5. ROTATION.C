/* Q. Write a program to rotate a given point in 2-D coordinates. */

#include<stdio.h>
#include<conio.h>
#include<graphics.h>
#include<math.h>

void rotation(double *x,double *y,double angle_in_degrees)
{
	double angle_in_radians=(angle_in_degrees)*(3.14/180);
	double sin_value=sin(angle_in_radians);
	double cos_value=cos(angle_in_radians);

	*x= (*x)*cos_value - (*y)*sin_value;
	*y= (*x)*sin_value + (*y)*cos_value;
}

void main()
{
	int gd=DETECT,gm;
	double x,y;
	double angle;
	clrscr();

	initgraph(&gd,&gm,"c:\\tc\\bgi");
	printf("Coordinate X= ");
	scanf("%lf",&x);
	printf("Coordinate Y= ");
	scanf("%lf",&y);

	putpixel(x,y,WHITE);

	printf("Angle of Rotation(in Degrees): ");
	scanf("%lf",&angle);

	rotation(&x,&y,angle);

	putpixel(x,y,WHITE);

	getch();
}