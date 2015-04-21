<#assign ViewMode=enums["com.naver.fog.web.ViewMode"]>
<#assign ResourceType=enums["com.naver.fog.ResourceType"]>

<#macro resourceLink resource target="_self" showLabel=false>
	<#if showLabel>
		${resource.type.name()}:
	</#if>
	<a href="/${resource.type.name()?lower_case}/${resource.id?c}" target="${target}">${resource.name}</a>
</#macro>

<#macro resourcePopup resource showLabel=false>
	<#if showLabel>
		${resource.type.name()}:
	</#if>
	<a href="/${resource.type.name()?lower_case}/${resource.id?c}" target="_blank">${resource.name}</a>
</#macro>

<#macro inputCell name modifiable=true value="" size=30>
	<#if modifiable>
		<input type="text" size="${size?c}" name="${name}" value="${value}">
	<#else>
		<input type="hidden" name="${name}" value="${value}">
		${value}
	</#if>
</#macro>

<#macro textCell name modifiable=true value="" rows=5 cols=30>
	<#if modifiable>
		<textarea rows="${rows?c}" cols="${cols?c}" name="${name}">${value}</textarea>
	<#else>
		${value}
	</#if>
</#macro>

<#macro nameCell resource mode>
	<@inputCell name="name" value=resource.name size=30 modifiable=mode.modifiable/>
</#macro>

<#macro descriptionCell resource mode>
	<@textCell name="description" value=resource.description cols=30 rows=5 modifiable=mode.modifiable/>
</#macro>
