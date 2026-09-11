<%@ Page Language="C#" AutoEventWireup="true" CodeFile="AccountInfo.aspx.cs" Inherits="User_AccountInfo" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Edit Account</title>
        <style type="text/css">
        .newStyle1
        {
            padding: inherit;
            margin: 15px 0px inherit inherit;
            font-family: "Times New Roman", Times, serif;
            font-size: small;
            font-style: normal;
            font-variant: normal;
            color: #FFFFFF;
            background-color: #F0F0FF;
        }
        .newStyle2
        {
            border-left: thick none #FFFFFF;
            border-right: thick outset #FFFFFF;
            border-top: thick none #FFFFFF;
            border-bottom: thick outset #FFFFFF;
            padding: 10px;
            margin: 10px;
            background-color: #3737FF;
            font-family: "Times New Roman", Times, serif;
            color: #FFFFFF;
            font-size: small;
            font-weight: 400;
            font-style: normal;
            font-variant: normal;
            text-transform: none;
            height: 117px;
            width: 1185px;
        }
        .newStyle3
        {
            font-family: "Comic Sans MS";
            font-size: large;
            font-weight: normal;
            color: #FFFFFF;
            background-color: #0080FF;
            border: thin outset #003300;
            cursor: auto;
            clip: rect(auto, 1px, 2px, 3px);
        }
            .newStyle4
            {
                margin-top: 35px;
                margin-right: 100px;
                border: thin solid #000080;
                height: 660px;
                width: 567px;
                margin-left: 358px;
            }
    </style>

</head>

<script language = "javascript">

    function SaveChange() 
    {
        alert("Changes Saved");
    }



</script>


<body>
    <form id="form1" runat="server">
    
     <div class="newStyle2" style="margin-left: 0px">
    
        <asp:ImageButton ID="ImageButton1" runat="server" BorderColor="White" 
            BorderWidth="1px" Height="64px" ImageUrl="~/images/chatbug.jpg" Width="241px" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
        <br />
    
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:LinkButton ID="lnkChat" runat="server" ForeColor="White" 
             onclick="lnkChat_Click">Enter Chat Room</asp:LinkButton>
        &nbsp;&nbsp;&nbsp;
        <asp:LinkButton ID="lnkLogOut" runat="server" onclick="lnkLogOut_Click" 
             ForeColor="White">Log Out</asp:LinkButton>
         <asp:ScriptManager ID="ScriptManager1" runat="server">
         </asp:ScriptManager>
         <asp:HiddenField ID="hfUserId" runat="server" />
   </div>
    
    <div class="newStyle4">
    
        <br />
        <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblName" runat="server" Text="Name"></asp:Label>
&nbsp;&nbsp;
        <asp:TextBox ID="txtFirstName" runat="server" Height="20px" Width="128px" 
            ValidationGroup="first"></asp:TextBox>
&nbsp;
        <asp:TextBox ID="txtLastName" runat="server" Height="20px" Width="128px" 
            ValidationGroup="first"></asp:TextBox>
        &nbsp;<asp:RequiredFieldValidator ID="rfvFirstName" runat="server" 
                ControlToValidate="txtFirstName" ErrorMessage="*" 
            SetFocusOnError="True" ValidationGroup="first"></asp:RequiredFieldValidator>
        <br />
        <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblGender" runat="server" Text="Gender"></asp:Label>
