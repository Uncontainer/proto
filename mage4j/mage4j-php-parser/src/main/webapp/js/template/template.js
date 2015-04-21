$(function() {
	$("#save_button").bind("click", function(ev) {
		$("#view_layout").val($("div#view_layout_area").html());
		$("#edit_layout").val($("div#edit_layout_area").html());
//		console.log($("#view_layout").val());
//		console.log($("#edit_layout").val());
		jQuery("#save_form").submit();
	});
});

// layout tab
$(function() {
	$("#layout_tabs").tabs();
});
