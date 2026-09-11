/* Q. Write a program to implement binary search technique and perform running time analysis of this technique. */

#include<fstream.h>
#include<conio.h>
#include<time.h>
#include<stdlib.h>
#include<dos.h>

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

int binary_search(int array[],int no,int size)
{
	int flag=0,start=0,end=size-1,mid;

	while(start<=end)
	{
		delay(1);
		mid=(start+end)/2;

		if(array[mid]>no)
		end=mid-1;

		else if(array[mid]<no)
		start=mid+1;

		else
		{
			flag=mid+1;
			break;
		}
	}
	return flag;

}

void find_file_element(char file_name[],int no,int size)
{
	int *array=new int[size];
	fstream fin;
	clock_t t1,t2;
	fin.open(file_name,ios::in);
	for(int i=0;fin;i++)
	fin>>array[i];
	quick_sort(array,0,size-1);

	t1=clock();
	int result=binary_search(array,no,size);
	t2=clock();

	if(result)
	cout<<"\n"<<no<<" found at position "<<result<<" in "<<file_name;
	else
	cout<<"\n"<<no<<" not found in "<<file_name;

	cout<<"\nTime taken to search from "<<size<<" elements: ";
	cout<<(t2-t1)/CLK_TCK<<" seconds"<<"\n";

	delete array;
}


void main()
{
	clrscr();
	int no;
	cout<<"Enter the number to be searched: ";
	cin>>no;

	cout<<"\nBINARY SEARCH";
	find_file_element("FILE1.TXT",no,5000);
	find_file_element("FILE2.TXT",no,10000);
	find_file_element("FILE3.TXT",no,15000);
	find_file_element("FILE4.TXT",no,20000);

	getch();
}