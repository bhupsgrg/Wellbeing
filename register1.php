<?
session_start();
if(isset($_SESSION['user']) && $_SESSION['user']!=''){
 header("Location:index.php");
}
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Surveyor Sign Up</title>

    <link rel="icon" href="surveys.ico">

    <meta name="viewport" content="width=device-width, initial-scale=1"><!--bootstrap adjust content based on device--> 
    <link rel="stylesheet" href="styles.css"><!--my css-->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"><!--bootstrap-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script><!--jquery-->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script><!--bootstrap-->
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script><!--angular js-->
    <script type="text/javascript" src="assets/js/student_signUp.js"></script>

    <!-- FONT ICONS -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
</head>
<body class="grey">
     <!-- =================================
            Nav bar
    ===================================-->
    <nav class="navbar navbar-custom style-1">
  <div class="container">
    <div class="navbar-header">
     <a class="navbar-brand" href="index.php"><i class="fa fa-scissors" aria-hidden="true"></i>Hand Crafted Surveys</a>
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
      </button>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="student_signUp1.php">Student Sign Up</a></li>
        <li><a href="loginform.php">Surveyor Log In</a></li>
      </ul>
    </div>
  </div>
</nav>
    <!-- =================================
            Details
    ===================================-->
    <section class="title">
        <div class=" container style-1">
            <center><h3>Surveyor Sign Up</h3></center>
        </div>
    </section>
     <!-- =================================
            Log in form
    ===================================-->
    <div class="container box">
      <form method="post" action="register1.php">
      	<div class="form-group row">
          <label for="firstname" class="col-sm-2 col-form-label">First Name</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="firstname" name="firstname" required>
          </div>
        </div>
        <div class="form-group row">
          <label for="lastname" class="col-sm-2 col-form-label">Last Name</label>
          <div class="col-sm-10">
            <input type="text" class="form-control" id="lastname" name="lastname" required>
          </div>
        </div>
        <div class="form-group row">
          <label for="inputEmail3" class="col-sm-2 col-form-label">Email</label>
          <div class="col-sm-10">
            <input type="email" class="form-control" id="inputEmail3" name="user" required>
          </div>
        </div>
        <div class="form-group row">
          <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="inputPassword"  name="pass" required>
          </div>
        </div>
        <div class="form-group row">
          <label for="re-inputPassword" class="col-sm-2 col-form-label">Repeat Password</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="re-inputPassword" name="re-pass" onChange="checkPasswordMatch();" required>
          </div>
        </div>
        <div class="" id="divCheckPasswordMatch"></div>
        <div class="form-group row">
          <div class="offset-sm-2 col-sm-10">
              <button type="submit" class="btn btn-primary" name="submit">Sign Up</button>
            </div>
        </div>
      </form>
      <div class="info"></div>
    </div>
</body>
</html>
<?
  if(isset($_POST['submit'])){
   $musername = "dp015507_bhups";
   $mpassword = "u*******";
   $hostname = "localhost";
   $db = "dp015507_surveys";
   $port = 3306;
   $dbh=new PDO('mysql:dbname='.$db.';host='.$hostname.";port=".$port,$musername, $mpassword);
   if(isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['user']) && isset($_POST['pass'])){
    $password=$_POST['pass'];
    $sql=$dbh->prepare("SELECT COUNT(*) FROM `users` WHERE `username`=?");
    $sql->execute(array($_POST['user']));
    if($sql->fetchColumn()!=0){
     die("User Exists");
    }else{
     function rand_string($length) {
      $str="";
      $chars = "abcdefghijklmanopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      $size = strlen($chars);
      for($i = 0;$i < $length;$i++) {
       $str .= $chars[rand(0,$size-1)];
      }
      return $str; 
     }
     $p_salt = rand_string(20); 
     $site_salt="s"; 
     $salted_hash = hash('sha256', $password.$site_salt.$p_salt);
     $sql1=$dbh->prepare("INSERT INTO `users` (`id`,`firstName`,`lastName`, `username`, `password`, `psalt`) VALUES (NULL, ?, ?, ?, ?, ?);");
     $sql1->execute(array($_POST['firstname'], $_POST['lastname'], $_POST['user'], $salted_hash, $p_salt));
     if (!$sql1) {
		    echo "\nPDO::errorInfo():\n";
    		    print_r($dbh->errorInfo());
		} else {
		
		$user_To      = $_POST['user'];
		$user_subject = 'Hand Crafted Surveys - Thank you for registering';
		$user_message = '<html>
		<head>
		  <title>Hand Crafted Survey</title>
		</head>
		<body>
		  <p>Hi '.$_POST['firstname'].',</p>
		  <p>Thank you for registering in Hand Crafted Survey. Your account needs to be verified by the University of Reading before you can log in to the website. Your account will be approved within 24 hours and you will be notified when your account has been approved.</p>
		  <p>Bhupal Gurung<br>
		  Website Administrator</p>
		</body>
		</html>'
		;
		
		$headers[] = 'MIME-Version: 1.0';
		$headers[] = 'Content-type: text/html; charset=UTF-8';
		
		// Additional headers
		$headers[] = 'From: Hand Crafted Survey <noreply@handcraftedsurvey.co.uk>';
		
		// Mail it
		mail($user_To, $user_subject, $user_message, implode("\r\n", $headers));    
				
		
		$to      = 'dp015507@reading.ac.uk';
		$subject = 'Approval needed - New surveyor registered';
		$message = 'Following surveyor needs to be approved'. "\r\n". 'Full Name: '.$_POST["firstname"].' '.$_POST["lastname"];
		$message = wordwrap($message, 70, "\r\n");
		
		
		mail($to, $subject, $message);
		
		
		
     echo '<div class="container box alert alert-success" role="alert"><center><p>Successfully Registered. Your account needs to be verified by university in order to login. Please <a href="loginform.php">Login</a></p></center></div>';
     }
    }
   }
  }
?>