<!-- Write a program to determine weather a number is prime or not using jsp. -->

<%!
boolean check(int no)
{
	if(no<=1)
		return false;
	boolean flag=true;
	for(int i=2;i<=no/2;i++)
	{
		if(no%i==0)
		{
			flag=false;
			break;
		}
	}
	return flag;
}
%>
<html>
<head>
<title>Prime Check</title>
</head>
<body>
<form>
<input type="text" name="number" />
<input type="submit" value="Submit"/>
</form>
<%
String n=request.getParameter("number");
if(n!=null)
{
	try
	{
		int no=Integer.parseInt(n);
		boolean c=check(no);
		if(c)
			out.println(no + " is a prime number.");
		else
			out.println(no + " is not a prime number.");
	}
	catch(NumberFormatException e)
	{
		out.println("Enter a valid number only!!");
	}
}
%>
</body>
</html>