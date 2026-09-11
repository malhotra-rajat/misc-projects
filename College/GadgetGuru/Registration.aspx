<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Registration.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Registration Page</title>
    <style type="text/css">
        .style1
        {
            font-size: xx-large;
            font-family: "Lucida Handwriting";
        }
    </style>
</head>
<body style="text-align: center" background="Images/untitled.bmp">
    <form id="form2" runat="server">
    <p class="style1" style="text-align: center">
        Sign Up for Gadget Guru</p>
    <p>
        First Name:&nbsp;
        <asp:TextBox ID="TextBox1" runat="server"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" 
            ControlToValidate="TextBox1" ErrorMessage="*"></asp:RequiredFieldValidator>
    </p>
    <p>
        Last Name:&nbsp;
        <asp:TextBox ID="TextBox2" runat="server"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" 
            ControlToValidate="TextBox2" ErrorMessage="*"></asp:RequiredFieldValidator>
    </p>
    <p>
        E-Mail Address:&nbsp;
        <asp:TextBox ID="TextBox3" runat="server"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator5" runat="server" 
            ControlToValidate="TextBox3" Display="Dynamic" ErrorMessage="*"></asp:RequiredFieldValidator>
        <asp:RegularExpressionValidator ID="RegularExpressionValidator1" runat="server" 
            ControlToValidate="TextBox3" Display="Dynamic" 
            ErrorMessage="Enter correct E-mail Address" 
            ValidationExpression="\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*"></asp:RegularExpressionValidator>
    </p>
    <p>
        User Name:&nbsp;
        <asp:TextBox ID="TextBox4" runat="server" AutoPostBack="True" 
            ontextchanged="TextBox4_TextChanged"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator3" runat="server" 
            ControlToValidate="TextBox4" ErrorMessage="*"></asp:RequiredFieldValidator>
        <asp:Label ID="lblInvalid" runat="server" Text="User Name already exists" 
            Visible="False" ForeColor="Red"></asp:Label>
    </p>
    <p>
        Password:&nbsp;
        <asp:TextBox ID="TextBox5" runat="server" TextMode="Password"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator4" runat="server" 
            ControlToValidate="TextBox5" ErrorMessage="*"></asp:RequiredFieldValidator>
    </p>
    <p>
        Re-enter Password:&nbsp;&nbsp;
        <asp:TextBox ID="TextBox6" runat="server" TextMode="Password" Height="22px" 
            Width="128px"></asp:TextBox>
        <asp:RequiredFieldValidator ID="RequiredFieldValidator7" runat="server" 
            ControlToValidate="TextBox6" Display="Dynamic" ErrorMessage="*"></asp:RequiredFieldValidator>
        <asp:CompareValidator ID="CompareValidator1" runat="server" 
            ControlToCompare="TextBox5" ControlToValidate="TextBox6" 
            ErrorMessage="Passwords do not match" Display="Dynamic"></asp:CompareValidator>
    </p>
    <p style="text-align: center">
        &nbsp;
        <asp:Button ID="Button1" runat="server" onclick="Button1_Click" Text="Submit" 
            style="text-align: left" />
    </p>
    <p style="text-align: center">
        &nbsp;</p>
    <br />
    <br />
    </form>
</body>
</html>
