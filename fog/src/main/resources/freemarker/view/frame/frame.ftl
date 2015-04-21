<#include "/view/common/common.ftl">

<@resource src="/js/frame/frame.js" debug="${RequestParameters['debug']!}"/>

<@resource>
</@resource>

<div class="main_menu">
	<a href="/frame">목록</a>
	<#if mode.readOnly>
	| <a href="/frame/${frame.id?c}/edit">수정</a>
	</#if>
	| <a href="/content/add/${frame.id?c}">컨텐츠 추가</a>
</div>
<div class="data">
	<#if !mode.readOnly>
	<form id="save_form" action="/frame/${mode.path}" method="POST">
		<input type="hidden" name="id" value="${frame.id?c}"/>
	</#if>
		<table>
			<tr>
				<th>이름</th>
				<td>
					<@inputCell name="name" value=frame.name modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<th>설명</th>
				<td>
					<@textCell name="description" value=frame.description cols=30 rows=5 modifiable=mode.modifiable/>
				</td>
			</tr>
			<#if mode != ViewMode.ADD>
			<tr>
				<th>기본 템플릿</th>
				<td><@resourcePopup frame.defaultTemplate/></td>
			</tr>
			</#if>
			<tr>
				<th style="vertical-align:top">부모 프레임</th>
				<td>
					<#if !mode.readOnly> 
					<div>
						<input id="parent_frame_input" type="text"/><a href="/frame/add" target="_blank">신규</a>
					</div>
					</#if>
					<div>
						<table id="parent_frames">
						<#list frame.parents as parent>
							<tr id='parent_frame_${parent.id?c}'>
								<input type='hidden' name='parentFrameIds' value='${parent.id?c}'/>
								<td>${parent.name}</td>
								<td>${parent.description}</td>
								<#if !mode.readOnly>
								<td><button class='_remove_parent_frame' data-id='${parent.id?c}'>X</button></td>
								</#if>
							</tr>
						</#list>
						</table>
					</div>
				</td>
			</tr>
			<tr>
				<th style="vertical-align:top">필드</th>
				<td>
					<#if !mode.readOnly>
					<div>
						<input id="field_input" type="text"/><a href="/field/add" target="_blank">신규</a>
					</div>
					</#if>
					<div>
						<table id="fields">
						<#list frame.fields as field>
							<tr id='field_${field.id?c}'>
								<input type='hidden' name='fieldIds' value='${field.id?c}'/>
								<td>${field.name}</td>
								<td>${field.description}</td>
								<#if !mode.readOnly>
								<td><button class='_remove_field' data-id='${field.id?c}'>X</button></td>
								</#if>
							</tr>
						</#list>
						</table>
						<table style="margin-top:10px">
							<@displayField frame.parents 1/>
						</table>
					</div>
				</td>
			</tr>
		</table>
	<#if !mode.readOnly>	
	</form>
	<#elseif contents.content?has_content>
		<div style="margin-top:10px">
			<h4>생성된 컨텐츠</h4>
			<table>
			<#list contents.content as content>
				<tr>
					<td><@resourcePopup content/><td>
					<td>${content.description}</td>
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

<#macro displayField frames depth>
	<#assign leftMargin="${depth*15}px"/>
	<#list frames as frame>
		<tr>
			<td colspan="2"><span style="margin-left:${leftMargin};font-style:italic">[<@resourcePopup frame/>]</span></td>
		</tr>
		<#list frame.fields as field>
			<tr>
				<td><span style="margin-left:${leftMargin}"><@resourcePopup field/></span></td>
				<td>${field.description}</td>
			</tr>
		</#list>
		<@displayField frame.parents depth+1/>
	</#list>
</#macro>
