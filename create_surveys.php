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
        else {
        
        $_SESSION['start'] = time();
        $_SESSION['expire'] = $_SESSION['start'] + (60 * 60);
        
	$servername = "localhost";
	$username = "dp015507_bhups";
	$password = "**************";
	$dbname = "dp015507_surveys";

    // Create connection
    $conn = new mysqli($servername, $username, $password,$dbname);

    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    
    $getQuestionnaire = "SELECT questionaire_id,questionaire_title, num_of_question FROM questionaire WHERE expert_id = ".$_SESSION['user']." ORDER BY questionaire_id desc";
    $result = $conn->query($getQuestionnaire);
    
    
    
	$conn->close();
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Create Survey</title>

    <link rel="icon" href="surveys.ico">

    <meta name="viewport" content="width=device-width, initial-scale=1"><!--bootstrap adjust content based on device--> 
    <link rel="stylesheet" href="styles.css"><!--my css-->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"><!--bootstrap-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script><!--jquery-->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script><!--bootstrap-->
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script><!--angular js-->
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	<script type="text/javascript" src="assets/js/create_survey.js"></script><!-- my js -->
    <!-- FONT ICONS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
</head>
<body class="grey">
<div class="container"><?php echo "Welcome ". $_SESSION['name']."<a href='logout.php'>(Logout)</a>";?></div>
<form method="POST" action="create_surveys.php">
<!-- =================================
        Nav Bar
===================================-->
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
        <li><input id="submitSurvey" type="submit" value="Create Survey" name="send_survey"/></li>
      </ul>
    </div>
  </div>
</nav>

<section>
        <div class="container qPagetitle">
            <center><h3>Survey Creation</h3></center>
        </div>
</section>

<!-- =================================
        Survey Details
===================================-->
<section id="details">
    <div class="container">
            <div class="box style-1 description">
                <input class="form-control" id="title" type="text" placeholder="Survey Title" title="Title" name="title" required/>
                <textarea class="form-control inputBox" id="surveyDescription" placeholder="Survey description" rows="3" title="description" name="description" required></textarea>
            </div>
    </div>
</section>
<!-- =====================================
	extra details
========================================== -->
<section id="extra_details">
    <div class="container box">
        <div class="form-group row">
          <label for="launch_date" class="col-sm-2 col-form-label">Launch Date</label>
          <div class="col-sm-10">
            <input type="date" class="form-control" id="launch_date" name="launch_date" required/>
          </div>
        </div>
        <div class="form-group row">
          <label for="end_date" class="col-sm-2 col-form-label">End Date</label>
          <div class="col-sm-10">
            <input type="date" class="form-control" id="end_date" name="end_date" required/>
          </div>
        </div>
        <div class="form-group row">
          <label for="target" class="col-sm-2 col-form-label">Target</label>
          <div class="col-sm-10">
            <select id="target" name="target">
            	<option value="" selected>Please Select</option>
            	<option value="school">School</option>
            	<option value="department">Department</option>
		<option value="course">Course</option>
		<option value="gender">Gender</option>
		<option value="age">Age Group</option>	
            </select><span>
            <select id="sub_target" name="sub_target">
            	<option value=0 selected>Please Select</option>
	</select>
          </div>
        </div>
    </div>
</section>
<!-- =====================================
	questionnaire
========================================== -->
<section>
	<div class="box">
		<div id="questionnaireList">
			<form>
			<!-- questionnaire goes here -->
				<p>Please select one of the following questionnaire to send:</p>
				<?php
				
				if ($result->num_rows > 0) {
				    // output data of each row
				    while($row = $result->fetch_assoc()) {

				        echo "<div class='questionaire row'>
				        <div class='input-group'>
				        <span class='input-group-addon'><input type='radio' value ='".$row['questionaire_id']."' name='selection' aria-label='Radio button for following text input'></span>
				        <h3 class='input-group-addon'>".$row['questionaire_title']."</h3><h3 class='input-group-addon'>Total questions: "." ".$row['num_of_question']."</h3>
				        </div>
				        </div>";   
				    }
				} else {
				    echo "You don't have questionnaires at the moment. Your questionnaire will appear here when it is created.";
				}
				?>
			</form>
		</div>
	</div>
</section>

</form>
</body>
</html>
<?php
  if(isset($_POST['send_survey'])){
   $port = 3306;
   $userID = $_SESSION['user'];
   $dbh=new PDO('mysql:dbname='.$dbname.';host='.$servername.";port=".$port,$username, $password);

if(isset($_POST['title']) && isset($_POST['description']) && isset($_POST['launch_date']) && isset($_POST['end_date']) && isset($_POST['target']) && isset($_POST['sub_target']) && isset($_POST['selection'])){

     $sql=$dbh->prepare("INSERT INTO `survey` (`questionnaire_id`,`title`,`description`,`expert_id`,`start_date`, `end_date`, `target`,`sub_target`) VALUES (?, ?, ?, ?, ?, ?,?,?);");
     $sql->execute(array($_POST['selection'], $_POST['title'], $_POST['description'],$userID, $_POST['launch_date'], $_POST['end_date'],$_POST['target'],$_POST['sub_target']));
    }
   }
   
   }
   }
?>