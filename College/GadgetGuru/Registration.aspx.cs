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
using System.Data.SqlClient;


public partial class _Default : System.Web.UI.Page 
{

    string connectionString = "Data Source=.\\SQLEXPRESS;AttachDbFilename=|DataDirectory|\\Database.mdf;Integrated Security=True;User Instance=True";

    protected void Page_Load(object sender, EventArgs e)
    {
        
            

    }
    protected void Button1_Click(object sender, EventArgs e)
    {
        SqlConnection conn = new SqlConnection(connectionString);

        string strcmd = "Database_AddUser_UserDetails";
        SqlCommand cmd = new SqlCommand(strcmd, conn);


        
        
        cmd.Parameters.AddWithValue("@FirstName", TextBox1.Text);
        cmd.Parameters.AddWithValue("@LastName", TextBox2.Text);
        cmd.Parameters.AddWithValue("@Email", TextBox3.Text);
        cmd.Parameters.AddWithValue("@UserName", TextBox4.Text);
        cmd.Parameters.AddWithValue("@Password", TextBox5.Text);
        cmd.Parameters.AddWithValue("@PasswordVerify", TextBox6.Text);
        



        cmd.CommandType = CommandType.StoredProcedure;


        conn.Open();
        

        int x = cmd.ExecuteNonQuery();

        if (x == 1)
        {
            
                     
            Response.Redirect("RegSuc.aspx");
        
          
        }
        
        conn.Close();

    }



    protected void TextBox4_TextChanged(object sender, EventArgs e)
    {
        SqlConnection cn = new SqlConnection(connectionString);
        SqlCommand cmd = new SqlCommand("Select count (UserName) from UserDetails where UserName = @UserName", cn);
        cmd.Parameters.AddWithValue("UserName", TextBox4.Text);
        cn.Open();
        int x = (int)cmd.ExecuteScalar();
        cn.Close();
        if (x >= 1)
        {
            lblInvalid.Visible = true;
        }
        else
        {
            lblInvalid.Visible = false;
        }
    }
}
