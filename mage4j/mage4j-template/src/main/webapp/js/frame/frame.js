$(function() {
	$("#save_button").bind("click", function(ev) {
		jQuery("#save_form").submit();
	});
});

// 부모 frame 편집
$(function() {
	$("#parent_frame_input").autocomplete({
		source : "/frame/_search",
		select : function(event, ui) {
			var item = ui.item;
			$("#parent_frames").append(
				"<tr id='parent_frame_" + item.value + "'>" +
				"	<input type='hidden' name='parentFrameIds' value='" + item.value +"'/>"+
				"	<td>" + item.label + "</td>"+
				"	<td>" + item.description + "</td>"+
				"	<td><button class='_remove_parent_frame' data-id='" + item.value + "'>X</button></td>"+
				"</tr>");
		},
		close : function(event) {
			event.target.value = '';
		}
	});
	
	$("#parent_frames").on("click", "button._remove_parent_frame", function(ev) {
		$("tr#parent_frame_" + $(ev.target).attr('data-id')).remove();
	});
});

// 필드 편집
$(function() {
	$("#field_input").autocomplete({
		source : "/field/_search",
		select : function(event, ui) {
			var item = ui.item;
			$("#fields").append(
				"<tr id='field_" + item.value + "'>" +
				"	<input type='hidden' name='fieldIds' value='" + item.value +"'/>"+
				"	<td>" + item.label + "</td>"+
				"	<td>" + item.description + "</td>"+
				"	<td><button class='_remove_field' data-id='" + item.value + "'>X</button></td>"+
				"</tr>");
		},
		close : function(event) {
			event.target.value = '';
		}
	});
	
	$("#fields").on("click", "button._remove_field", function(ev) {
		$("tr#field_" + $(ev.target).attr('data-id')).remove();
	});
});
