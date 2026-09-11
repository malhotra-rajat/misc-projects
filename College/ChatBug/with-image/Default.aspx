<%@ Page Language="C#" AutoEventWireup="true"  CodeFile="Default.aspx.cs" Inherits="_Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Login To ChatBug</title>
    <style type="text/css">
        .newStyle1
        {
            padding: inherit;
            font-family: "Times New Roman", Times, serif;
            font-size: small;
            font-style: italic;
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
            height: 148px;
            width: 1284px;
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
    </style>
</head>
<body>
    <form id="form1" defaultbutton="btnLogin" runat="server">
    <div class="newStyle2" style="margin-left: 0px">
    
        <asp:ImageButton ID="ImageButton1" runat="server" BorderColor="White" 
            BorderWidth="1px" Height="64px" ImageUrl="~/images/chatbug.jpg" Width="241px" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         <asp:Label ID="lblUserId" runat="server" Font-Names="Times New Roman" 
            Font-Size="Small" Text="User Id"></asp:Label>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblPassword" runat="server" Text="Password"></asp:Label>
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtUserId" runat="server" Width="187px" 
            ontextchanged="txtUserId_TextChanged" ValidationGroup="first"> </asp:TextBox>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtPassword" runat="server" TextMode="Password" Width="149px" ValidationGroup="first"></asp:TextBox>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Button ID="btnLogin" runat="server" BackColor="#575BF0" 
            BorderStyle="Outset" BorderWidth="1px" Font-Bold="True" ForeColor="White" 
            Height="23px"  ValidationGroup="first" Text="Login" 
            onclick="btnLogin_Click" ToolTip="Click here to login" />
        <br />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
        <asp:CheckBox ID="chkbxKeepMeLoggedIn" runat="server" 
            Text="Keep me logged in" ToolTip="Keeps you logged in for 15 days" />
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
        <asp:HyperLink ID="HyperLink1" runat="server" 
            ToolTip="Problem logging in? Click here" NavigateUrl="~/HelpMe.aspx" 
            ForeColor="White">Help me access my account</asp:HyperLink>
        <br />
    
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:RequiredFieldValidator ID="rfvUserId" runat="server" 
            ControlToValidate="txtUserId" ErrorMessage="Enter User Id" 
            ValidationGroup="first" Display="Dynamic"></asp:RequiredFieldValidator>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" 
            ControlToValidate="txtPassword" ErrorMessage="Enter Password" 
            SetFocusOnError="True" ValidationGroup="first" Display="Dynamic"></asp:RequiredFieldValidator>
&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblLoginValidate" runat="server" Font-Size="Small" 
            ForeColor="Red"></asp:Label>
        <br />
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <div class="newStyle1" style="margin-left: 0px; width: 617px; margin-top: 31px;">
    
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblNew" runat="server" Font-Names="Comic Sans MS" 
            Font-Size="Large" ForeColor="#000099" Text="New to Chat Bug?"></asp:Label>
            &nbsp;&nbsp;
        <asp:Label ID="lblSignUp" runat="server" Font-Bold="True" 
            Font-Names="Times New Roman" Font-Size="X-Large" ForeColor="#000099" 
            Text="Sign Up"></asp:Label>
            <asp:ScriptManager ID="ScriptManager1" runat="server">
            </asp:ScriptManager>
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblFree" runat="server" Font-Names="Comic Sans MS" 
            Font-Size="Medium" ForeColor="#000099" Text="It's Free and Easy"></asp:Label>
        <br />
        <br />
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblName" runat="server" ForeColor="#000099" Text="Name"></asp:Label>
            &nbsp;&nbsp;
        <asp:TextBox ID="txtFirstName" runat="server" Height="20px" 
            style="margin-top: 0px"></asp:TextBox>
            &nbsp;
        <asp:TextBox ID="txtLastName" runat="server" Height="20px" 
            style="margin-top: 0px"></asp:TextBox>
            &nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvFirstName" runat="server" 
                ControlToValidate="txtFirstName" ErrorMessage="*" SetFocusOnError="True" 
                ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
            <asp:RegularExpressionValidator ID="revFirstName" runat="server" 
                ControlToValidate="txtFirstName" Display="Dynamic" ErrorMessage="(a-z  A-Z)" 
                ValidationExpression="[a-z A-Z .]+" ValidationGroup="second" 
                ForeColor="#990000"></asp:RegularExpressionValidator>
&nbsp;<asp:RegularExpressionValidator ID="revLastName" runat="server" 
                ControlToValidate="txtLastName" Display="Dynamic" ErrorMessage="(a-z  A-Z)" 
                ValidationExpression="[a-z A-Z .]+" ValidationGroup="second" 
                ForeColor="Maroon"></asp:RegularExpressionValidator>
            <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblGender" runat="server" ForeColor="#000099" Text="Gender"></asp:Label>
            &nbsp;
        <asp:DropDownList ID="ddlGender" runat="server">
            <asp:ListItem Value="-1">Select One</asp:ListItem>
            <asp:ListItem Value="0">Male</asp:ListItem>
            <asp:ListItem Value="1">Female</asp:ListItem>
        </asp:DropDownList>
            &nbsp;&nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvGender" runat="server" 
                ControlToValidate="ddlGender" ErrorMessage="*" InitialValue="-1" 
                SetFocusOnError="True" ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
            <asp:UpdatePanel ID="UpdatePanel2" runat="server" oninit="UpdatePanel2_Init">
                <ContentTemplate>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <br />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:Label ID="lblBirthday" runat="server" ForeColor="#000099" 
    Text="Birthday"></asp:Label>
                    &nbsp;<asp:DropDownList ID="ddlYear" runat="server" 
                        onselectedindexchanged="ddlYear_SelectedIndexChanged" AutoPostBack="True" 
                        Height="20px">
                        <asp:ListItem Value="-1">Year</asp:ListItem>
                    </asp:DropDownList>
                    &nbsp;<asp:DropDownList ID="ddlMonth" runat="server" onselectedindexchanged="ddlMonth_SelectedIndexChanged"
                        style="height: 22px" AutoPostBack="True" Height="20px">
                        <asp:ListItem Value="-1">Month</asp:ListItem>
                    </asp:DropDownList>
                    <asp:DropDownList ID="ddlDate" runat="server" Height="20px" AutoPostBack="True" 
                        ValidationGroup="second">
                        <asp:ListItem Value="-1">Date</asp:ListItem>
                    </asp:DropDownList>
                    &nbsp;&nbsp;&nbsp;
                    <asp:RequiredFieldValidator ID="rfvDate" runat="server" 
                        ControlToValidate="ddlDate" ErrorMessage="*" InitialValue="-1" 
                        SetFocusOnError="True" ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
                </ContentTemplate>
            </asp:UpdatePanel>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; &nbsp;&nbsp;
            <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblCountry" runat="server" ForeColor="#000099" Text="Country"></asp:Label>
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
            &nbsp;&nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvCountry" runat="server" 
                ControlToValidate="ddlCountry" ErrorMessage="*" InitialValue="-1" 
                SetFocusOnError="True" ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
            <asp:UpdatePanel ID="UpdatePanel3" runat="server">
                <ContentTemplate>
                    <p style="margin-left: 80px">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <asp:Label ID="lblUserId2" runat="server" ForeColor="#000099" Text="User Id"></asp:Label>
                        &nbsp;&nbsp;
                        <asp:TextBox ID="txtUserId2" runat="server" AutoPostBack="True" Height="20px" 
                            ontextchanged="txtUserId2_TextChanged" style="margin-top: 0px" Width="151px"></asp:TextBox>
                        &nbsp;&nbsp;&nbsp;
                        <asp:RequiredFieldValidator ID="rfvUserId2" runat="server" 
                            ControlToValidate="txtUserId2" ErrorMessage="*" SetFocusOnError="True" 
                            ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
                        &nbsp;&nbsp;&nbsp;
                        <asp:Label ID="lblAvailable" runat="server"></asp:Label>
                        &nbsp;<asp:RegularExpressionValidator ID="revUserId" runat="server" 
                            ControlToValidate="txtUserId2" ErrorMessage="(a-z  A-Z  0-9  _  . )" 
                            ValidationExpression="[a-z A-Z 0-9 _ .]+" ValidationGroup="second" 
                            ForeColor="Maroon"></asp:RegularExpressionValidator>
                    </p>
                </ContentTemplate>
            </asp:UpdatePanel>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblPassword2" runat="server" ForeColor="#000099" Text="Password"></asp:Label>
            &nbsp;&nbsp;
        <asp:TextBox ID="txtPassword2" runat="server" Height="20px" 
            style="margin-top: 0px" TextMode="Password"></asp:TextBox>
            &nbsp;&nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvPassword2" runat="server" 
                ControlToValidate="txtPassword2" ErrorMessage="*" SetFocusOnError="True" 
                ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblRePassword" runat="server" ForeColor="#000099" 
            Text="Re-enter Password"></asp:Label>
            &nbsp;&nbsp;
        <asp:TextBox ID="txtRePassword" runat="server" Height="20px" 
            style="margin-top: 0px" TextMode="Password"></asp:TextBox>
            &nbsp;&nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvRePassword" runat="server" 
                ControlToValidate="txtRePassword" ErrorMessage="*" SetFocusOnError="True" 
                ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
            <asp:CompareValidator ID="cvRePassword" runat="server" 
                ControlToCompare="txtPassword2" ControlToValidate="txtRePassword" 
                ErrorMessage="Password does not match" SetFocusOnError="True" 
                ValidationGroup="second" ForeColor="Maroon"></asp:CompareValidator>
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblInCase" runat="server" Font-Names="Comic Sans MS" 
            Font-Size="Medium" ForeColor="#000099" Text="In case you forget your password"></asp:Label>
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblSecQue" runat="server" ForeColor="#000099" 
            Text="Security Question"></asp:Label>
            &nbsp;&nbsp;
        <asp:TextBox ID="txtSecQue" runat="server" Height="20px" Width="350px"></asp:TextBox>
            &nbsp;&nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvSecQue" runat="server" 
                ControlToValidate="txtSecQue" ErrorMessage="*" SetFocusOnError="True" 
                ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblAnswer" runat="server" ForeColor="#000099" Text="Answer"></asp:Label>
            &nbsp; &nbsp;
        <asp:TextBox ID="txtAnswer" runat="server" Height="20px" Width="135px"></asp:TextBox>
            &nbsp;&nbsp;&nbsp;
            <asp:RequiredFieldValidator ID="rfvAnswer" runat="server" 
                ControlToValidate="txtAnswer" ErrorMessage="*" SetFocusOnError="True" 
                ValidationGroup="second" ForeColor="Maroon"></asp:RequiredFieldValidator>
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <asp:UpdatePanel ID="UpdatePanel1" runat="server" oninit="UpdatePanel1_Init">
                <ContentTemplate>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:CheckBox ID="chkbxAccept" runat="server" AutoPostBack="True" 
                        ForeColor="#000099" Text="I accept" 
                        oncheckedchanged="chkbxAccept_CheckedChanged" />
                    &nbsp;<asp:HyperLink ID="HyperLink2" runat="server" Font-Underline="True" 
                        ForeColor="#000099" ToolTip="Click to view ChatBug Terms and Conditions" 
                        NavigateUrl="~/privacy.html">ChatBug Terms and Conditions</asp:HyperLink>
                    <br />
                    <br />
                    <br />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:Button ID="btnCreate" runat="server" BackColor="#009933" 
                        BorderStyle="Outset" CssClass="newStyle3" ForeColor="White" Height="49px" 
                        onclick="btnCreate_Click" Text="Create My Account &gt;&gt;" 
                        ValidationGroup="second" Width="213px" ToolTip="Click here to proceed" />
                    <br />
                </ContentTemplate>
            </asp:UpdatePanel>
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <br />
        <br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    
    </div>
    
    </div>
    &nbsp;&nbsp;<div 
        
        style="height: 715px; margin-left: 630px; margin-right: 0px; margin-top: 13px; width: 663px;">
        <img src="images/backgroundbug.jpg" 
            
            
            style="width: 666px; height: 689px; visibility: visible; display: run-in; margin-top: 28px;" /></div>
    &nbsp;</form>
</body>
</html>
