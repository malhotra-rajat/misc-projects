#include<fstream.h>
#include<conio.h>
#include<time.h>

int partition(int array[],int p,int r)
{
	int x=array[r];
	int i=p-1;
	int temp;
	for(int j=p;j<r;j++)
	{
		if(array[j]<=x)
		{
			i++;
			temp=array[i];
			array[i]=array[j];
			array[j]=temp;
		}
	}
	temp=array[i+1];
	array[i+1]=array[r];
	array[r]=temp;

	return i+1;
}

void quick_sort(int array[],int p,int r)
{
	if(p<r)
	{
		int q=partition(array,p,r);
		quick_sort(array,p,q-1);
		quick_sort(array,q+1,r);
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
	quick_sort(array,0,size-1);
	t2=clock();

	cout<<"\nTime taken to sort "<<size<<" elements: ";
	cout<<(t2-t1)/CLK_TCK<<" seconds";
 /*	getch();

	for(i=0;i<size;i++)
	cout<<array[i]<<" ";
   */
	delete array;
}


void main()
{
	clrscr();
	cout<<"QUICK SORT";
	sort_file_elements("FILE1.TXT",5000);
	sort_file_elements("FILE2.TXT",10000);
	sort_file_elements("FILE3.TXT",15000);
	sort_file_elements("FILE4.TXT",20000);

	getch();
}