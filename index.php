<?php
session_start();
if($_SESSION['user']==''){
 header("Location:homehome.html");
}else{
	$now = time(); // Checking the time now when home page starts.

        if ($now > $_SESSION['expire']) {
            session_destroy();
            header("Location:loginform.php");
        }
        else { $_SESSION['start'] = time();
		$_SESSION['expire'] = $_SESSION['start'] + (60 * 60);
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create Questionnaire</title>

    <link rel="icon" href="surveys.ico">

    <meta name="viewport" content="width=device-width, initial-scale=1"><!--bootstrap adjust content based on device--> 
    <link rel="stylesheet" href="styles.css"><!--my css-->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"><!--bootstrap-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script><!--jquery-->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script><!--bootstrap-->
    <script type="text/javascript" src="assets/js/main.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"><!-- FONT ICONS -->
    <script type="text/javascript">
passSession("<?php echo $_SESSION['user']; ?>");
</script>
        
</head>
<body>
<div class="container"><?php echo "Welcome ". $_SESSION['name']."<a href='logout.php'>(Logout)</a>";?></div>
<!-- =================================
        Nav Bar
===================================-->
<form method="post" id="surveyFormForm" action="uploadSurvey.php">
<nav class="navbar navbar-custom style-1">
  <div class="container">
    <div class="navbar-header">
     <a class="navbar-brand" href="index.php"><i class="fa fa-scissors" aria-hidden="true"></i>Hand Crafted Surveys</a>

      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
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
        <li><input id="submitSurvey" class="submit" type="submit" value="Upload" name="submit"/></li>
      </ul>
    </div>
  </div>
</nav>

<section>
        <div class="container qPagetitle">
            <center><h3>Questionnaire Creation</h3></center>
        </div>
</section>

<!-- =================================
        Survey Details
===================================-->
<section id="details">
    <div class="container">
            <div class="box style-1 description">
                <input class="form-control" id="title" type="text" placeholder="Questionnaire Title" title="Title" name="questionaireTitle" required/>
            </div>
    </div>
</section>

<!-- =================================
            Questions
===================================-->
<section>
    <div class="container">
        <div class="row">
            <div id="surveyForm">
                <!-- Questions goes here -->
            </div>
        </div>
    </div>
</section>

<!-- =================================
        Tool Box
===================================-->

<section>
    <div class="container toolBox">
        <div class="row">
            <form>
                <div class="box style-1">
                    <select id="questionTypeSelection">
                        <option value="single">Single Choice</option>
                        <option value="multiple">Multiple choice</option>
                        <option value="yesNo">Yes/No</option>
                        <option value="openAnswer">Open answer</option>
                        <option value="satification">Satisfaction</option>
                        <option value="scale">Linear Scale</option>
                    </select><span>
                    <input id="btnAddQ" type="button" value="Add Question"/></span>
                </div>
            </form>
        </div>
    </div>
</section>
</form>
</body>
</html>
<?php

	}
}
?>