<div>
	<a href="/server/create">생성</a>
</div>
<div class="tbl_type1">
	<table>
		<thead>
			<tr>
				<th style="width:70px">solution</th>
				<th style="width:150px">project</th>
				<th style="width:150px">name</th>
				<th style="width:70px">IP</th>
				<th style="width:50px">port</th>
				<th style="width:60px">running</th>
				<th style="width:60px">regist</th>
				<th style="width:130px">last startup</th>
				<th style="width:130px">last shutdown</th>
				<th style="width:70px">op</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>	
			<#list servers as server>
				<#assign serverName=server.server_name!''>
				<#assign projectName=server.project_name!''>
				
				<tr>
					<td>${server.solutionName}</td>
					<td>${server.projectName}</td>
					<td>${server.name}</td>
					<td>${server.ip}</td>
					<td>${server.port?c}</td>
					<td>${server.runningStatus!''}</td>
					<td>${server.registStatus!''}</td>
					<td>${server.lastStartupDate!''}</td>
					<td>${server.lastShutdownDate!''}</td>
					<td>
						<a href="/server/modify?id=${server.id?c}">수정</a>
						| <a href="/server/remove?id=${server.id?c}">삭제</a>
					</td>
					<td>&nbsp;</td>
			    </tr> 
			</#list>
		</tbody>
	</table>
</div>