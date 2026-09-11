#include<iostream.h>
#include<conio.h>

int a[50];

void merge(int,int,int);

void merge_sort(int low,int high)
{
	int mid;

	if(low<high)
	{
		mid=(low+high)/2;
		merge_sort(low,mid);
		merge_sort(mid+1,high);
		merge(low,mid,high);
	}
}

void merge(int low,int mid,int high)
{
	int h,i,j,b[50],k;
	h=low;
	i=low;
	j=mid+1;

	while ( (h<=mid) && (j<=high) )
	{
		if(a[h]<=a[j])
		{
			b[i]=a[h];
			h++;
		}
		else
		{
			b[i]=a[j];
			j++;
		}
	i++;
	}

	if(h>mid)
	{
		for(k=j;k<=high;k++)
		{
			b[i]=a[k];
			i++;
		}
	}
	else
	{
		for(k=h;k<=mid;k++)
		{
			b[i]=a[k];
			i++;
		}
	}
	for(k=low;k<=high;k++)
		a[k]=b[k];
}

void main()
{
	int num,i;
	clrscr();
	cout<<"Please Enter the no. of elements you want to sort: ";
	cin>>num;

	cout<<endl;

	cout<<"Now, Please Enter the ("<< num <<") numbers: ";

	for(i=1;i<=num;i++)
	{
		cin>>a[i] ;
	}

	merge_sort(1,num);
	cout<<endl;

	cout<<"\n\nSo, the sorted list (using MERGE SORT) will be :"<<endl;
	cout<<endl<<endl;

	for(i=1;i<=num;i++)
		cout<<a[i]<<" ";


	getch();
}