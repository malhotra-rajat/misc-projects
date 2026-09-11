using System;
using System.Collections;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;

public partial class Welcome : System.Web.UI.Page
{
    
    protected void Page_Load(object sender, EventArgs e)
    {
        welcomeMsg();
        
        
    }

    protected void lnkLogOut_Click(object sender, EventArgs e)
    {
        FormsAuthentication.SignOut();
        
        Response.Redirect("~/Default.aspx");
        
    }

    private void welcomeMsg()
    {
        lblWelcomeText.Text = "Welcome " + Request.QueryString["UserId"];
        if(Request.QueryString["NewUser"]=="True")
        {
            lblCreated.Text = "Your account has been created!";
        }
        else
        {
            lblCreated.Text=null;
        }
    }
    protected void EditAccInfo_Click(object sender, EventArgs e)
    {
        bool flag;
        if(Request.QueryString["Remember"]=="True")
            flag=true;
        else
            flag=false;

        FormsAuthentication.SetAuthCookie(Request.QueryString["UserId"],flag);
        Response.Redirect("~/User/AccountInfo.aspx?UserId1="+Request.QueryString["UserId"]+"&Remember3="+Request.QueryString["Remember"]);
    }
    protected void lnkChatRoom_Click(object sender, EventArgs e)
    {
        bool flag;
        if (Request.QueryString["Remember"] == "True")
            flag = true;
        else
            flag = false;

        
        FormsAuthentication.SetAuthCookie(Request.QueryString["UserId"], flag);
        Response.Redirect("~/User/ChatRoom.aspx?UserId2=" + Request.QueryString["UserId"]+"&Remember2="+Request.QueryString["Remember"]);
    }
}
