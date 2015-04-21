<#include "/view/common/common.ftl">

<@resource src="/js/content/content.js" debug="${RequestParameters['debug']!}"/>

<@resource>
</@resource>

<div class="main_menu">
	<a href="/content">목록</a>
	<#if mode.readOnly>
	| <a href="/content/${content.id?c}/edit">수정</a>
	</#if>
</div>
<div class="data">
	<#if !mode.readOnly>
	<form id="save_form" action="/content/${mode.path}" method="POST">
		<input type="hidden" name="id" value="${content.id?c}"/>
	</#if>
		<table>
			<tr>
				<th>이름</th>
				<td>
					<@inputCell name="name" value=content.name modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<th>설명</th>
				<td>
					<@inputCell name="description" value=content.description modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<input type="hidden" name="frameId" value="${content.frameId?c}"/>
				<th style="vertical-align:top">프레임</th>
				<td><@resourcePopup content.frame/></td>
			</tr>
			<#--
			<#list content.frame.fieldsAll as field>
				<tr>
					<th>${field.name}</th>
					<td><@inputCell name="field_${field.id}" value=content.getFieldValue(field.id)!'' modifiable=mode.modifiable/></td>
				</tr>
			</#list>
			-->
		</table>
		
		<br/>
		${html!""}
	<#if !mode.readOnly>	
	</form>
	</#if>
</div>

<div class="bottom_menu">
	<#if !mode.readOnly>	
		<button id="save_button">${mode.text}</button>
	</#if>
</div>
