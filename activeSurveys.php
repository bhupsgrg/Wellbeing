<?php
error_reporting(0);
session_start();
if($_SESSION['user']==''){
 header("Location:lhomehome.html");
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
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Your Surveys</title>

    <link rel="icon" href="surveys.ico">

    <meta name="viewport" content="width=device-width, initial-scale=1"><!--bootstrap adjust content based on device--> 
    <link rel="stylesheet" href="styles.css"><!--my css-->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"><!--bootstrap-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script><!--jquery-->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script><!--bootstrap-->
    <script type="text/javascript" src="assets/js/main.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css"><!-- FONT ICONS -->
        
</head>
<body class="grey">
<script>
	function setSession(obj){
		var id = obj.id;
		
		$(document).ready(function(){
		$.ajax({
			type:"POST",
			url: "setSessionSurveyID.php",
			data: {survey_id: id},
			success:function(data){
				window.open("analytic.php","_self");
				},
			error: function(xhr, status, error) {
			  	var err = eval("(" + xhr.responseText + ")");
			  	alert(err.Message);
			}			
			});	
	
		});
	    	
	}
	
	function setSessionForCSV(obj){
		var id = obj.id;
		
		$(document).ready(function(){
		$.ajax({
			type:"POST",
			url: "setSessionSurveyID.php",
			data: {survey_id: id},
			success:function(data){
				window.open("downloadCSV.html","_self");
				},
			error: function(xhr, status, error) {
			  	var err = eval("(" + xhr.responseText + ")");
			  	alert(err.Message);
			}			
			});	
	
		});
	    	
	}
</script>
<div class="container"><?php echo "Welcome ". $_SESSION['name']."<a href='logout.php'>(Logout)</a>";?></div>
<!-- =================================
        Nav Bar
===================================-->
<form method="post" id="surveyFormForm" action="uploadRealSurvey.php">
<nav class="navbar navbar-custom style-1">
  <div class="container">
    <div class="navbar-header">
     <a class="navbar-brand" href="index.php"><i class="fa fa-scissors" aria-hidden="true"></i>Hand Crafted Surveys</a>

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
  </div>
</nav>

<section>
        <div class="container qPagetitle">
            <center><h3>Your Surveys</h3></center>
            
            <div class="panel panel-success">
		  <div class="panel-heading">
		    <h3 class="panel-title">Active Surveys</h3>
		  </div>
		  <div class="panel-body">
		  	<?php
		    	
						    
		    $getSurvey0 = "SELECT * FROM survey where start_date <= CURDATE() AND end_date >= CURDATE()";
		    $result0 = $conn->query($getSurvey0);
		 
		    
		    	if ($result0->num_rows > 0) {
                    ?>
		    	<div class='table-responsive'>          
						  <table class='table'>
						    <thead>
						      <tr>
						        <th>#</th>
						        <th>Survey Title</th>
						        <th>Description</th>
						        <th>Launch Date</th>
						        <th>End Date</th>
						        <th>Target(Students)</th>
						        <th>Sub-Target(Students)</th>
						        <th>Questionnaire</th>
						        <th>Participator</th>
						        <th>Download</th>
						      </tr>
						    </thead><tbody>
		    	<?php
            		$count0 = 1;
				    // output data of each row
				    while($row0 = $result0->fetch_assoc()) {
				    $getTitle;
				    if($row0['target'] == "school"){
				    	$getTitle = "SELECT name from school where school_id = ".$row0['sub_target']."";
				    }else if($row0['target'] == "course"){
				    	$getTitle = "SELECT name from course where course_id = ".$row0['sub_target']."";
				    }else if($row0['target'] == "department"){
				    	$getTitle = "SELECT name from department where department_id = ".$row0['sub_target']."";
				    }
				    
				    $sTitle = $conn->query($getTitle);
				    $sTitles = mysqli_fetch_assoc($sTitle);
				    						     	
				    $getQuestionnaireTitle = "SELECT questionaire_title from questionaire where questionaire_id = ".$row0['questionnaire_id']."";
				    $questionnaireTitle = $conn->query($getQuestionnaireTitle);
				    $qTitleRow0 = mysqli_fetch_assoc($questionnaireTitle);
				    
				    $countAnswers = "SELECT * FROM survey_completed where survey_id = ".$row0['survey_Id']."";
				    $numAns= $conn->query($countAnswers);
				    $ansCount0=mysqli_num_rows($numAns);
				    
			?>
						  
						      <tr>
						        <td><?php echo $count0; ?></td>
						        <td><?php echo $row0['title']; ?></td>
						        <td><?php echo $row0['description'] ?></td>
						        <td><?php echo $row0['start_date'] ?></td>
						        <td><?php echo $row0['end_date'] ?></td>
						        <td><?php echo $row0['target'] ?></td>
						        <td><?php if($row0['target'] == "gender" || $row0['target'] == "age"){echo $row0['sub_target'];}
						        	 else {echo $sTitles['name'];} ?></td>
						        <td><?php echo $qTitleRow0['questionaire_title'] ?></td>
						        <td><a id=<?php echo '"'.$row0['survey_Id'].'"'; ?> onclick="setSession(this)"><?php echo $ansCount0 ?><span>
<i class="fa fa-line-chart" aria-hidden="true"></i></span></a></td>
							<td><button id=<?php echo '"'.$row0['survey_Id'].'"'; ?> onclick="setSessionForCSV(this)">Download CSV</td>
						      </tr>
						   
				<?php 
						 
						 $count0 = $count0 + 1; 							    
					    
				    }
				} else {
				    echo "<center>You haven't got active surveys at the moment.</center>";
				}
		    ?>
		    </tbody>
		</table>
		</div>
		</div>
        </div>
	</div>
	
	<div class="container">	
		<div class="panel panel-danger">
		<div class="panel-heading">
		    <h3 class="panel-title">Ended Surveys</h3>
		  </div>
		  <div class="panel-body">
		  	<?php
		  	
		  	$getSurvey = "SELECT * FROM survey where end_date < CURDATE()";
		  	$result = $conn->query($getSurvey);
		  	
		    	if ($result->num_rows > 0) {
                    ?>
		      <div class='table-responsive'>          
						  <table class='table'>
						    <thead>
						      <tr>
						        <th>#</th>
						        <th>Survey Title</th>
						        <th>Description</th>
						        <th>Launch Date</th>
						        <th>End Date</th>
						        <th>Target(Students)</th>
						        <th>Sub-Target(Students)</th>
						        <th>Questionnaire</th>
						        <th>Participator</th>
						        <th>Download</th>
						      </tr>
						    </thead>
						    <tbody>
		    	<?php
            		$count = 1;
				    // output data of each row1
				    while($row = $result->fetch_assoc()) {
				    
				    $getTitle;
				    if($row['target'] == "school"){
				    	$getTitle = "SELECT name from school where school_id = ".$row['sub_target']."";
				    }else if($row['target'] == "course"){
				    	$getTitle = "SELECT name from course where course_id = ".$row['sub_target']."";
				    }else if($row['target'] == "department"){
				    	$getTitle = "SELECT name from department where department_id = ".$row['sub_target']."";
				    }
				    
				    $sTitle = $conn->query($getTitle);
				    $sTitles = mysqli_fetch_assoc($sTitle);
					    					     	
				    $getQuestionnaireTitle = "SELECT questionaire_title from questionaire where questionaire_id = ".$row['questionnaire_id']."";
				    $questionnaireTitle = $conn->query($getQuestionnaireTitle);
				    $qTitleRow = mysqli_fetch_assoc($questionnaireTitle);
				    
				    $countAnswers = "SELECT * FROM survey_completed where survey_id = ".$row['survey_Id']."";
				    $numAns= $conn->query($countAnswers);
				    $ansCount=mysqli_num_rows($numAns);

				     ?>   
						      <tr>
						        <td><?php echo $count ?></td>
						        <td><?php echo $row['title'] ?></td>
						        <td><?php echo $row['description'] ?></td>
						        <td><?php echo $row['start_date'] ?></td>
						        <td><?php echo $row['end_date'] ?></td>
						        <td><?php echo $row['target'] ?></td>
						        <td><?php if($row['target'] == "gender" || $row['target'] == "age"){echo $row['sub_target'];}
						        	 else {echo $sTitles['name'];} ?></td>
						        <td><?php echo $qTitleRow['questionaire_title']?></td>
						        <td><a id=<?php echo '"'.$row['survey_Id'].'"'; ?> onclick="setSession(this)"><?php echo $ansCount ?><span>
<i class="fa fa-line-chart" aria-hidden="true"></i></span></a></td>
							<td><button id=<?php echo '"'.$row0['survey_Id'].'"'; ?> onclick="setSessionForCSV(this)">Download CSV</td>
						      </tr>
						    
						 						    
				<?php		
				 $count = $count + 1; 	    
				    }
				} else {
				    echo "<center>You haven't got ended surveys at the moment.</center>";
				}
		    ?>
		    </tbody>
		</table>
		</div>
		</div>
	  </div>
	</div>
	<div class="container">		
	   <div class="panel panel-info">
		<div class="panel-heading">
		    <h3 class="panel-title">Upcoming Surveys</h3>
		  </div>
		  <div class="panel-body">
		    <?php
		    				    
		    $getSurvey1 = "SELECT * FROM survey WHERE start_date > CURDATE() AND end_date > CURDATE()";
		    $result1 = $conn->query($getSurvey1);
		    
		    	if ($result1->num_rows > 0) {
		    	?>
		    	<div class='table-responsive'>          
						  <table class='table'>
						    <thead>
						      <tr>
						        <th>#</th>
						        <th>Survey Title</th>
						        <th>Description</th>
						        <th>Launch Date</th>
						        <th>End Date</th>
						        <th>Target(Students)</th>
						        <th>Sub-Target(Students)</th>
						        <th>Questionnaire</th>
						        <th>Participator</th>
						        <th>Download</th>
						      </tr>
						    </thead><tbody>
                    <?php
                    
            		$count1 = 1;
				    // output data of each row
				    while($row1 = $result1->fetch_assoc()) {
				    
				    $getTitle2;
				    if($row1['target'] == "school"){
				    	$getTitle2 = "SELECT name from school where school_id = ".$row1['sub_target']."";
				    }else if($row1['target'] == "course"){
				    	$getTitle2 = "SELECT name from course where course_id = ".$row1['sub_target']."";
				    }else if($row1['target'] == "department"){
				    	$getTitle2 = "SELECT name from department where department_id = ".$row1['sub_target']."";
				    }
				    
				    $sTitle2 = $conn->query($getTitle2);
				    $sTitles = mysqli_fetch_assoc($sTitle2);
  					     	
				    $getQuestionnaireTitle = "SELECT questionaire_title from questionaire where questionaire_id = ".$row1['questionnaire_id']."";
				    $questionnaireTitle = $conn->query($getQuestionnaireTitle);
				    $qTitleRow1 = mysqli_fetch_assoc($questionnaireTitle);
				    
				    $countAnswers = "SELECT * FROM survey_completed where survey_id = ".$row1['survey_Id']."";
				    $numAns= $conn->query($countAnswers);
				    $ansCount1=mysqli_num_rows($numAns);

				        
					?>
						
						      <tr>
						        <td><?php echo $count1 ?></td>
						        <td><?php echo $row1['title'] ?></td>
						        <td><?php echo $row1['description'] ?></td>
						        <td><?php echo $row1['start_date'] ?></td>
						        <td><?php echo $row1['end_date'] ?></td>
						        <td><?php echo $row1['target'] ?></td>
						        <td><?php if($row1['target'] == "gender" || $row1['target'] == "age"){echo $row1['sub_target'];}
						        	 else {echo $sTitles['name'];} ?></td>
						        <td><?php echo $qTitleRow1['questionaire_title'] ?></td>
						        <td><a id=<?php echo '"'.$row1['survey_Id'].'"'; ?> onclick="setSession(this)"><?php echo $ansCount1 ?><span>
<i class="fa fa-line-chart" aria-hidden="true"></i></span></a></td>
							<td><button id=<?php echo '"'.$row0['survey_Id'].'"'; ?> onclick="setSessionForCSV(this)">Download CSV</td>
						      </tr>
						    
						  
						  <?php
						  
						  $count1 = $count1 + 1; 							    
					    
				    }
				} else {
				    echo "<center>You haven't got surveys to be launched at the moment.</center>";
				}
		    ?>
		    			</tbody>
				</table>
			</div>
		  </div>
	  </div>
		
        </div>
</section>
<?php $conn->close(); ?>
</form>
</body>
</html>
<?php
	}
}
?>