<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Resource 목록</title>

<@y.script>
	function removeResource(resourceId) {
		if(!window.confirm("'" + resourceId + "'를 삭제하시겠습니까?")) {
			return;
		}

		document.location = "/class/remove?resourceId=" + resourceId;
	}
</@y.script>
</head>
<body>
	<div class="topMenu">
		<a href="/class/create">생성</a>
	</div>
	
	<div class="contentBody">
		<table class="tblmain" style="width:500px">
			<tr>
				<th>ID</th>
				<th>Description</th>
				<th style="width:30px">Delete</th>
				<th>&nbsp;</th>
			</tr>
			<#list classes.content as class>
				<tr>
					<td style="text-align:left"><a href="/class/read?resourceId=${class.id}">${class.id}</a></td>
					<td>${class.description!}</td>
					<td style="text-align:center;width:30px"><a href="#" onclick="removeResource('${class.id}')">삭제</a></td>
					<td>&nbsp;</td>
				</tr>
			</#list>
		</table>
		
		<div style="text-align:center;padding-top:10px">
			<ghost:page totalCount="${totalCount}" page="${page}" pageSize="${pageSize}" linkPrefix="/class/list"/>
		</div>
	</div>
	
</body>
</html>
