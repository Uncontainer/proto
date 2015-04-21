<#include "/WEB-INF/ftl/common.ftl">
<#include "/WEB-INF/ftl/common/locale_value_editor.ftl">

<div class="topMenu">
	<#if resource.typeClassId?has_content>
		<@classLink classId=resource.typeClassId/>
		| <a href="/resource/list.do?typeClassId=${resource.typeClassId}">목록</a>
	</#if>
</div>

<div class="contentBody">
	<fieldset>
		<legend>기본 정보</legend>
		<table style="width:100%;">
			<input type="hidden" name="id" value="${resource.id}"/>
			<tr>
				<td style="width:55px;">ID</td>
				<td>${resource.id}</td>
			</tr>
			<tr>
				<td style="width:55px;">class</td>
				<td>
			    	<input type="hidden" name="typeClassId" value="${resource.typeClassId}"/>
			    	<#--
					<ghost:tboxElement target="class" id="${resource.typeClassId}"/>
					-->
				</td>
			</tr>
		</table>
	</fieldset>
	
	<#--========== 이름 수정 테이블 ========== -->
	<fieldset>
		<legend>이름</legend>
		<@resourceNameEditor resourceId=resource.id/>
	</fieldset>
	
	<fieldset>
		<legend>설명</legend>
		<@resourceDescriptionEditor resourceId=resource.id/>
	</fieldset>

	<#--========== triple 관련 link ========== -->
	<!--
	<fieldset>
		<legend>Triples</legend>
		<a href="/triple/list.do?resourceId=${resource.id}">목록</a><br/>
		<a href="/triple/create.do?resourceId=${resource.id}">추가</a>
	</fieldset>
	-->
</div>

