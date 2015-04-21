$(function() {
	$("#save_button").bind("click", function(ev) {
		if(($("#frame_id").val() || "") === "") {
			$("#frame_id").val("0");
		}
		jQuery("#save_form").submit();
	});
});

// frame 편집
$(function() {
	$("#field_type_selector").change(function(ev) {
		if(ev.target.value === "FRAME") {
			$("#frame_row").show();
		} else {
			$("#frame_row").hide();
		}
	});
	
	$("#frame_input").autocomplete({
		source : "/frame/_search",
		select : function(event, ui) {
			var item = ui.item;
			$("#frame_id").attr("value", item.value);
			$("#frame_name").html(item.label);
		},
		close : function(event) {
			event.target.value = '';
		}
	});
});