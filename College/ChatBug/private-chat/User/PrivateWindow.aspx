<%@ Page Language="C#" AutoEventWireup="true" CodeFile="PrivateWindow.aspx.cs" Inherits="User_PrivateWindow" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
       
    <style type="text/css">
        .newStyle1
        {
            background-color: #3737FF;
            width: 1005px;
        }
        .newStyle2
        {
            position: fixed;
            top: 528px;
            left: 509px;
            height: 44px;
            width: 99px;
        }
    </style>
</head>
<body>
        
    <form id="form1" runat="server">
    <div class="newStyle1">
        
        <asp:Label ID="lblUsers" runat="server" ForeColor="White"></asp:Label>
        <br />
    
    </div>
    <div style="width: 549px">
        <asp:Panel ID="Panel2" runat="server" Height="225px" ScrollBars="Vertical" 
            Width="540px">
            <asp:ScriptManager ID="ScriptManager1" runat="server">
            </asp:ScriptManager>
            <asp:UpdatePanel ID="UpdatePanel3" runat="server">
                <ContentTemplate>
                    <asp:Timer ID="Timer1" runat="server" Interval="50" ontick="Timer1_Tick1">
                    </asp:Timer>
                    <asp:Label ID="lblMsg" runat="server" ForeColor="Red"></asp:Label>
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                </ContentTemplate>
            </asp:UpdatePanel>
        </asp:Panel>
    </div>
    <div>
        &nbsp;<asp:UpdatePanel ID="UpdatePanel2" runat="server" UpdateMode="Conditional">
            <ContentTemplate>
                <asp:TextBox ID="txtMsg" runat="server" Height="57px" TextMode="MultiLine" 
                    Width="450px"></asp:TextBox>
                &nbsp;<asp:Button ID="btnSubmit" runat="server" Height="30px" style="margin-top: 0px" 
                    Text="Send" onclick="btnSubmit_Click" />
                <br />
                <br />
            </ContentTemplate>
        </asp:UpdatePanel>
        <br />
    </div>
    </form>
</body>
</html>
