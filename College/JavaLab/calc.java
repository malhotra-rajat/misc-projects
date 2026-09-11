/* Program to show multiple inheritance using interfaces */

 interface area 
{
     public void compute();
}
interface show
{
      public void display();
}

class rect implements area,show
{
   int v; 
   int a;
   int b;
    rect(int x,int y)
     {
        a=x;
        b=y;
     }
public  void compute()
  { 
     v=a*b;
  }
public void display()
  {
   System.out.println("The area of the rectangle is:"+v);
  }
}
class triag implements area
{
   float z;
   int c;
   int d;
   triag(int e,int f)
    { 
       c=e;
       d=f;
    }
public void compute()
   {
     z=(float)0.5*c*d;
   }
public void display()
  {
  System.out.println("The area of triangle is:"+z);
  }
}

class calc
{
    public static void main(String args[])
  {
    rect ob=new rect(5,10);
    triag a=new triag (3,4);
    ob.compute();
    a.compute();
    ob.display();
    a.display();
   }
 
}