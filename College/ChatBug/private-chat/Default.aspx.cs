using System;
using System.Configuration;
using System.Data;
using System.Text.RegularExpressions;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;


public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {
             EnablebtnCreate();  //to enable or disable the create my account button//
             string LoggedUser=User.Identity.Name;    // get the name of currently logged in user //
         
            
          if (User.Identity.IsAuthenticated==true)    // checks whether or not he is authenticated//
             {                                        // if yes, he is redirected to the home page//   
           
                 FormsAuthentication.SetAuthCookie(LoggedUser, true);
                 Response.Redirect("~/User/Welcome.aspx?UserId=" + LoggedUser.ToString() + "&newuser=False&Remember=True");
             }
             
    }
    private void EnablebtnCreate()
    {
        if (chkbxAccept.Checked == true)
        {
            btnCreate.Enabled = true;
        }
        else
        {
            btnCreate.Enabled = false;
        }

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
    
    protected void btnCreate_Click(object sender, EventArgs e)
    {
        AddUser();
        Response.Write("About to redirect!");
        FormsAuthentication.SetAuthCookie(txtUserId2.Text, false);
        Response.Redirect("~/User/Welcome.aspx?UserId=" + txtUserId2.Text+"&NewUser=True&Remember=false");
    }
    private void AddUser()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        UserData user = new UserData();
        user.FirstName = txtFirstName.Text;
        user.LastName = txtLastName.Text;
        user.Gender = ddlGender.SelectedItem.ToString();
        user.Birthday = ddlMonth.SelectedValue + "/" + ddlDate.SelectedValue + "/" + ddlYear.SelectedValue;
        user.Country = ddlCountry.SelectedItem.ToString();
        user.UserId = txtUserId2.Text;
        user.Password = txtPassword2.Text;
        user.SecurityQuestion = txtSecQue.Text;
        user.Answer = txtAnswer.Text;
        user.StatusName = "unavailable";
        user.StatusValue = 0;
        user.BDate = ddlDate.SelectedIndex;
        user.BMonth = ddlMonth.SelectedIndex;
        user.Byear = ddlMonth.SelectedIndex;
        user.GenderIndex = ddlGender.SelectedIndex;
        user.CountryIndex = ddlCountry.SelectedIndex;
        dc.UserDatas.InsertOnSubmit(user);
        dc.SubmitChanges();

    }
    protected void txtUserId_TextChanged(object sender, EventArgs e)
    {

    }
    protected void btnLogin_Click(object sender, EventArgs e)
    {
        LoginValidate();
    }
    protected void ddlYear_SelectedIndexChanged(object sender, EventArgs e)
    {
        ddlMonth.Enabled = true;
        ddMonth();
      
    }
    protected void ddlMonth_SelectedIndexChanged(object sender, EventArgs e)
    {
        ddlDate.Enabled = true;
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
        

    }
    protected void UpdatePanel2_Init(object sender, EventArgs e)
    {
        ddYear();
    }
    protected void chkbxAccept_CheckedChanged(object sender, EventArgs e)
    {

    }
    protected void UpdatePanel1_Init(object sender, EventArgs e)
    {
        
    }
    protected void txtUserId2_TextChanged(object sender, EventArgs e)
    {
        CheckAvailability();
    }

    private void CheckAvailability()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.ToList();
        Boolean flag = true;
        bool bIsAlphaNum = Regex.IsMatch(txtUserId2.Text, "^\\w*$");
        if (bIsAlphaNum == false)
        {

            lblAvailable.Text = "(a-z A-Z 0-9 _ . )";
            lblAvailable.ForeColor = System.Drawing.Color.Red;
            return;

        }
        foreach (var r in result)
        {
            if (r.UserId.ToLower() == txtUserId2.Text.ToLower())
            {
                flag = false;
                break;
            }
        }

        if (flag == true)
        {
            lblAvailable.Text = "Id Available!";
            lblAvailable.ForeColor = System.Drawing.Color.Green;
        }
        else if (flag == false)
        {
            lblAvailable.Text = "Id Unavailable! Please try another Id.";
            lblAvailable.ForeColor = System.Drawing.Color.Red;

        }



    }


    private void LoginValidate()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId.ToLower() == txtUserId.Text.ToLower() && x.Password == txtPassword.Text).SingleOrDefault();
        if (result != null)
        {
            FormsAuthentication.SetAuthCookie(txtUserId.Text, chkbxKeepMeLoggedIn.Checked);
            
            Response.Redirect("~/User/Welcome.aspx?UserId=" +txtUserId.Text+"&newuser=False&Remember="+chkbxKeepMeLoggedIn.Checked );
        }
        else
            lblLoginValidate.Text = "Invalid Login Details!";        
    }
}