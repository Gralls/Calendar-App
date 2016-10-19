$(document).ready(function(){
	$(".sidebarToggleOn").click(function() {
		$("#sidebarWrapper").css("width", "250px");
		$("#mainContainer").css("margin-left", "250px");
		$(".sidebarToggleOn").css("display", "none");
	});
	$(".sidebarToggleOff").click(function() {
		$("#sidebarWrapper").css("width", "0");
		$("#mainContainer").css("margin-left", "0");
		$(".sidebarToggleOn").css("display", "block");
	});
});