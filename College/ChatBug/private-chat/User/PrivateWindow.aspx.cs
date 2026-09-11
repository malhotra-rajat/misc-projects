using System.Collections;
using System;
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

public partial class User_PrivateWindow : System.Web.UI.Page
{
    DateTime EntryTime;


    protected void Page_Load(object sender, EventArgs e)
    {
        Title = Request.QueryString["SenderId"] + " - " + Request.QueryString["ReceiverId"];
        if (ViewState["DateTime"] == null)
            ViewState["DateTime"] = System.DateTime.Now;
        else
        {
            EntryTime = Convert.ToDateTime(ViewState["DateTime"]);

        }

        lblMsg.Text = "Clock: " + System.DateTime.Now.ToString() + "<br/>Entry: " + EntryTime;
        lblUsers.Text = Request.QueryString["SenderId"] + " - " + Request.QueryString["ReceiverId"];
       
    }
    
    protected void btnSend_Click(object sender, EventArgs e)
    {

    }
    protected void txtMsg_TextChanged(object sender, EventArgs e)
    {

    }
    protected void Timer1_Tick(object sender, EventArgs e)
    {
        
    }
    protected void btnSubmit_Click(object sender, EventArgs e)
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        One2OneData data = new One2OneData();
        data.MsgDT = System.DateTime.Now;
        data.One2OneId = Convert.ToInt32( Request.QueryString["ID"]);
        data.SenderId = User.Identity.Name;
        data.Msg = txtMsg.Text;
        dc.One2OneDatas.InsertOnSubmit(data);
        dc.SubmitChanges();
        txtMsg.Text = null;
    }
    protected void Timer1_Tick1(object sender, EventArgs e)
    {   DataBaseDBDataContext dc = new DataBaseDBDataContext();

    var result2 = dc.One2Ones.Where(x => (x.SenderId.ToLower() == User.Identity.Name.ToLower()) || (x.ReceiverId.ToLower() == User.Identity.Name.ToLower()));
        foreach (var r in result2)
        {
            if (User.Identity.Name.ToLower() == r.SenderId.ToLower())
            {
                r.IsAcceptedS = 1;
                r.onlinetimeS = System.DateTime.Now;
            }
            else
            {
                r.IsAcceptedR = 1;
                r.onlinetimeR = System.DateTime.Now;
            }

        
        }
        dc.SubmitChanges();
        
        
        var result = dc.One2OneDatas.ToList();
        foreach (var r in result)                       // Retreiving chat log
        {   
            
            if (DateTime.Compare(EntryTime, r.MsgDT) < 0 && r.One2OneId == Convert.ToInt32(Request.QueryString["ID"]))
                lblMsg.Text += "<br/>" + r.SenderId + ":  " + r.Msg + " " + r.MsgDT.ToString();

        }
       
    }
}
