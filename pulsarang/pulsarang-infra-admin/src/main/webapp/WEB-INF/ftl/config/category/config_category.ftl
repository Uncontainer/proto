<#include "/WEB-INF/ftl/common.ftl">
<@validateMode />

<script type="text/javascript">
	function removeConfigCategory(categoryName) {
		if(!confirm("'" + categoryName + "'을(를) 삭제하시겠습니까?")) {
			return;
		}
		
		var form = $Form('removeForm');
		form.submit();
	}
</script>


<div class="linkSection">
	<#if mode == 'read'>
		<a href='/config/category/modify/${configCategory.name}'>modify</a>
		| <a href='#' onclick="removeConfigCategory('${configCategory.name}');return false;">delete</a>
		
		<form id='removeForm' name='removeForm' method='get' action='/config/category/remove/${configCategory.name}'>
		</form>
	</#if>
</div>

<form id='defalutForm' name='defalutForm' method="post" action="/config/category/${mode}">
	<#if mode != 'read'>
		<div class="headSection">
			<input type='submit' value="${mode}">
		</div>
	</#if>
	
	<div class="bodySection">
		<table>
			<tr>
				<th style="width:150px">name</th>
				<th>value</th>
			</tr>
			<tr>
				<td>name</td>
				<td><@inputCell name="name" value=configCategory.name!'' modifiable=(mode=='create')/></td>
			</tr>
			<tr>
				<td>scope</td>
				<td><@selectCell name='scopeTypeCode' values=scopeTypeCodes selectedValue=configCategory.scopeTypeCode!'' caseSensitive=false modifiable=(mode!='read')/></td>
			</tr>
			<tr>
				<td>description</td>
				<td><@inputCell name="description" value=configCategory.description!'' modifiable=(mode!='read') /></td>
			</tr>
			<tr>
				<td>super categories</td>
				<td><@inputCell name="superCategoryNames" value=configCategory.superCategoryNames!'' modifiable=(mode!='read')/></td>
		    </tr>
		</table>
	</div>
	
	<#if mode != 'read'>
		<div class="tailSection">
			<input type='submit' value="${mode}">
		</div>
	</#if>
</form>
