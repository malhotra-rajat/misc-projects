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
using System.Data.SqlClient;

public partial class Login : System.Web.UI.Page
{
    string connectionString = "Data Source=.\\SQLEXPRESS;AttachDbFilename=|DataDirectory|\\Database.mdf;Integrated Security=True;User Instance=True";
    
    protected void Page_Load(object sender, EventArgs e)
    {
        
    
       
    }
    protected void Register(object sender, EventArgs e)
    {
        Response.Redirect("Registration.aspx");
    }
    protected void btnLogin_Click(object sender, EventArgs e)
    {
      
        SqlConnection conn = new SqlConnection(connectionString);

        string strcmd = "select Count (*) from UserDetails where UserName = @UserName and Password = @Password";
        
        SqlCommand cmd = new SqlCommand(strcmd, conn);

        cmd.Parameters.AddWithValue("@UserName", txtUserName .Text );
        cmd.Parameters.AddWithValue("@Password", txtPassword .Text );
        conn.Open();
        int x = (int)cmd.ExecuteScalar();
        conn.Close();
        if (x == 1)
        {
            string user = x.ToString();
            FormsAuthentication.SetAuthCookie(user,false);
            Response.Redirect("Secured Pages/Home1.aspx");
        }
        else
        {
            lblCheck.Visible = true;
        }
        
    }
}
