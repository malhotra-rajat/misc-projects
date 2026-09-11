#include<iostream.h>
#include<conio.h>

void insertion_sort(int *array, int array_length)
{
     int i, j, key;
     for(j = 1; j < array_length; j++)    //Notice starting with 1 (not 0)
     {
	   key = array[j];
	   for(i = j - 1; (i >= 0) && (array[i] > key); i--)   //Move smaller values up one position
	   {
		 array[i+1] = array[i];
	   }
	   array[i+1] = key;    //Insert key into proper position
     }

}

void main()
{
	int A[10], size;
	clrscr();
	cout<<"Enter the size of the array (1-10): ";
	cin>>size;

	cout<<"\n\nEnter the elements: ";
	for (int i=0; i<size; i++)
	{
		cin>>A[i];
	}
	insertion_sort (A, size);

	cout<<"\n\nThe sorted array is: ";

	for (i=0; i<size; i++)
	{
		cout<<A[i]<<"  ";

	}
	getch();
}





