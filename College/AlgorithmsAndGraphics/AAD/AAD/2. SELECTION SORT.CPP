#include<fstream.h>
#include<conio.h>
#include<time.h>

void selection_sort(int array[],int size)
{
	int min,pos,temp;
	for(int i=0;i<size-1;i++)
	{
		min=array[i];
		pos=i;

		for(int j=i+1;j<size;j++)
		{
			if(array[j]<min)
			{
				min=array[j];
				pos=j;
			}
		}
		temp=array[i];
		array[i]=array[pos];
		array[pos]=temp;
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
	selection_sort(array,size);
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
	cout<<"SELECTION SORT";
	sort_file_elements("FILE1.TXT",5000);
	sort_file_elements("FILE2.TXT",10000);
	sort_file_elements("FILE3.TXT",15000);
	sort_file_elements("FILE4.TXT",20000);

	getch();
}