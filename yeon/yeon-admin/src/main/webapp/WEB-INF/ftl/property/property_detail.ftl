<#include "/WEB-INF/ftl/common.ftl">
<#include "/WEB-INF/ftl/class/class_finder.ftl">
<#include "/WEB-INF/ftl/common/locale_value_editor.ftl">

<div class="topMenu">
</div>

<div class="contentBody">
	<fieldset>
		<legend>기본 정보</legend>
		<table style="width:100%;">
			<input type="hidden" name="id" value="${property.id}"/>
			<tr>
				<th style="width:55px;">ID</th>
				<td>${property.id}</td>
			</tr>
			<tr>
				<th style="width:55px;">domain</th>
				<td>
					<@classFinder name="domainClassId" classId=property.domainClassId/>
				</td>
			</tr>
			<tr>
				<th>range</th>
				<td>
					<@classFinder name="rangeClassId" classId=property.rangeClassId/>
				</td>
			</tr>
			<tr>
				<th>value-on-timeline</th>
				<td>
					<@selectCell name="valueOnTimelineName" values=valueOnTimelines selectedValue=property.valueOnTimeline!'' modifiable=(mode == 'create') />
				</td>
			</tr>
			<tr>
				<th style="vertical-align:top">이름</th>
				<td><@propertyNameEditor resourceId=property.id/></td>
			</tr>
			<tr>
				<th style="vertical-align:top">설명</th>
				<td><@propertyDescriptionEditor resourceId=property.id/></td>
			</tr>
		</table>
	</fieldset>
</div>
