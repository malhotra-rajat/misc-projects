#include<fstream.h>
#include<conio.h>
#include<time.h>

void insertion_sort(int array[],int size)
{
	int j,temp;

	for(int i=1;i<size;i++)
	{
		temp=array[i];
		j=i;
		while( (j>0) && (array[j-1]>temp) )
		{
			array[j]=array[j-1];
			j--;
		}
	array[j]=temp;
	}
}


void sort_file_elements(char file_name[],int size)
{
	int *array=new int[size];
	fstream fin;
	clock_t t1,t2;
	fin.open(file_name,ios::in);
	for(int i=0;fin;i++)
	fin>>array[i];

	t1=clock();
	insertion_sort(array,size);
	t2=clock();

	cout<<"\nTime taken to sort "<<size<<" elements: ";
	cout<<(t2-t1)/CLK_TCK<<" seconds";
//	getch();

/*	for(i=0;i<size;i++)
	cout<<array[i]<<" ";
  */
	delete array;
}


void main()
{
	clrscr();
	cout<<"INSERTION SORT";
	sort_file_elements("FILE1.TXT",5000);
	sort_file_elements("FILE2.TXT",10000);
	sort_file_elements("FILE3.TXT",15000);
	sort_file_elements("FILE4.TXT",20000);

	getch();
}