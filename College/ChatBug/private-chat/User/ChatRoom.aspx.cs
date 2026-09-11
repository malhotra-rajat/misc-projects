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
           DataBaseDBDataContext dc = new DataBaseDBDataContext();
           var result = dc.UserDatas.Where(x => x.UserId == User.Identity.Name).SingleOrDefault();
           if (result != null)
           {
               result.StatusValue = 1;
               dc.SubmitChanges();
           }

            if(!IsPostBack)
             BindOnlineUsersDDL();
      
            
            if (ViewState["DateTime"] == null)
            ViewState["DateTime"] = System.DateTime.Now;
        else
        { 
            EntryTime=Convert.ToDateTime(ViewState["DateTime"]);
        
        }

        lblMsg.Text = "Clock: " + System.DateTime.Now.ToString() + "<br/>Entry: " + EntryTime;
    }

  


    protected void lnkLogOut_Click(object sender, EventArgs e)
    {
        FormsAuthentication.SignOut();
        Response.Redirect("~/Default.aspx");
    }
    protected void lnkEditAccount_Click(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId == User.Identity.Name).SingleOrDefault();
        result.StatusValue = 0;
        dc.SubmitChanges();

        
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
        if (txtMsg.Text.Length>=1)
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

    }

    protected void Timer1_Tick(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
                                                                
       var result2 = dc.One2Ones.ToList();           
       foreach( var t in result2)                                   
        {
            if (System.DateTime.Compare(System.DateTime.Now, Convert.ToDateTime(t.onlinetimeR).AddSeconds(2)) > 0 && System.DateTime.Compare(System.DateTime.Now, Convert.ToDateTime(t.onlinetimeS).AddSeconds(2)) > 0)
             dc.One2Ones.DeleteOnSubmit(t);
        }   dc.SubmitChanges();
       
            

        var result1 = dc.UserDatas.Where(x => x.UserId.ToLower() == User.Identity.Name.ToLower()).SingleOrDefault();
        result1.onlinetime = System.DateTime.Now;
        dc.SubmitChanges();

        var result = dc.CommonRooms.ToList();
        foreach (var r in result)                       // Retreiving chat log
        {
            if(DateTime.Compare(EntryTime,r.MsgDT)<0)
            lblMsg.Text += "<br/>" + r.UserId + ":  " + r.Msg + " " + r.MsgDT.ToString();
        }
        
        BindOnlineUsersBL();      // display users
        WindowCheck();

    }
    protected void UpdatePanel3_Init(object sender, EventArgs e)
    {
        
    }

    private void BindOnlineUsersBL()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x=>x.StatusValue==1).ToList();

        foreach (var r in result)
        {
         
            if (DateTime.Compare( System.DateTime.Now, Convert.ToDateTime( r.onlinetime).AddSeconds(2)) < 0)
            {
                
            }
            else
            {
                r.StatusValue = 0;
                dc.SubmitChanges();
            }
        }
        blOnlineUsers.DataSource = result;
        blOnlineUsers.DataTextField = "UserId";
        blOnlineUsers.DataValueField = "ID";
        blOnlineUsers.DataBind();
    }

    private void BindOnlineUsersDDL()
    {
        ddlOnlineUsers.Items.Clear();
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.StatusValue == 1 && x.UserId.ToLower() != User.Identity.Name.ToLower()).ToList();

        foreach (var r in result)
        {
            if (DateTime.Compare(System.DateTime.Now, Convert.ToDateTime(r.onlinetime).AddSeconds(2)) < 0)
            {

            }
            else
            {
                r.StatusValue = 0;
                dc.SubmitChanges();
            }
        }
        ddlOnlineUsers.DataSource = result;
        ddlOnlineUsers.DataTextField = "UserId";
        ddlOnlineUsers.DataValueField = "ID";
        ddlOnlineUsers.DataBind();
       
    }

    protected void blOnlineUsers_Click(object sender, BulletedListEventArgs e)
    {
        
    }
    protected void btnOpen_Click(object sender, EventArgs e)
    {
        
    }


    protected void btnRefresh_Click(object sender, EventArgs e)
    {

        BindOnlineUsersDDL();
    }
    protected void btnInvite_Click(object sender, EventArgs e)
    {
        if(ddlOnlineUsers.SelectedValue.ToString().Length>0)
            PopUp();     
        
    }

 
    private void PopUp()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.One2Ones.ToList();
        string Sender = User.Identity.Name;
        string Receiver = ddlOnlineUsers.SelectedItem.ToString();
        int ID1;
        bool flag = true;


        foreach (var r in result)
        {

            if ((Sender.ToLower() == r.SenderId.ToLower() && Receiver.ToLower() == r.ReceiverId.ToLower()) || (Receiver.ToLower() == r.SenderId.ToLower() && Sender.ToLower() == r.ReceiverId.ToLower()))
            {
                ID1 = r.ID;
                flag = false;

                string WinName = ID1.ToString();
                string openPage = "PrivateWindow.aspx?SenderId=" + Sender + "&ReceiverId=" + Receiver + "&ID=" + ID1.ToString();
                Response.Write("<script language='javascript' > window.open('" + openPage + "', '" + WinName + "', 'left=20, top=20, width=550, height=350');</script>");
                break;
            }
        }
        

        if (flag == true)
        {
            One2One NewMsg = new One2One();
            NewMsg.SenderId = User.Identity.Name;
            NewMsg.ReceiverId = ddlOnlineUsers.SelectedItem.ToString();
            NewMsg.DT = System.DateTime.Now;
            NewMsg.IsAcceptedR = 0;
            NewMsg.IsAcceptedS = 1;
            NewMsg.onlinetimeR = System.DateTime.Now;
            NewMsg.onlinetimeS = System.DateTime.Now;
            dc.One2Ones.InsertOnSubmit(NewMsg);
            dc.SubmitChanges();
            ID1 = NewMsg.ID;
            string WinName = ID1.ToString();
            string openPage = "PrivateWindow.aspx?SenderId=" + Sender + "&ReceiverId=" + Receiver + "&ID=" + ID1.ToString();
            Response.Write("<script language='javascript' > window.open('" + openPage + "', '" + WinName + "', 'left=20, top=20, width=550, height=350,location=0,status=0');</script>");
        }

        
        

    }

  
    protected void lnkLogOut_Click1(object sender, EventArgs e)
    {

        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId == User.Identity.Name).SingleOrDefault();
        result.StatusValue = 0;
        dc.SubmitChanges();


        FormsAuthentication.SignOut();
        Response.Redirect("Default.aspx");
    }
    protected void txtMsg_TextChanged(object sender, EventArgs e)
    {
        
    }
    protected void UpdatePanel3_Unload(object sender, EventArgs e)
    {
        
    }


    private void WindowCheck()
    {
        DataBaseDBDataContext dc2 = new DataBaseDBDataContext();
        var result2 = dc2.One2Ones.ToList();                     // CHECK FOR WINDOW AND UPDATE THE LABEL
        int WindowCount = 0;
        foreach (var s in result2)
        {
            if (User.Identity.Name.ToLower() == s.SenderId.ToLower() && System.DateTime.Compare(System.DateTime.Now,Convert.ToDateTime(s.onlinetimeS).AddSeconds(2))>0)
                s.IsAcceptedS=0;
            else if (User.Identity.Name.ToLower() == s.ReceiverId.ToLower() && System.DateTime.Compare(System.DateTime.Now, Convert.ToDateTime(s.onlinetimeR).AddSeconds(2)) > 0)
                s.IsAcceptedR=0;
        }
        dc2.SubmitChanges();
        foreach (var r in result2)
        {
            int IsAccepted=0;
            if (User.Identity.Name.ToLower() == r.SenderId.ToLower())
                IsAccepted = r.IsAcceptedS;
            else if(User.Identity.Name.ToLower()==r.ReceiverId.ToLower())
                IsAccepted = r.IsAcceptedR;


            if ((r.ReceiverId == User.Identity.Name || r.SenderId == User.Identity.Name) && IsAccepted == 0)
                WindowCount++;
            
        }

        if (WindowCount != 0)
        {

            lblChkWindow.Text = "You Have " + WindowCount.ToString() + " Private chat requests!";


        }
        else 
        {
            lblChkWindow.Text = "You Don't have any Private Chat Requests!";
        }

    
    }
    protected void btnAcceptWindows_Click(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.One2Ones.ToList();
        foreach (var r in result)
        {
            int IsAccepted=0;
            if (User.Identity.Name.ToLower() == r.SenderId.ToLower())
                IsAccepted = r.IsAcceptedS;
            else if (User.Identity.Name.ToLower() == r.ReceiverId.ToLower())
                IsAccepted = r.IsAcceptedR;


            if ((r.ReceiverId == User.Identity.Name || r.SenderId == User.Identity.Name) && IsAccepted == 0)
            {
                string WinName = r.ID.ToString();
                string openPage = "PrivateWindow.aspx?SenderId=" + r.SenderId + "&ReceiverId=" + r.ReceiverId + "&ID=" + r.ID.ToString();
                Response.Write("<script language='javascript' > window.open('" + openPage + "', '" + WinName + "', 'left=20, top=20, width=550, height=350,location=0,status=0');</script>");

                if (User.Identity.Name.ToLower() == r.SenderId.ToLower())
                    r.IsAcceptedS = 1;
                else if (User.Identity.Name.ToLower() == r.ReceiverId.ToLower())
                    r.IsAcceptedR = 1;

             }
                     
        }
        dc.SubmitChanges();
    }
}
