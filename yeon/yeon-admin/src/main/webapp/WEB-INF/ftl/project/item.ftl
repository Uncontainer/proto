<#include "/WEB-INF/ftl/common.ftl">

<div class="tbl_type1">
	<form id='defalutForm' name='defalutForm' method='post' action='/project/${mode}'>
		<div class="bodySection">
			<#if mode != 'read'>
				<@hiddenCell name="id" value=(project['id'])!'' show=false />
				<input type='submit' value='${mode}'}/>
			</#if>
			<table>
				<thead>
					<tr>
						<th style="width:100px">name</th>
						<th>value</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>solution</th>
						<td><@inputCell name="solutionName" value=(project['solutionName'])!'' modifiable=(mode=='create') /></td>
				    </tr> 
				    <tr>
				    	<th>project</th>
						<td><@inputCell name="projectName" value=(project['projectName'])!'' modifiable=(mode=='create') /></td>
				    </tr>
				    <tr> 
				    	<th>port</th>
				    	<td><@inputCell name="portPrefix" value=(project['portPrefix']?c)!117 modifiable=(mode!='read') /></td>
				    </tr> 
				</tbody>
			</table>
		</div>
	</form>
</div>
