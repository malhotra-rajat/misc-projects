//TO SHOW LINEAR AND BINARY SEARCH

#include<iostream.h>
#include<conio.h>

int lsearch(int a[],int sz,int item);
int bsearch(int a[],int sz,int item);

void main()
{
	clrscr();
	int a[10],sz,i,item,pos,ch;

	cout<<"Enter the array size: ";
	cin>>sz;
	cout<<"\nEnter the array\n\n";

	for(i=0;i<sz;i++)
	{
		cout<<"Enter Element "<<i+1<<": ";
		cin>>a[i];
	}
	cout<<"\n\nEnter 1 for linear search";
	cout<<"\nEnter 2 for binary search\n\n";

	cin>>ch;

	cout<<"\nEnter the item to be searched for: ";
	cin>>item;

	if(ch==1)
		pos=lsearch(a,sz,item);
	else if(ch==2)
		pos=bsearch(a,sz,item);

	if(pos==50)
		cout<<"Element not in the list ";
	else
		cout<<"\n"<<item<<" found at "<<1+pos;

	getch();
}

int lsearch(int a[],int sz,int item)
{
	for(int i=0;i<sz;i++)
	{
		if(a[i]==item)
		break;
	}
	if(i==sz)
		return 50;
	else
		return i;
}

int bsearch(int a[],int sz,int item)
{
	int beg=0,last=sz-1,mid;

	while(beg<=last)
	{
		mid=(beg+last)/2;
		if(a[mid]==item)
		{
			return mid;
		}
		else if(a[mid]>item)
		{
			last=mid-1;
		}
		else if(a[mid]<item)
		{
			beg=mid+1;
		}
	}
	return 50;
}
