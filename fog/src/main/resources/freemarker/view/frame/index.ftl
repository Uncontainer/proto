<#include "/view/common/common.ftl">

<div class="main_menu">
	<a href="/frame/add">추가</a>
</div>
<div class="data">
	<table>
		<tr>
			<th>ID</th>
			<th>이름</th>
			<th>설명</th>
			<th>생성자</th>
			<th>생성일</th>
			<th>컨텐츠</th>
		</tr>
	<#list frames as frame>
		<tr>
			<td><@resourceLink frame/></td>
			<td>${frame.name}</td>
			<td>${frame.description!''}</td>
			<td>${frame.creatorId}</td>
			<td>${frame.createdDate?string('yyyy-MM-dd HH:mm:ss')}</td>
			<td class="center"><a href="/content/add/${frame.id?c}">추가</a></td>
		</tr>
	</#list>
	</table>
</div>