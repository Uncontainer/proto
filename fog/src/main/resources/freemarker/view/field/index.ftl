<@resource src="/js/fog.termifier.js" debug="${RequestParameters['debug']!}"/>

<div class="main_menu">
	<a href="/field/add">추가</a>
</div>
<div class="data">
	<table>
		<tr>
			<th>ID</th>
			<th>이름</th>
			<th>Type</th>
			<th>설명</th>
			<th>생성자</th>
			<th>생성일</th>
		</tr>
	<#list fields as field>
		<tr>
			<td><a href="/field/${field.id?c}">${field.id?c}</a></td>
			<td>${field.name}</td>
			<td>${field.fieldType.name()}</td>
			<td>${field.description!''}</td>
			<td>${field.creatorId}</td>
			<td>${field.createdDate?string('yyyy-MM-dd HH:mm:ss')}</td>
		</tr>
	</#list>
	</table>
</div>

<div>
	<label>Features:</label> <abbr>Full-grain</abbr> leather uppers.
	Lather lining <abbr>Vibram</abbr> sole. <abbr>Goodyear welt</abbr>.
</div>