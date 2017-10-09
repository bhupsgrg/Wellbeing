<?php
session_start();
if(isset($_SESSION['user']) && $_SESSION['user']!=''){header("Location:index.php");}
$dbh=new PDO('mysql:dbname=dp015507_surveys;host=localhost', 'dp015507_bhups', '********');
$email=$_POST['mail'];
$password=$_POST['pass'];
$error;
if(isset($_POST) && $email!='' && $password!=''){
 $sql=$dbh->prepare("SELECT id,firstName,password,psalt, verified FROM users WHERE username=?");
 $sql->execute(array($email));
 while($r=$sql->fetch()){
  $p=$r['password'];
  $p_salt=$r['psalt'];
  $id=$r['id'];
  $fname = $r['firstName'];
  $verified = $r['verified'];
 }
 
 $site_salt="******";
 $salted_hash = hash('sha256',$password.$site_salt.$p_salt);
 if($verified == true){
 if($p==$salted_hash){
  $_SESSION['user']=$id;
  $_SESSION['name']=$fname;
  $_SESSION['start'] = time();
 $_SESSION['expire'] = $_SESSION['start'] + (60 * 60);
  header("Location:index.php");
 }else{
  $error =  "<div class='container box alert alert-danger' role='alert'><center><h4>Username or Password is Incorrect.</h4></center></div>";
 }
} else {
  $error = "<div class='container box alert alert-warning' role='alert'><center><h4>Your account is not verified.</h4></center></div>";
}
}
?>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Log In</title>

    <link rel="icon" href="surveys.ico">

    <meta name="viewport" content="width=device-width, initial-scale=1"><!--bootstrap adjust content based on device--> 
    <link rel="stylesheet" href="styles.css"><!--my css-->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"><!--bootstrap-->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script><!--jquery-->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script><!--bootstrap-->

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
        <span class="icon-bar"></span>
      </button>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav navbar-right">
      	<li><a href="student_signUp1.php">Student Sign Up</a></li>
        <li><a href="register1.php">Surveyor Sign Up</a></li>
      </ul>
    </div>
  </div>
</nav>
    <!-- =================================
            Details
    ===================================-->
    <section class="title">
        <div class="container style-1">
            <center><h3>Surveyor Log In</h3></center>
        </div>
    </section>
     <!-- =================================
            Log in form
    ===================================-->
    <div class="container box">
      <form method="post" action="loginform.php">
        <div class="form-group row">
          <label for="inputEmail3" class="col-sm-2 col-form-label">Email</label>
          <div class="col-sm-10">
            <input type="email" class="form-control" id="inputEmail3" placeholder="Email" name="mail" required>
          </div>
        </div>
        <div class="form-group row">
          <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
          <div class="col-sm-10">
            <input type="password" class="form-control" id="inputPassword" placeholder="Password" name="pass" required>
          </div>
        </div>
        <div class="form-group row">
          <div class="offset-sm-2 col-sm-10">
              <button type="submit" class="btn btn-primary" name="Submit">Sign in</button>
            </div>
        </div>
      </form>
    </div>
    <div class="container">
    	<center><p><?php echo "Don't" ?> have an account, <a href="register1.php">Sign up here</a></p></center>
    </div>
    <div class="error"><?php echo $error; ?></div>
</body>
</html>