&nbsp;&nbsp;
        <asp:DropDownList ID="ddlGender" runat="server" 
            onselectedindexchanged="ddlGender_SelectedIndexChanged" 
            ValidationGroup="first">
            <asp:ListItem Value="-1">Select One</asp:ListItem>
            <asp:ListItem Value="0">Male</asp:ListItem>
            <asp:ListItem Value="1">Female</asp:ListItem>
        </asp:DropDownList>
        &nbsp;<asp:RequiredFieldValidator ID="rfvGender" runat="server" 
                ControlToValidate="ddlGender" ErrorMessage="*" InitialValue="-1" 
                SetFocusOnError="True" ValidationGroup="first"></asp:RequiredFieldValidator>
        <br />
        <asp:UpdatePanel ID="UpdatePanel1" runat="server" oninit="UpdatePanel1_Init">
            <ContentTemplate>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <br />
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <asp:Label ID="lblBirthday" runat="server" Text="Birthday"></asp:Label>
                &nbsp;&nbsp;
                <asp:DropDownList ID="ddlYear" runat="server" AutoPostBack="True" Height="20px" 
                    onselectedindexchanged="ddlYear_SelectedIndexChanged" 
                    ValidationGroup="first">
                    <asp:ListItem Value="-1">Year</asp:ListItem>
                </asp:DropDownList>
                &nbsp;&nbsp;<asp:DropDownList ID="ddlMonth" runat="server" AutoPostBack="True" 
                    Height="20px" onselectedindexchanged="ddlMonth_SelectedIndexChanged" 
                    style="height: 22px" ValidationGroup="first">
                    <asp:ListItem Value="-1">Month</asp:ListItem>
                </asp:DropDownList>
                &nbsp;
                <asp:DropDownList ID="ddlDate" runat="server" AutoPostBack="True" Height="20px" 
                    onselectedindexchanged="ddlDate_SelectedIndexChanged" 
                    ValidationGroup="first">
                    <asp:ListItem Value="-1">Date</asp:ListItem>
                </asp:DropDownList>
                <asp:RequiredFieldValidator ID="rfvDate" runat="server" 
                    ControlToValidate="ddlDate" ErrorMessage="*" InitialValue="-1" 
                    SetFocusOnError="True" ValidationGroup="first"></asp:RequiredFieldValidator>
                <br />
            </ContentTemplate>
        </asp:UpdatePanel>
        <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblCountry" runat="server" Text="Country"></asp:Label>
&nbsp;&nbsp;
        <asp:DropDownList ID="ddlCountry" runat="server">
            <asp:ListItem Value="-1">Select One</asp:ListItem>
            <asp:ListItem Value="0">Australia</asp:ListItem>
            <asp:ListItem Value="1">Brazil</asp:ListItem>
            <asp:ListItem Value="2">Canada</asp:ListItem>
            <asp:ListItem Value="3">Denmark</asp:ListItem>
            <asp:ListItem Value="4">England</asp:ListItem>
            <asp:ListItem Value="5">France</asp:ListItem>
            <asp:ListItem Value="6">Germany</asp:ListItem>
            <asp:ListItem Value="7">Holland</asp:ListItem>
            <asp:ListItem Value="8">India</asp:ListItem>
            <asp:ListItem Value="9">Japan</asp:ListItem>
            <asp:ListItem Value="10">Korea</asp:ListItem>
            <asp:ListItem Value="11">Lebanon</asp:ListItem>
            <asp:ListItem Value="12">Malaysia</asp:ListItem>
            <asp:ListItem Value="13">Nepal</asp:ListItem>
            <asp:ListItem Value="14">Oman</asp:ListItem>
            <asp:ListItem Value="15">Pakistan</asp:ListItem>
            <asp:ListItem Value="16">Qatar</asp:ListItem>
            <asp:ListItem Value="17">Russia</asp:ListItem>
            <asp:ListItem Value="18">Singapore</asp:ListItem>
            <asp:ListItem Value="19">Taiwan</asp:ListItem>
            <asp:ListItem Value="20">USA</asp:ListItem>
            <asp:ListItem Value="21">Vatican City</asp:ListItem>
            <asp:ListItem Value="22">Wales</asp:ListItem>
            <asp:ListItem Value="23">Yugoslavia</asp:ListItem>
            <asp:ListItem Value="24">Zimbabwe</asp:ListItem>
        </asp:DropDownList>
            &nbsp;<asp:RequiredFieldValidator ID="rfvCountry" runat="server" 
                ControlToValidate="ddlCountry" ErrorMessage="*" InitialValue="-1" 
                SetFocusOnError="True" ValidationGroup="first"></asp:RequiredFieldValidator>
            <br />
        <br />
&nbsp;
        <asp:Label ID="lblSecQue" runat="server" Text="Security Question"></asp:Label>
&nbsp;&nbsp;
        <asp:TextBox ID="txtSecQue" runat="server" Height="20px" Width="350px" 
            ValidationGroup="first"></asp:TextBox>
            &nbsp;<asp:RequiredFieldValidator ID="rfvSecQue" runat="server" 
                ControlToValidate="txtSecQue" ErrorMessage="*" SetFocusOnError="True" 
            ValidationGroup="first"></asp:RequiredFieldValidator>
            <br />
        <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblAnswer" runat="server" Text="Answer"></asp:Label>
