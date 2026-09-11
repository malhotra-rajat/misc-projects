<%@ Page Language="C#" AutoEventWireup="true" CodeFile="RegSuc.aspx.cs" Inherits="RegSuc" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Registration Successful ...</title>
    <style type="text/css">
        .style1
        {
            font-family: "Monotype Corsiva";
            font-size: xx-large;
        }
    </style>
</head>
<body background="Images/untitled.bmp">
    <form id="form1" runat="server">
    <p class="style1">
        Registration Succesuful. Please
        <asp:LinkButton ID="LinkButton1" runat="server" PostBackUrl="~/Login.aspx">Click 
        Here</asp:LinkButton>
&nbsp;to go to the Login Page.</p>
    <div>
    
    </div>
    </form>
</body>
</html>
