<#include "/WEB-INF/ftl/common.ftl">
<#include "/WEB-INF/ftl/class/class_finder.ftl">

<@y.script>
	$(function() {
		$('#create_property').bind('click', function(ev) {
			jQuery("#propertyForm").submit();
			return false;
		};
	});
</@y.script>
 
<div class="contentBody">
    <form name="propertyForm" id="propertyForm" method="post" action="/property/createSubmit.do">
		<table style="width:100%;">
			<tr>
				<td style="width:55px;">이름</td>
				<td><input type="text" name="locale" size="3" maxlength="3"/>&nbsp;<input type="text" name="name"/></td>
			<tr>
			<tr>
				<td valign="top">설명</td>
				<td><textarea name="description" style="width:100%">${property.description!''}</textarea></td>
			</tr>
			<tr>
				<td valign="top">domain</td>
				<td><@classFinder name="domainClassId"/></td>
			</tr>
			<tr>
				<td valign="top">range</td>
				<td><@classFinder name="rangeClassId"/></td>
			</tr>
		</table>

		<#--========== 저장하기 버튼 ==========-->
		<button id="create_property">추가</button>
	</form>
</div>
 