&nbsp;&nbsp;
        <asp:TextBox ID="txtAnswer" runat="server" Height="20px" Width="135px" 
            ValidationGroup="first"></asp:TextBox>
            &nbsp;
            <asp:RequiredFieldValidator ID="rfvAnswer" runat="server" 
                ControlToValidate="txtAnswer" ErrorMessage="*" SetFocusOnError="True" 
            ValidationGroup="first"></asp:RequiredFieldValidator>
            <br />
            <br />
        <asp:UpdatePanel ID="UpdatePanel2" runat="server" oninit="UpdatePanel2_Init">
            <ContentTemplate>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <asp:LinkButton ID="lnkChangePassword" runat="server" 
                    onclick="lnkChangePassword_Click" PostBackUrl="~/User/AccountInfo.aspx">Click 
                to change password</asp:LinkButton>
            </ContentTemplate>
        </asp:UpdatePanel>
&nbsp;<asp:Panel ID="Panel1" runat="server" Enabled="False" oninit="Panel1_Init" 
            Height="131px">
            <br />
            <asp:UpdatePanel ID="UpdatePanel3" runat="server">
                <ContentTemplate>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:Label ID="lblOldPassword" runat="server" Text="Old Password"></asp:Label>
                    &nbsp;&nbsp;
                    <asp:TextBox ID="txtOldPassword" runat="server" AutoPostBack="True" 
                        ontextchanged="txtOldPassword_TextChanged" ValidationGroup="first" 
                        TextMode="Password"></asp:TextBox>
                    &nbsp;<asp:RequiredFieldValidator ID="rfvOldPassword" runat="server" 
                        ControlToValidate="txtOldPassword" Enabled="False" ErrorMessage="*" 
                        ValidationGroup="first"></asp:RequiredFieldValidator>
                    &nbsp;
                    <asp:Label ID="lblMsg" runat="server"></asp:Label>
                    <br />
                    <br />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:Label ID="lblNewPassword" runat="server" Text="New Password"></asp:Label>
                    &nbsp;&nbsp;
                    <asp:TextBox ID="txtNewPassword" runat="server" Enabled="False" 
                        ValidationGroup="first" TextMode="Password"></asp:TextBox>
                    &nbsp;<asp:RequiredFieldValidator ID="rfvNewPassword" runat="server" 
                        ControlToValidate="txtNewPassword" Enabled="False" ErrorMessage="*" 
                        ValidationGroup="first"></asp:RequiredFieldValidator>
                    <br />
                    <br />
                    &nbsp;<asp:Label ID="lblConfirmPassword" runat="server" Text="Confirm Password"></asp:Label>
                    &nbsp;&nbsp;
                    <asp:TextBox ID="txtConfirmPassword" runat="server" Enabled="False" 
                        ValidationGroup="first" TextMode="Password"></asp:TextBox>
                    &nbsp;<asp:RequiredFieldValidator ID="rfvConfirmPassword" runat="server" 
                        ControlToValidate="txtConfirmPassword" Display="Dynamic" Enabled="False" 
                        ErrorMessage="*" ValidationGroup="first"></asp:RequiredFieldValidator>
                    &nbsp;<asp:CompareValidator ID="cvConfirmPassword" runat="server" 
                        ControlToCompare="txtNewPassword" ControlToValidate="txtConfirmPassword" 
                        Enabled="False" ErrorMessage="*" ValidationGroup="first" Display="Dynamic"></asp:CompareValidator>
                </ContentTemplate>
            </asp:UpdatePanel>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <br />
        </asp:Panel>
        <br />
&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:ImageButton ID="btnSaveChanges" runat="server" Height="40px" 
            ImageUrl="~/images/SaveChanges.jpg" Width="145px" 
            onclick="btnSaveChanges_Click" ValidationGroup="first" 
            PostBackUrl="~/User/AccountInfo.aspx"  OnClientClick="SaveChange()" />
        <br />
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;
        <asp:Label ID="lblMsg2" runat="server" Font-Names="Comic Sans MS" 
            Font-Size="Medium" ForeColor="#003300"></asp:Label>
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <br />
        <br />
        <br />
        <br />
        <br />
        <asp:HiddenField ID="hfUserSaved" runat="server" />
        <asp:HiddenField ID="hfRemembered" runat="server" />
        <br />
&nbsp;&nbsp;&nbsp;
    
    </div>
    </form>
</body>
</html>
