<?php
session_start();
if($_SESSION['user']==''){
 header("Location:homehome.html");
}else{
	if(isset($_SESSION['survey_id']) && isset($_SESSION['survey_title'])){
		        	
?>
<!doctype html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Analytics</title>
    <link rel="icon" href="surveys.ico">
    
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script><!--Google charts-->
    <meta name="viewport" content="width=device-width, initial-scale=1"><!--bootstrap adjust content based on device--> 
    <link rel="stylesheet" href="styles.css"><!--my css-->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"><!--bootstrap-->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script><!--bootstrap-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"><!-- FONT ICONS -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="assets/js/activeSurvey.js"></script>
    
  </head>
  <body class="grey">
  <div class="container"><?php echo "Welcome ". $_SESSION['name']."<a href='logout.php'>(Logout)</a>";?>

  <nav class="navbar navbar-custom style-1">
  
    <div class="navbar-header">
     <a class="navbar-brand" href="index.php"><i class="fa fa-scissors" aria-hidden="true"></i>Hand Crafted Surveys | Data Analytics</a>

      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="index.php">Create Questionnaire</a></li>
        <li><a href="create_surveys.php">Create Surveys</a></li>
        <li><a href="activeSurveys.php">Your Surveys</a></li>
      </ul>
    </div>
  
</nav>
      </div>
<section>
        <div class="container qPagetitle">
            <center><h3><?php echo $_SESSION['survey_title']; ?></h3></center>
	<center>
        	<div class="chart" id="chart_div"></div>
	</center>
  </div>
</section>
  </body><!--container-->
</html>
<?php

}else{
		
	}
    
    }
?>