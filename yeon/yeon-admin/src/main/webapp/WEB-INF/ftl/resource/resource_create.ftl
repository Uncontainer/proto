<#include "/WEB-INF/ftl/common.ftl">
<#include "/WEB-INF/ftl/class/class_finder.ftl">

<@y.script>
	$(function() {
		$('#create_resource').bind('click', function(ev) {
			jQuery("#resourceForm").submit();
			return false;
		});
	});
</@y.script>
 
<div class="topMenu">
	<#if resource.typeClassId?has_content>
		<a href="/class/read.do?typeClassId=${resource.typeClassId}">class</a>
		| <a href="/resource/list.do?typeClassId=${resource.typeClassId}">목록</a>
	</#if>
</div>

<div class="contentBody">
    <form name="resourceForm" id="resourceForm" method="post" action="/resource/createSubmit.do">
		<table style="width:100%;">
			<tr>
				<td style="width:55px;">Type</td>
				<td>
					<@classFinder name="typeClassId" classId=resource.typeClassId/>
				</td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="locale" size="3" maxlength="3"/>&nbsp;<input type="text" name="name"/></td>
			<tr>
			<tr>
				<td valign="top">설명</td>
				<td><textarea name="description" style="width:100%">${resource.description!''}</textarea></td>
			</tr>
		</table>

		<#--========== 저장하기 버튼 ==========-->
		<button id="create_resource">추가</button>
	</form>
</div>
 