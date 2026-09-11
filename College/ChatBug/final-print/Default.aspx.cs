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

public partial class _Default : System.Web.UI.Page 
{
    protected void Page_Load(object sender, EventArgs e)
    {
             EnablebtnCreate();  //to enable or disable the create my account button//
             string LoggedUser=User.Identity.Name;    // get the name of currently logged in user //
         
            
          if (User.Identity.IsAuthenticated==true)    // checks whether or not he is authenticated//
             {                                        // if yes, he is redirected to the home page//   
           
                 FormsAuthentication.SetAuthCookie(LoggedUser, true);
                 Response.Redirect("~/User/Welcome.aspx?UserId=" + LoggedUser.ToString() + "&newuser=False&Remember=True"); //new user is true if there is a new user (different label is displayed on the welcome page then)
             }
             
    }
    private void EnablebtnCreate() //to enable or disable the create my account button based on "accept terms"//
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
    private void ddDate(int days) //populating the drop down list of days after the selected item(index) of month changes
    {
        ddlDate.Items.Clear();
        for (int i = 1; i <= days; i++)
        {
            ListItem itm = new ListItem();
            itm.Text = i.ToString();
            itm.Value = i.ToString();
            ddlDate.Items.Add(itm);
        }
        ddlDate.Items.Insert(0, new ListItem("Date", "-1")); //Initial Value displayed in Drop down list
    }
    private void ddMonth() //to populate the month drop down list
    {
        ddlMonth.Items.Clear();
        for (int i = 1; i <= 12; i++)
        {
            ListItem itm1 = new ListItem();
            itm1.Text = i.ToString();
            itm1.Value = i.ToString();
            ddlMonth.Items.Add(itm1);
        }
        ddlMonth.Items.Insert(0, new ListItem("Month", "-1")); ////Initial Value displayed in Drop down list
    }
    private void ddYear() //populating the year drop down list
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
    
    protected void btnCreate_Click(object sender, EventArgs e) //create my account button
    {
        AddUser();
        Response.Write("About to redirect!");
        FormsAuthentication.SetAuthCookie(txtUserId2.Text, false); //false because we dont want to create permanant cookie after registration, only temporary cookie for that login
        Response.Redirect("~/User/Welcome.aspx?UserId=" + txtUserId2.Text+"&NewUser=True&Remember=false");
    }
    private void AddUser()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext(); //For each LINQ to SQL designer file added to our solution, a custom DataContext class will also be generated.  
                                                            //This DataContext class is the main conduit by which we'll query entities from the database as well as apply changes
                                                             // Creating object of this class for quering, updating, adding, deleting from the database
        UserData user = new UserData(); //creating object of table UserData
        user.FirstName = txtFirstName.Text; //insering data in columns
        user.LastName = txtLastName.Text;
        user.Gender = ddlGender.SelectedItem.ToString();
        user.Birthday = ddlMonth.SelectedValue + "/" + ddlDate.SelectedValue + "/" + ddlYear.SelectedValue;
        user.Country = ddlCountry.SelectedItem.ToString();
        user.UserId = txtUserId2.Text;
        user.Password = txtPassword2.Text;
        user.SecurityQuestion = txtSecQue.Text;
        user.Answer = txtAnswer.Text;
        user.StatusName = "unavailable";
        user.StatusValue = 1;
        user.BDate = ddlDate.SelectedIndex;
        user.BMonth = ddlMonth.SelectedIndex;
        user.Byear = ddlMonth.SelectedIndex;
        user.GenderIndex = ddlGender.SelectedIndex;
        user.CountryIndex = ddlCountry.SelectedIndex;
        dc.UserDatas.InsertOnSubmit(user);//inserting into
        dc.SubmitChanges();               //database




   
    }
    protected void txtUserId_TextChanged(object sender, EventArgs e)
    {

    }
    protected void btnLogin_Click(object sender, EventArgs e) //checking for login on button click
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
        if (ddlMonth.SelectedValue == "2") //month of february
        {
            if (Convert.ToInt32(ddlYear.SelectedValue) % 4 == 0) //checking for leap year
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
    protected void UpdatePanel2_Init(object sender, EventArgs e) //panel of date of birth..triggered on pageload
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
        var result = dc.UserDatas.ToList(); //var - implicitly typed variable....here it is a list
        Boolean flag=true;
        foreach (var r in result)
        {
            if (r.UserId.ToLower() == txtUserId2.Text.ToLower())
            {    flag=false;
                break;
                   
            }
        }

        if (flag == true)
        {
            lblAvailable.Text = "Id Available!";
            lblAvailable.ForeColor=System.Drawing.Color.Green;    
        }
        else
        {
            lblAvailable.Text="Id Unavailable! Please try another Id.";
            lblAvailable.ForeColor=System.Drawing.Color.Red;
        
        }
    }

    private void LoginValidate()
    {
        DataBaseDBDataContext dc = new DataBaseDBDataContext();
        var result = dc.UserDatas.Where(x => x.UserId.ToLower() == txtUserId.Text.ToLower() && x.Password == txtPassword.Text).SingleOrDefault(); //x is any identifier
        if (result != null) //id and password found
        {
            result.StatusValue = 1; //changes the status value to indicate the user is online
            dc.SubmitChanges();
            FormsAuthentication.SetAuthCookie(txtUserId.Text, chkbxKeepMeLoggedIn.Checked);
            
            Response.Redirect("~/User/Welcome.aspx?UserId=" +txtUserId.Text+"&newuser=False&Remember="+chkbxKeepMeLoggedIn.Checked ); //putting user id and the remember in the query string
        }
        else
            lblLoginValidate.Text = "Invalid Login Details!";        
    }
}