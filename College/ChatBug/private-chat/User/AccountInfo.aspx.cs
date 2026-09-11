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

public partial class User_AccountInfo : System.Web.UI.Page
{

    protected void Page_Load(object sender, EventArgs e)
    {
        if (ViewState["Remember"] != null)
            lblMsg2.Text = "Changes Saved!";
        
        if (ViewState["Remember"] == null)
        {
            ViewState["Remember"] = Request.QueryString["Remember3"];
        }

      

        if (!IsPostBack)
        {
            hfUserId.Value = Convert.ToString(Request.QueryString["UserId1"]);

            ddlMonth.Enabled = true;
            ddlDate.Enabled = true;
            ddYear();
            ddMonth();

            int Days;
            if (ddlMonth.SelectedValue == "2")
            {
                if (Convert.ToInt32(ddlYear.SelectedValue) % 4 == 0)
                    Days = 29;
                else
                    Days = 28;
            }
            else
            {
                if (ddlMonth.SelectedValue == "1" || ddlMonth.SelectedValue == "3" || ddlMonth.SelectedValue == "5" || ddlMonth.SelectedValue == "7" || ddlMonth.SelectedValue == "8" || ddlMonth.SelectedValue == "10" || ddlMonth.SelectedValue == "12")
                    Days = 31;
                else
                    Days = 30;
            }
            ddDate(Days);
            LoadUserData();

            
        }
        
        

    }
    protected void lnkLogOut_Click(object sender, EventArgs e)
    {
        FormsAuthentication.SignOut();
        Response.Redirect("~/Default.aspx");
    }
   
  
    protected void ddlYear_SelectedIndexChanged(object sender, EventArgs e)
    {
        ddlMonth.Enabled = true;
        
    }
    protected void ddlMonth_SelectedIndexChanged(object sender, EventArgs e)
    {
        ddlDate.Enabled = true;
    }
    protected void ddlDate_SelectedIndexChanged(object sender, EventArgs e)
    {

    }
    protected void ddlCountry_SelectedIndexChanged(object sender, EventArgs e)
    {

    }
    protected void ddlGender_SelectedIndexChanged(object sender, EventArgs e)
    {

    }

