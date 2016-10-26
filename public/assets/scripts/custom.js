$(document).ready(function(){
	$(".sidebarToggle").click(function() {
		if($("#sidebarWrapper").css("width") > '0px'){
			$("#sidebarWrapper").css("width", "0");
			$("#mainContainer").css("margin-left", "0");
		}
		else{
			$("#sidebarWrapper").css("width", "250px");
			$("#mainContainer").css("margin-left", "250px");
		}
	});
});