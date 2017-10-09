    var questionCounter = 0; //counts num of question
    var questionIdCounter = 0; //keeps track of question id.
    var tmpId; //temporarly storees question ID
    var oTmpId;
    var removeQuestionId;
    var objArray = []; //Array of questions
    var optionObjS = []; //array for storing single choice form data
    var optionObjM = []; //array for storing multiple choice form data
    var dataString = []; //array for questionText
    var userId;
    
//object of question
function NewQuestion(questionId, questionType){
    
    this.questionId = questionId;
    this.questionTypeObj = questionType;
    this.options = 0;
    this.required = false;
    this.counter = 0;
}

function NewDataString(questionText){
	this.questionText = questionText;
}


//question type selection
function getQuestionType(){
    
    var select = document.getElementById('questionTypeSelection');
    var option = select.options[select.selectedIndex].value;
    return option;
    
}

//create new question object
function createNewQuestionObj(aQId, aQType){
    
    var q = new NewQuestion(aQId, aQType);
    
    objArray.push(q);
    
}

//reduce question counter
function removeQuestion(id){
    questionCounter = questionCounter - 1;
    questionIdCounter = questionIdCounter - 1;
    removeQuestionId = id;
}

//get id
function getID(oObject) 
{
    var id = oObject.id;
    tmpId = id;
}

//increment options of a question
function incremntOption(i){
    objArray[i].options++;
}

//decreases the option when removed
function decreaseOption(i){
    objArray[i].options--;
}

//gets the parent 3rd parent id
function getParentID(sender){
    var divID = sender.parentNode.parentNode.parentNode.parentNode;
    oTmpId = divID.id;
}

//gets the parent 3rd parent id
function getParentID1(sender){
    var divID = sender.parentNode;
    oTmpId = divID.id;
}

//add object questionText to array
function addQuestionText(text){
	var t = new NewDataString(text);
	dataString.push(t);
}

//add toggle button value
function addToggleButtonState(i, value){
	objArray[i].required = value;
}

//increase objects counter
function incremntCounter(i){
    objArray[i].counter++;
}

//decrease object counter
function decreaseCounter(i){
	objArray[i].counter--;
}

//delete option
$(function () {    
    $("body").on("click", ".remove", function () {
        $(this).closest("div").remove();

        var questionId = oTmpId;
        
        for(i = 0; i<objArray.length; i++){
            if(questionId == objArray[i].questionId){
                if(objArray[i].options>1){
                	decreaseOption(i);
                	decreaseCounter(i);
                }
            }
        }     
    });
});

//delete question
$(function () {
    $("body").on("click", ".removeQuestion", function () {
        $(this).closest("div").remove();
        removeQuestion(oTmpId);
       	var a = removeQuestionId - 1;
	objArray.splice(a,1);
    }); 
});


function passSession(session){
	userId = session;
}

//add question
$(document).ready(function(){
	$('#btnAddQ').click(function(){
		questionCounter++;
		questionIdCounter++;
		var tmp = getQuestionType();
		var questionTypeText; //store type of question
		var qId = questionIdCounter; //unique question id
        
		var questionNum = '<p>Question ' + questionCounter + '</p>';
        
		var question = '<textarea class="form-control style-1" id="question' + qId + '" placeholder="Your question" title="question" name="question"></textarea>';
        
		var singleChoice = '<div class="form-group"><input id="'+ qId + '" type="button" value="Add Option" class="addOption" onclick="getID(this);"/></div>';
        
		var multipleChoice = '<div class="form-group"><input id="'+ qId +'" type="button" value="Add Option" class="addOption" onclick="getID(this);"/></div>';
        
        var yesNoChoice = '<div class="style-1"><input type="radio" id="yes"/><label for="yes">Yes</label><input type="radio" id="no"/><label for="no">No</label></div>';
        
        var openAnswer = '<textarea class="form-control style-1" id="openAnswer"' + qId + '" title="openAnswer" rows="2" name="openAnswer"></textarea>';
        
        var howSatisfied = '<div class="style-1 radioGroup"><input type="radio" id="7"/><label for="#7">Definitely agree</label><br><input type="radio" id="6"/><label for="#6">Mostly agree</label><br><input type="radio" id="5"/><label for="#5">Neither agree nor disagree</label><br><input type="radio" id="4"/><label for="#4">Mostly disagree</label><br><input type="radio" id="3"/><label for="#3">Definitely disagree</label><br><input type="radio" id="2"/><label for="#2">Not applicable</label></div>';
        
        var linear = '<div class="style-1"><input type="radio" id="#1"><label for="#1">1</label><input type="radio" id="#2"><label for="#2">2</label><input type="radio" id="#3"><label for="#">3</label><input type="radio" id="#4"><label for="#4">4</label><input type="radio" id="#5"><label for="#5">5</label></div>';
        
        var requiredToggle = '<label id="requiredLabel" for="requiredToggle">Required</label><label class="switch"><input id="requiredToggle"'+ qId + '" type="checkbox" checked="checked" ><div class="slider round"></div></label>';
        
		
		//check type of option
		if(tmp == "single"){ questionTypeText = singleChoice;}
			else if(tmp == "multiple"){questionTypeText = multipleChoice;}
        		else if(tmp == "yesNo"){questionTypeText = yesNoChoice;}
        		else if(tmp == "openAnswer"){questionTypeText = openAnswer;}
        		else if(tmp == "satification"){questionTypeText = howSatisfied;}
        		else if(tmp == "scale"){questionTypeText = linear;}
		
		var option = '<div id="multiple"><div id="multipleChoiceContainer' + qId + '" class="style-1 row multipleChoiceContainer"></div></div>';
        
		var deleteButton = '<a class="btn btn-default removeQuestion" href="#" aria-label="Delete" onclick="getParentID1(this);"><i class="fa fa-trash-o" aria-hidden="true"></i></a>';
        
		$('#surveyForm').append('<div id="'+ qId + '"  class="question box style-1">' + questionNum + question  + questionTypeText +  option  + deleteButton + '</div>');
        
		$('html,body').animate({
			scrollTop: $(window).scrollTop() + 200
		});
        
        createNewQuestionObj(qId, tmp);
	});
});

