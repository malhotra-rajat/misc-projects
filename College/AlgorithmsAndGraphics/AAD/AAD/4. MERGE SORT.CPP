#include<fstream.h>
#include<conio.h>
#include<time.h>

void merge(int array[],int p,int q,int r)
{
	int n1=q-p+1;
	int n2=r-q;
	int *arr=new int[n1+n2];

	for(int i=0,j=0;i<n1&&j<n2;)
	{
		if(array[p+i]>array[q+1+j])
		{
			arr[i+j]=array[q+1+j];
			j++;
		}
		else
		{
			arr[i+j]=array[p+i];
			i++;
		}
	}
	if(i==n1)
	{
		while(j<n2)
		{
			arr[i+j]=array[q+1+j];
			j++;
		}
	}
	else if(j==n2)
	{
		while(i<n1)
		{
			arr[i+j]=array[p+i];
			i++;
		}
	}

	for(i=0;i<n1+n2;i++)
	array[p+i]=arr[i];

	delete arr;
}

void merge_sort(int array[],int p,int r)
{
	if(p<r)
	{
		int q=(p+r)/2;
		merge_sort(array,p,q);
		merge_sort(array,q+1,r);
		merge(array,p,q,r);
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
	merge_sort(array,0,size-1);
	t2=clock();

	cout<<"\nTime taken to sort "<<size<<" elements: ";
	cout<<(t2-t1)/CLK_TCK<<" seconds";
	getch();

/*	for(i=0;i<size;i++)
	cout<<array[i]<<" ";
  */
	delete array;
}


void main()
{
	clrscr();
	cout<<"MERGE SORT";
	sort_file_elements("FILE1.TXT",5000);
	sort_file_elements("FILE2.TXT",10000);
	sort_file_elements("FILE3.TXT",15000);
	sort_file_elements("FILE4.TXT",20000);

	getch();
}