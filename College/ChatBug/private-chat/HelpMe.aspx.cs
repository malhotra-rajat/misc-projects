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

public partial class User_HelpMe : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    protected void lnkLogin_Click(object sender, EventArgs e)
    {
        Response.Redirect("Default.aspx");
    }
    protected void btnProceed_Click(object sender, EventArgs e)
    {
        validateUser();
    }
    private void validateUser()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId == txtUserId.Text).SingleOrDefault();
        if (result != null)
        {
            lblSecQue.Text = result.SecurityQuestion;
            hfID.Value = result.ID.ToString();
            Panel1.Enabled = true;
            hfAttempts.Value = "4";
        }
        else
        {
            Panel1.Enabled = false;
            lblMsg.Text = "Invalid User ID";
        }
    }
    protected void btnProceed2_Click(object sender, EventArgs e)
    {
        validateAnswer();
    }
    private void NoAttemptsRemaining()
    {
        HttpCookie cookie = new HttpCookie("customvalue");
        cookie["BlockAttempt"] = "1";
        Response.Cookies.Add(cookie);
        cookie.Expires = DateTime.Now.AddMinutes(15);
        Panel3.Enabled = true;
        lblMsg2.Text = "You cannot attempt for the next 15 minutes";
        btnProceed2.Enabled = false;
        Panel3.Enabled = true;
        lnkLogin.Text = "Click Here to Proceed";
        lnkLogin.Enabled = true;
    }
    private void CheckAttempt()
    {
        HttpCookie cookie = Request.Cookies["customvalue"];
        if (cookie != null)
        {
            if (cookie["BlockAttempt"] == "1")
            {
                lblMsg2.Text = "You cannot attempt for the next 15 minutes";
                btnProceed2.Enabled = false;
            }
        }
    }
        
    private void validateAnswer()
    {
        int id = Convert.ToInt32(hfID.Value);
        int count = Convert.ToInt32(hfAttempts.Value);
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.ID == id).SingleOrDefault();
        if (txtAnswer.Text == result.Answer)
            Panel2.Enabled = true;
        else
        {
            count = Convert.ToInt32(hfAttempts.Value);
            lblMsg2.Text = "Wrong Answer, " + count + " attempts remaining";
            count--;
            hfAttempts.Value = count.ToString();
            txtAnswer.Text = null;
            if (Convert.ToInt32(hfAttempts.Value) == -1)
            {
                NoAttemptsRemaining();
                lblMsg2.Text = "You cannot attempt for the next 15 minutes";
                btnProceed2.Enabled = false;
            }
        }
        Panel3.Enabled = false;
    }

    protected void btnChangePassword_Click(object sender, EventArgs e)
    {
        Panel3.Enabled = false;
        Changepassword();
        
    }
    private void Changepassword()
    {
        int id = Convert.ToInt32(hfID.Value);
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.ID == id).SingleOrDefault();
        result.Password = txtPassword.Text;
        dc.SubmitChanges();
        lblMsg3.Text = "Password Changed";
        Panel3.Enabled = true;
        
    }
}
