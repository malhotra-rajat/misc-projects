#include<iostream.h>
#include<conio.h>
#include<iomanip.h>

void fn( int a[], int n)
{
	int mat [10][10];
	 for ( int i = 0; i<n; i++)
	 for ( int j =0; j<n; j++)
		mat [i][j] = a[j];


	  int m =n; int z = n-1;
	  for (int k =0; k<m; k++)
	  {
		n = m-1;
		while  (  n > z)
		{
			mat [k][n] = 0;
			n--;
		}
		z--;

	  }

	  cout<<"The matrix is: \n";




	  for ( i = 0; i<m; i++)
	{ for (  j =0; j<m; j++)
	cout<<setw(7)<<mat[i][j];
		cout<<endl;
	 }

}

void main()
{
	clrscr();

	int x[9];
	int size;
	cout<<"\n\nEnter size: ";
	cin>>size;
	cout<<"\n\nEnter elements: ";
	for ( int k = 0; k<size; k++)
	cin>>x[k];

	fn ( x, size);


	getch();
}