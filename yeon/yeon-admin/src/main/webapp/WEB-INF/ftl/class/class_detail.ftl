<#include "/WEB-INF/ftl/common.ftl">
<#include "/WEB-INF/ftl/common/locale_value_editor.ftl">

<div class="topMenu">
</div>

<div class="contentBody">
	<fieldset>
		<legend>기본 정보</legend>
		<table style="width:100%;">
			<input type="hidden" name="id" value="${class.id}"/>
			<tr>
				<th style="width:55px;">ID</th>
				<td>${class.id}</td>
			</tr>
			<tr>
				<th style="vertical-align:top">이름</th>
				<td><@classNameEditor resourceId=class.id/></td>
			</tr>
			<tr>
				<th style="vertical-align:top">설명</th>
				<td><@classDescriptionEditor resourceId=class.id/></td>
			</tr>
		</table>
	</fieldset>
	
	<#--========== Property ========== -->
	<br/>
	<fieldset>
		<legend>Properties <a href="/property/create?domainClassId=${class.id}"><img src="/static/img/btn_plus2.gif"/></a></legend>
		
		<table class="tblmain" style="width:100%">
			<tr>
				<th>ID</th>
				<th>Description</th>
				<th style="width:30px">Delete</th>
				<th>&nbsp;</th>
			</tr>
			<#list properties as property>
				<tr>
					<td style="text-align:left"><a href="/property/read?propertyId=${property.id}">${property.id}</a></td>
					<td>${property.description!}</td>
					<td style="width:30px"><a href="#" onclick="removeProperty('${property.id}')">삭제</a></td>
					<td>&nbsp;</td>
				</tr>
			</#list>
		</table>
	</fieldset>

	<#--========== Instance ========== -->
	<br/>
	<fieldset>
		<legend>Instances</legend>
		 <a href="/instance/list?typeClassId=${class.id}">목록</a><br/>
		 <a href="/instance/create?typeClassId=${class.id}">추가</a>
	</fieldset>
</div>
