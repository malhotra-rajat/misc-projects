#include<stdio.h>
#include<conio.h>

void TOWER(int n,char BEG,char AUX,char ENDING);

void main()
{
	int n;
	clrscr();
	
	printf("TOWERS OF HANOI\n\n");
	printf("PEG 1 denoted as A\n");
	printf("PEG 2 denoted as B\n");
	printf("PEG 3 denoted as C\n");
	printf("-----------------------------\n");
	printf("Enter the no.of disks : ");
	
	scanf ("%d", &n);

	printf("-----------------------------\n");
	
	TOWER(n,'A','B','C');
	
	printf("--------- O V E R -----------");
	getch();

}

void TOWER(int n,char BEG,char AUX,char ENDING)
{
	if(n==1)
	{
		printf("Move Disk from Peg : %c --> %c",BEG, ENDING);
		printf ("\n");
	}

	else
	{
		TOWER(n-1,BEG,ENDING,AUX);
		printf("Move Disk from Peg : %c --> %c",BEG, ENDING);
		printf ("\n");
		TOWER(n-1,AUX,BEG,ENDING);
	}
}