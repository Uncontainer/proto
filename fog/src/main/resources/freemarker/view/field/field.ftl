<#include "/view/common/common.ftl">
<#assign FieldType=enums["com.naver.fog.field.FieldType"]>

<@resource src="/js/field/field.js" debug="${RequestParameters['debug']!}"/>

<div class="main_menu">
	<a href="/field">목록</a>
	<#if mode.readOnly>
	| <a href="/field/${field.id?c}/edit">수정</a>
	</#if>
</div>

<div class="data">
	<#if !mode.readOnly>
	<form id="save_form" action="/field/${mode.path}" method="POST">
		<input type="hidden" name="id" value="${field.id?c}"/>
	</#if>
		<table
			<tr>
				<th>이름</th>
				<td>
					<@inputCell name="name" value=field.name modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<th>Type</th>
				<td>
					<#if mode.readOnly>
						${field.fieldType.text}
					<#else>
						<select id="field_type_selector" name="fieldType">
						<#list FieldType?values as fieldType>
							<option value="${fieldType.name()}"<#if fieldType.id == (field.fieldType.id)!0> selected="selected"</#if>>${fieldType.text}</option>
						</#list>
						</select>
					</#if>
				</td>
			</tr>
			<tr>
				<th>설명</th>
				<td>
					<@inputCell name="description" value=field.description modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<th>Cardinality</th>
				<td><@inputCell name="cardinality" value=field.cardinality modifiable=mode.modifiable/></td>
			</tr>
			<tr>
				<th>Candidates</th>
				<td><@inputCell name="candidate" value=field.candidate modifiable=mode.modifiable/></td>
			</tr>
			<tr id="frame_row"<#if field.fieldType! != FieldType.FRAME> style="display:none"</#if>>
				<th>Frame</th>
				<td>
					<#if mode.modifiable>
						<input id="frame_input" type="text"/><br/>
						<input id="frame_id" name="frameId" type="hidden"/>
						<span id="frame_name">${(field.frame.name)!''}</span>
					<#else>
						${(field.frame.name)!''}
					</#if>
				</td>
			</tr>
			<#if mode != ViewMode.ADD>
			<tr>
				<th>기본 템플릿</th>
				<td><@resourcePopup field.defaultTemplate/></td>
			</tr>
			</#if>
		</table>
	<#if !mode.readOnly>
	</form>
	<#elseif frames?has_content>
		<div style="margin-top:10px">
			<h4>Using Frames</h4>
			<table>
			<#list frames as frame>
				<tr>
					<td><@resourcePopup frame/><td>
					<td>${frame.description}</td>
				</tr>
			</#list>
			</table>
		</div>
	</#if>
</div>

<div class="bottom_menu">
	<#if !mode.readOnly>	
		<button id="save_button">${mode.text}</button>
	</#if>
</div>
