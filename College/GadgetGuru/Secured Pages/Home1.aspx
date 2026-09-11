<%@ Page Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="Home1.aspx.cs" Inherits="Home1" Title="Welcome to Gadget Guru" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
    <style type = "text/css">
    .style3
    {
        font-size: xx-large;
        font-family: "Monotype Corsiva";
        margin-left: 400px;
    }
    
   
    .style4
    {
        font-size: xx-large;
        font-family: "Monotype Corsiva";
        text-align: center;
    }
    
   
</style>
    
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">
    <p class="style4">
        Welcome to Gadget Guru, India&#39;s best website </p>
    <p class="style4">
        for electronic gadget reviews ...</p>
    <p class="style4">
        <asp:Image ID="Image1" runat="server" ImageUrl="~/Images/gadgets.jpg" />
    </p>
<br />
</asp:Content>