//add option
var j=0;
$(function(){
	$('body').on("click", ".addOption", function(){
		var questionId = tmpId;
        	var questionType;
        	var tmp;
        
		var radio = '<input type="radio" disabled>';
		var checkbox = '<input type="checkbox" disabled>';
		//var textbox = '<input type="text" id="text" placeholder="Option"/>';
		var addOptionButton = '<input type="button" value="Remove" class="remove" onclick="getParentID(this);"/>';
        
        for(i = (questionId - 1); i < objArray.length; i++){
            if(objArray[i].questionId == questionId){
                tmp = objArray[i].questionTypeObj;
                
                if(tmp == "single"){ questionType = radio;}
		          else if(tmp == "multiple"){questionType = checkbox;}
		          
                j = objArray[i].counter;
                
                $('#multipleChoiceContainer' + objArray[i].questionId).append('<div class="style-1">' + questionType + 
'<input type="text" id="'+ objArray[i].questionId + 'texter' + j + '" placeholder="Option"/>' + addOptionButton + '</div>');
	incremntOption(i);
	incremntCounter(i); 
			}     
        	} 
	});
});

$(document).ready(function(){
	$("#surveyFormForm").on('submit', function(e){
		e.preventDefault();
		var qTitle = $('#title').val();
		var a = objArray.length;
		var singleArr = [];
		var mutlipleArr = [];
		if(a > 0){
		
			for(i = 0; i<objArray.length; i++){
	    			var questionText = $('#question' + (i+1)).val();
	    			//var requireState;
	    			singleArr = [];
	    			mutlipleArr = [];
	    			
	    			/*if($('#requiredToggle' + (i+1)).is(":checked")) {
				    requireState = true;
				    console.log("true");
				} else{
				    console.log("false");
				}*/
				
	   			for(j = 0; j<objArray[i].options; j++){
	   				if(objArray[i].questionTypeObj == "single"){
	   					var optionObjobjs = $('#' + objArray[i].questionId + 'texter' + j).val();
	   					singleArr.push(optionObjobjs);
	   				}else if(objArray[i].questionTypeObj == "multiple"){
	   					var optionObjobjm = $('#' + objArray[i].questionId + 'texter' + j).val();
	   					mutlipleArr.push(optionObjobjm);
	   				}
	   			}
	   			
	   			if(objArray[i].questionTypeObj == "single"){
	   				optionObjS.push(singleArr);
	   			}else if(objArray[i].questionTypeObj == "multiple"){
	   				optionObjM.push(mutlipleArr);
	   			}
	   			
	   			addQuestionText(questionText);
	   			
	    		}
	    		
	    		
    			var jsonArray = JSON.stringify(objArray);
    			var jsonOptionS = JSON.stringify(optionObjS);
    			var jsonOptionM = JSON.stringify(optionObjM);
    			var jsonQuestion = JSON.stringify(dataString);
    			
    			console.log(jsonOptionS);
    			
			$.ajax({
				type:"POST",
				url: "uploadSurvey.php",
				data: {jsonData: jsonArray, jsonData2: jsonOptionS, jsonData3: jsonOptionM, q: jsonQuestion, title: qTitle, uId: userId},
				success:function(data){
					console.log(data);
					window.open("create_surveys.php","_self");
				}			
			});
		}
	});
});
