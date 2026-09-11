<%@ Page Language="C#" AutoEventWireup="true" CodeFile="Login.aspx.cs" Inherits="Login" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Login</title>
    <style type="text/css">
        .style1
        {
            font-size: xx-large;
        }
        .style2
        {
            font-family: "Lucida Handwriting";
        }
    </style>
</head>
<body style="text-align: center" background="Images/untitled.bmp">
    <form id="form1" runat="server">
    <div>
    
        <span class="style1"><span class="style2">Welcome to Gadget Guru ...</span><br />
        <br />
        <span class="style2">LOGIN</span></span><br />
    
    </div>
    <p>
        User Name:&nbsp;&nbsp;
        <asp:TextBox ID="txtUserName" runat="server"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" 
            ErrorMessage="*" ControlToValidate="txtUserName"></asp:RequiredFieldValidator>
    </p>
    <p>
        Password:&nbsp;&nbsp;&nbsp;&nbsp;         <asp:TextBox ID="txtPassword" runat="server" TextMode="Password"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" 
            ErrorMessage="*" ControlToValidate="txtPassword"></asp:RequiredFieldValidator>
    </p>
    <p>
        <asp:Label ID="lblCheck" runat="server" Text="Please try again" Visible="False" 
            ForeColor="Red" Font-Bold="True"></asp:Label>
    </p>
    <p>
        &nbsp;
        <asp:Button ID="btnLogin" runat="server" Height="30px" onclick="btnLogin_Click" 
            Text="Log in" Width="99px" />
    </p>
    <p>
        <asp:LinkButton ID="LinkButton1" runat="server" onclick="Register" 
            CausesValidation="False">Sign Up</asp:LinkButton>
    </p>
    </form>
</body>
</html>
