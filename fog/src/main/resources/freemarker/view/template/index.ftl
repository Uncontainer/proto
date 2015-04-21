<#include "/view/common/common.ftl">

<div class="main_menu">
	<a href="/template/add">추가</a>
</div>
<div class="data">
	<table>
		<tr>
			<th>ID</th>
			<th>이름</th>
			<th>설명</th>
			<th>생성자</th>
			<th>생성일</th>
		</tr>
	<#list templates as template>
		<tr>
			<td><@resourceLink template/></td>
			<td>${template.name}</td>
			<td>${template.description!''}</td>
			<td>${template.creatorId}</td>
			<td>${template.createdDate?string('yyyy-MM-dd HH:mm:ss')}</td>
		</tr>
	</#list>
	</table>
</div>