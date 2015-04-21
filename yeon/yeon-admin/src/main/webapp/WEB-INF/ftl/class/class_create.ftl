<#include "/WEB-INF/ftl/common.ftl">
<#include "/WEB-INF/ftl/class/class_finder.ftl">

<@y.script>
	$(function() {
		$('#create_class').bind('click', function(ev) {
			jQuery("#classForm").submit();
			return false;
		});
	});
</@y.script>
 
<div class="contentBody">
    <form name="classForm" id="classForm" method="post" action="/class/createSubmit.do">
		<table style="width:100%;">
			<tr>
				<td style="width:55px;">Alias</td>
				<td><input type="text" name="alias" style="width:100%"/></td>
			</tr>
			<tr>
				<td>이름</td>
				<td>
					<input type="text" name="locale" size="3" maxlength="3"/>
					<input type="text" name="name"/>
				</td>
			</tr>
			<tr>
				<td>Super</td>
				<td>
					<@classFinder name="superClassIds" multi=true/>
				</td>
			</tr>
			<tr>
				<td valign="top">설명</td>
				<td><textarea name="description" style="width:100%">${class.description!''}</textarea></td>
			</tr>
		</table>

		<#--========== 저장하기 버튼 ==========-->
		<button id="create_class">추가</button>
	</form>
</div>
 