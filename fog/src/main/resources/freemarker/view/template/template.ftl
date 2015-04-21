<#include "/view/common/common.ftl">

<@resource src="/js/template/template.js" debug="${RequestParameters['debug']!}"/>

<@resource>
	$(function() {
		${viewLayoutScript!""}
		${editLayoutScript!""}
	});
</@resource>

<style>
  .linear_layout { list-style-type: none; margin: 0; padding: 0; width: 100%; }
  .linear_layout li { margin: 3px 3px 3px 3px; padding: 0.4em; }
</style>

<div class="main_menu">
	<a href="/template">목록</a>
	<#if mode.readOnly>
	| <a href="/template/${template.id?c}/edit">수정</a>
	</#if>
</div>
<div class="data">
	<#if !mode.readOnly>
	<form id="save_form" action="/template/${mode.path}" method="POST">
		<input type="hidden" name="id" value="${template.id?c}"/>
		<input type="hidden" name="viewLayoutHtml" id="view_layout"/>
		<input type="hidden" name="editLayoutHtml" id="edit_layout"/>
	</#if>
		<table>
			<tr>
				<th>이름</th>
				<td>
					<@inputCell name="name" value=template.name size=30 modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<th>설명</th>
				<td>
					<@textCell name="description" value=template.description cols=30 rows=5 modifiable=mode.modifiable/>
				</td>
			</tr>
			<tr>
				<th>대상</th>
				<td>
					<#if template.targetResource?has_content>
						<input type="hidden" name="targetResourceId" value="${template.targetResource.id?c}"/>
						<@resourcePopup resource=template.targetResource showLabel=true/>
					<#else>
						?
					</#if>
				</td>
			</tr>
		</table>
		
		<div id="layout_tabs" style="margin-top:10px">
			<ul>
				<li><a href="#view_layout_area">조회</a></li>
				<li><a href="#edit_layout_area">편집</a></li>
			</ul>
			<div id="view_layout_area" style="width:${deviceWidth?c}px">
				${viewLayoutHtml!""}
			</div>
			<div id="edit_layout_area" style="width:${deviceWidth?c}px">
				${editLayoutHtml!""}
			</div>
		</div>
	<#if !mode.readOnly>
	</form>
	</#if>
</div>

<div class="bottom_menu">
	<#if !mode.readOnly>	
		<button id="save_button">${mode.text}</button>
	</#if>
</div>

