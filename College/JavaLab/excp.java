class chk extends Exception
{
   chk(String s)
{
   super(s);
}
}

class excp
{
  public static void main(String args[])
{
    try
{
    test();
}
catch(Exception e)
{
   System.out.println(e);
}
}
static void test() throws Exception
{
  try{ throw new chk("Try"); }
  finally { throw new chk("Finally"); }
}
}

/* OUTPUT
 * chk: Finally

 */

  