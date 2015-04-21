<div class="main_menu">
	<#--<a href="/content/add">추가</a>-->
</div>
<div class="data">
	<table>
		<tr>
			<th>ID</th>
			<th>frame</th>
			<th>이름</th>
			<th>설명</th>
			<th>생성자</th>
			<th>생성일</th>
		</tr>
	<#list contents as content>
		<tr>
			<td><a href="/content/${content.id?c}">${content.id?c}</a></td>
			<td>${content.frame.name}</td>
			<td>${content.name}</td>
			<td>${content.description!''}</td>
			<td>${content.creatorId}</td>
			<td>${content.createdDate?string('yyyy-MM-dd HH:mm:ss')}</td>
		</tr>
	</#list>
	</table>
</div>