    private void LoadUserData()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId == Request.QueryString["UserId1"]).SingleOrDefault();
        if (result != null)
        {
            
            txtFirstName.Text = result.FirstName;
            txtLastName.Text = result.LastName;
            txtSecQue.Text = result.SecurityQuestion;
            txtAnswer.Text = result.Answer;
            ddlCountry.SelectedIndex = result.CountryIndex;
            ddlGender.SelectedIndex = result.GenderIndex;
            ddlDate.SelectedIndex = result.BDate;
            ddlMonth.SelectedIndex = result.BMonth;
            ddlYear.SelectedIndex = result.Byear;
        }
    }

    private void ddYear()
    {
        ddlMonth.Enabled = false;
        ddlDate.Enabled = false;
            ddlYear.Items.Clear();
            for (int i = 2000; i >= 1950; i--)
            {
                ListItem itm = new ListItem();
                itm.Text = i.ToString();
                itm.Value = i.ToString();
                ddlYear.Items.Add(itm);
            }
            ddlYear.Items.Insert(0, new ListItem("Year", "-1"));
       
    }
    private void ddDate(int days)
    {
            ddlDate.Items.Clear();
            for (int i = 1; i <= days; i++)
            {
                ListItem itm = new ListItem();
                itm.Text = i.ToString();
                itm.Value = i.ToString();
                ddlDate.Items.Add(itm);
            }
            ddlDate.Items.Insert(0, new ListItem("Date", "-1"));
       
    }
    private void ddMonth()
    {
            ddlMonth.Items.Clear();
            for (int i = 1; i <= 12; i++)
            {
                ListItem itm1 = new ListItem();
                itm1.Text = i.ToString();
                itm1.Value = i.ToString();
                ddlMonth.Items.Add(itm1);
            }
            ddlMonth.Items.Insert(0, new ListItem("Month", "-1"));
       
    }


    protected void UpdatePanel1_Init(object sender, EventArgs e)
    {   
    }
    protected void btnSaveChanges_Click(object sender, ImageClickEventArgs e)
    {
        UpdateUser();
        string RememberVal=Convert.ToString(ViewState["Remember"]);
        lblMsg2.Text = "Changes Saved";
        System.Threading.Thread.Sleep(5000);
        Response.Redirect("~/User/AccountInfo.aspx?UserId1=" + User.Identity.Name + "&Remember3="+RememberVal); 
    }

    private void UpdateUser()
     {
         DataBaseDBDataContext dc = new DataBaseDBDataContext();
         var user = dc.UserDatas.Where(x => x.UserId == hfUserId.Value).SingleOrDefault();
         if (user != null)
         {
             user.FirstName = txtFirstName.Text;
             user.LastName = txtLastName.Text;
             user.Gender = ddlGender.SelectedItem.ToString();
             user.GenderIndex = ddlGender.SelectedIndex;
             user.Birthday = ddlMonth.SelectedValue + "/" + ddlDate.SelectedValue + "/" + ddlYear.SelectedValue;
             user.BDate = ddlDate.SelectedIndex;
             user.BMonth = ddlMonth.SelectedIndex;
             user.Byear = ddlYear.SelectedIndex;
             user.Country = ddlCountry.SelectedItem.ToString();
             user.CountryIndex = ddlCountry.SelectedIndex;
             user.SecurityQuestion = txtSecQue.Text;
             user.Answer = txtAnswer.Text;

             if (Panel1.Enabled == true)
             {
                 user.Password = txtNewPassword.Text;
             }

             dc.SubmitChanges();
         }   
     }
      
    protected void lnkChangePassword_Click(object sender, EventArgs e)
    {
        EnableDisablePasswordChange();
     
    }
    protected void Panel1_Init(object sender, EventArgs e)
    {
        
    }
    protected void UpdatePanel2_Init(object sender, EventArgs e)
    {
     
    }

    private void EnableDisablePasswordChange()
    {
        if (Panel1.Enabled == true)
        {
            
            Panel1.Enabled = false;
            lnkChangePassword.Text = "Click to change password";
            txtOldPassword.Text = null;
            txtNewPassword.Text = null;
            txtConfirmPassword.Text = null;
            rfvOldPassword.Enabled = false;
            rfvNewPassword.Enabled = false;
            rfvConfirmPassword.Enabled = false;
            cvConfirmPassword.Enabled = false;
            lblMsg.Text = null;
           
        }
        else
        {
           
            Panel1.Enabled = true;
            lnkChangePassword.Text = "Click to Disable Password Change";
            txtOldPassword.Enabled = true;
            rfvOldPassword.Enabled = true;
            rfvNewPassword.Enabled = true;
            rfvConfirmPassword.Enabled = true;
            cvConfirmPassword.Enabled = true;
        
           
           

        }
    
    }

    protected void txtOldPassword_TextChanged(object sender, EventArgs e)
    {  
    
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId == hfUserId.Value && x.Password==txtOldPassword.Text).SingleOrDefault();
        if (result != null)
        {
            txtNewPassword.Enabled = true;
            txtConfirmPassword.Enabled = true;
            lblMsg.Text = "Enter New Password";
            lblMsg.ForeColor = System.Drawing.Color.Green;
          
        }
        else 
        {
            lblMsg.Text = "Invalid Password!";
            lblMsg.ForeColor = System.Drawing.Color.Red;
            txtNewPassword.Enabled = false;
            txtConfirmPassword.Enabled = false;
          
        }
    }
    protected void btnSignOut_Click(object sender, EventArgs e)
    {
        FormsAuthentication.SignOut();
        Response.Redirect("~/Default.aspx");

    }
    protected void lnkChat_Click(object sender, EventArgs e)
    {
        bool flag;
        if (Request.QueryString["Remember3"] == "True")
            flag = true;
        else
            flag = false;

        FormsAuthentication.SetAuthCookie(Request.QueryString["UserId1"], flag);
        Response.Redirect("~/User/ChatRoom.aspx?UserId2=" + Request.QueryString["UserId1"] + "&Remember2=" + Request.QueryString["Remember3"]);    
    }

    
}
