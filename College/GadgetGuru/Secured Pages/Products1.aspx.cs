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

public partial class Products1 : System.Web.UI.Page
{
    string connectionString = "Data Source=.\\SQLEXPRESS;AttachDbFilename=|DataDirectory|\\Database.mdf;Integrated Security=True;User Instance=True";
    
    protected void Page_Load(object sender, EventArgs e)
    {
        
        ShowAllProducts();
    }
    private void ShowAllProducts()
    {
        
        SqlConnection conn = new SqlConnection(connectionString);
       
        SqlDataAdapter da = new SqlDataAdapter("Select * from Product", conn);
        DataSet ds = new DataSet();
        
        da.Fill(ds);

        gvProduct.DataSource = ds;
        gvProduct.DataBind();
        
    }         
}
