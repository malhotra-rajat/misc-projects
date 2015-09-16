<%@ Page Language="C#" %>

<script runat="server">

</script>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Rajat Malhotra</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/default.css">
    <link href="css/bootstrap-theme.min.css" rel="stylesheet" />
    <link href="css/full-width-pics.css" rel="stylesheet" />
    <link rel="shortcut icon" href="images/favicon.ico">
    <script src="javascript/jquery-1.11.1.min.js"></script>
    <script src="javascript/bootstrap.min.js"></script>
    
    <script type="text/javascript" src="experiments/exp03/js/jquery.backstretch.min.js"></script>

    <style>
        .navbar .navbar-nav {
            display: inline-block;
            float: none;
            vertical-align: top;
        }

        .navbar .navbar-collapse {
            text-align: center;
        }
    </style>

</head>

<body class="container">
    <script>
        $.backstretch("images/images_wd/background6.jpg");
    </script>
      
       
         <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                </div>
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav" >
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif"  href="sitestatistics/" target="_blank">SiteStatistics</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif"  href="statistics/" target="_blank">Statistics</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif"  href="source/" target="_blank">Source</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif"  href="search/" target="_blank">Search</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif"  href="searchtree/" target="_blank">SearchTree</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif"  href="textview/" target="_blank">TextView</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif" href="filelist.aspx" target="_blank">FileList</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif" href="autofile.aspx" target="_blank">AutoFile</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif" href="images/autoimage.aspx" target="_blank">Images</a></li>
                        <li><a style="color:white; font-style: italic; font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif" href="blog/" target="_blank">Blog</a></li>
                    </ul>

                </div>
                <!-- /.navbar-collapse -->
            
            <!-- /.container -->
        </nav>
         
     
        
                <div class="myContent">
                  
                 <div class="imgDivStyle">
                    <img src="images/images_wd/Rajat_pic.jpg" class="imgStyle img-rounded" />
                </div>
                <hr />
               
                <p class="fontStyleBig">
                    Hello everyone! I am Rajat Malhotra, a computer science graduate student 
   at Northeastern University. This website is developed as part of the 
   CS5610 (Web Development) class taught by Prof. Jose Annunziato. I am highly 
   interested in web and mobile technologies and hope to learn
   a lot in this course. Please check out the links to my Web Development experiments and project below and feel free to connect with me on LinkedIn/Facebook.
                </p>

                <hr />
                     <p class="divStyleCenter">
                         <a class = "btn btn-primary" href="story/index.htm?../experiments/story.txt" target="_blank">Experiments</a>&nbsp;
                         <a class = "btn btn-primary" href="http://project-rmcs5610.rhcloud.com/movieapp#/" target="_blank">Project</a>&nbsp;
                         <a class = "btn btn-primary" href="story/index.htm?../project_documentation/story.txt" target="_blank">Project Documentation</a>
						 <a class = "btn btn-primary" href="https://github.com/rajat1988/webdev/tree/master/project" target="_blank">Project Source Code (GitHub)</a>
                     </p>
                     
                <hr />
                <div class="divStyleCenter">
              
                           
                    <footer>
                        <p style="text-align:center"><b>Copyright &copy; Rajat Malhotra. All rights reserved.</b></p>
                              <a href="https://www.facebook.com/malhotra.rajat1988">
                        <asp:Image ID="fb" class="margin10" runat="server" Height="50px" ImageUrl="~/images/images_wd/fb_icon.png" Width="50px"  ToolTip="Facebook Profile" />
                    </a>
                    <a href="http://www.linkedin.com/pub/rajat-malhotra/13/310/54/">
                        <asp:Image ID="li" class="margin10li" runat="server" Height="50px" ImageUrl="~/images/images_wd/linkedin_icon.png" Width="59px"  ToolTip="LinkedIn Profile"/>
                    </a>
              
                    </footer>  
                    
                      
               </div>
            </div>
        
  
  

</body>
</html>
