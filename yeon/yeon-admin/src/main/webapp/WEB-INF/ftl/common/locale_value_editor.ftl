<#macro localeValueEditor valueType resourceType resourceId>
	<#assign longText = (valueType == 'description' || valueType='desc')/>
	<form class="_resource_locale_value_create_form" id="resource_${valueType}_${resourceId}" data-value-type="${valueType}" data-resource-id="${resourceId}">
		<table>
			<tr>
				<th style="width:50px">Locale</th>
				<th style="width:500px">Value</th>
				<th style="width:100px">OP</th>
				<th>&nbsp;</th>
			</tr>
			<tr class="_add_area" style="vertical-align:top">
				<input type="hidden" name="resourceType" value="${resourceType}"/>
				<input type="hidden" name="resourceId" value="${resourceId}"/>
				<td>
					<input type="text" name="locale" style="width:25px" maxlength="3"/>
				</td>
				<td>
					<#if longText>
						<textarea name="value" style="width:95%" rows='2'></textarea>
					<#else>
						<input type="text" name="value" style="width:95%"/>
					</#if>
				</td>
				<td><button class="_add_button">추가</button></td>
				<td>&nbsp;</td>
			</tr>
			<tr class="_entry_template" style="display:none">
				<td class="_locale">${r"${locale}"}</td>
				<td class="_value">${r"${value}"}</td>
				<td><a class="_modify_button" data-entry-id="${r"${id}"}" href="#">수정</a> | <a class="_remove_button" data-entry-id="${r"${id}"}" href="#">삭제</a></td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</form>
	
	<@y.resource src="/js/jquery.tmpl.js"/>
	<@y.resource src="/js/jquery.yeon.locale_value_editor.js"/>

	<@y.script>
		$(function(){	$('#resource_${valueType}_${resourceId}').${valueType}Editor();	});
	</@y.script>
</#macro>

<#macro resourceNameEditor resourceId>
	<@localeValueEditor valueType='name' resourceType='RESOURCE' resourceId=resourceId/>
</#macro>

<#macro classNameEditor resourceId>
	<@localeValueEditor valueType='name' resourceType='CLASSS' resourceId=resourceId/>
</#macro>

<#macro propertyNameEditor resourceId>
	<@localeValueEditor valueType='name' resourceType='PROPERTY' resourceId=resourceId/>
</#macro>

<#macro resourceDescriptionEditor resourceId>
	<@localeValueEditor valueType='description' resourceType='RESOURCE' resourceId=resourceId/>
</#macro>

<#macro classDescriptionEditor resourceId>
	<@localeValueEditor valueType='description' resourceType='CLASSS' resourceId=resourceId/>
</#macro>

<#macro propertyDescriptionEditor resourceId>
	<@localeValueEditor valueType='description' resourceType='PROPERTY' resourceId=resourceId/>
</#macro>
