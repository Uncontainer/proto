<div>
	<a href="/project/create">생성</a>
</div>
<div class="tbl_type1">
	<table>
		<thead>
			<tr>
				<th style="width:70px">solution</th>
				<th style="width:150px">project</th>
				<th style="width:50px">portPrefix</th>
				<th style="width:70px">op</th>
				<th>&nbsp;</th>
			</tr>
		</thead>
		<tbody>	
			<#list projects as project>
				<tr>
					<td>${project.solutionName}</td>
					<td>${project.projectName}</td>
					<td>${project.portPrefix?c}</td>
					<td>
						<a href="/project/modify?id=${project.id?c}">수정</a>
						| <a href="/project/remove?id=${project.id?c}">삭제</a>
					</td>
					<td>&nbsp;</td>
			    </tr> 
			</#list>
		</tbody>
	</table>
</div>