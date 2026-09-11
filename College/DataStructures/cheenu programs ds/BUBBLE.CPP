#include<iostream.h>
#include<conio.h>

void bubble_sort(int *array, int arrayLength)
{
      int i, j, flag = 1;    // set flag to 1 to begin initial pass
      int temp;             // holding variable
      for(i = 1; (i <= arrayLength) && flag; i++)
      {
	  flag = 0;
	  for (j=0; j < (arrayLength -1); j++)
	  {
	       if (array[j+1] < array[j])      // ascending order simply changes to <
	       {
		    temp = array[j];             // swap elements
		    array[j] = array[j+1];
		    array[j+1] = temp;
		    flag = 1;               // indicates that a swap occurred.
	       }
	  }
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
	bubble_sort (A, size);

	cout<<"\n\nThe sorted array is: ";

	for (i=0; i<size; i++)
	{
		cout<<A[i]<<"  ";

	}
	getch();
}


