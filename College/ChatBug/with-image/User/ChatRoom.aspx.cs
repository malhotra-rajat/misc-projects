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

public partial class ChatRoom : System.Web.UI.Page
{
    DateTime EntryTime;
   
    
    protected void Page_Load(object sender, EventArgs e)
    {
        

        if (ViewState["DateTime"] == null)
            ViewState["DateTime"] = System.DateTime.Now;
        else
        { 
            EntryTime=Convert.ToDateTime(ViewState["DateTime"]);
        
        }

        txtMsgBox.Text = "Clock: " + System.DateTime.Now.ToString() + "\nEntry: " + EntryTime +"\n";
    }

  
    protected void lnkLogOut_Click(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId == User.Identity.Name).SingleOrDefault();
        result.StatusValue = 0;
        dc.SubmitChanges();
     
        FormsAuthentication.SignOut();
        Response.Redirect("~/Default.aspx");
    }
    protected void lnkEditAccount_Click(object sender, EventArgs e)
    {
        bool flag;
        if (Request.QueryString["Remember2"] == "True")
            flag = true;
        else
            flag = false;

        FormsAuthentication.SetAuthCookie(Request.QueryString["UserId2"], flag);
        Response.Redirect("~/User/AccountInfo.aspx?UserId1=" + Request.QueryString["UserId2"]+"&Remember3="+Request.QueryString["Remember2"]);
    }
    
    protected void btnSend_Click1(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        CommonRoom NewMsg = new CommonRoom();
        NewMsg.Msg = txtMsg.Text;
        NewMsg.MsgDT = System.DateTime.Now;
        NewMsg.UserId = User.Identity.Name;
        txtMsg.Text = null;
        dc.CommonRooms.InsertOnSubmit(NewMsg);
        dc.SubmitChanges();


    }

    protected void Timer1_Tick(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.CommonRooms.ToList();
        foreach (var r in result)
        {
            if(DateTime.Compare(EntryTime,r.MsgDT)<0)
            txtMsgBox.Text += "\n" + r.UserId + ":  " + r.Msg + "            " + r.MsgDT.ToString();
        }
        BindOnlineUsers();
    }
    protected void UpdatePanel3_Init(object sender, EventArgs e)
    {
        
    }

    private void BindOnlineUsers()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x=>x.StatusValue==1).ToList();
        blOnlineUsers.DataSource = result;
        blOnlineUsers.DataTextField = "UserId";
        blOnlineUsers.DataValueField = "ID";
        blOnlineUsers.DataBind();
    }

}
