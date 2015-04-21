$(function() {
	$("#save_button").bind("click", function(ev) {
		jQuery("#save_form").submit();
	});
});

// 부모 frame 편집
$(function() {
	$("#frame_input").autocomplete({
		source : "/frame/_search",
		select : function(event, ui) {
//			var item = ui.item;
//			$("#parent_frames").append(
//				"<tr id='parent_frame_" + item.value + "'>" +
//				"	<input type='hidden' name='parentFrameIds' value='" + item.value +"'/>"+
//				"	<td>" + item.label + "</td>"+
//				"	<td>" + item.description + "</td>"+
//				"	<td><button class='_remove_parent_frame' data-id='" + item.value + "'>X</button></td>"+
//				"</tr>");
		},
		close : function(event) {
//			event.target.value = '';
		}
	});
});
