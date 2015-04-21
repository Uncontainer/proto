<#include "/WEB-INF/ftl/common.ftl">

<@y.resource src="/js/jquery.tmpl.js"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Class 목록</title>

<@y.script>
	$(function() {
		$.template("entryTemplate", $('#result_row_template').show().html() );
		$('#result_row_template').remove();
	
		$('#search_button').bind('click', function(ev) {
			var searchForm = $(this).closest('#resource_search_form');
			
			$.get('/resource/search/submit', 
				searchForm.serialize(),
				function(response) {
					$('#resource_search_result tbody').html('');
					
					if (response.success || false) {
						if(response.value.content.length > 0) {
							for(var i=0; i<response.value.content.length; i++) {
								var entry = response.value.content[i];
								$('#resource_search_result tbody')
									.append($('<tr/>').css({'vertical-align':'top'}))
									.append($.tmpl("entryTemplate", entry));
							}
						} else {
							$('#resource_search_result tbody').append($('<tr><td colspan="3" style="text-align:center">검색 결과가 없습니다.</td></tr>'));
						}
					} else {
						if (response.message) {
							alert(response.message);
						} else {
							alert('에러 발생');
						}
					}
				});
				
			return false;
		});
		
		<#if popup>
			$('._resource_id').live('click', function(ev) {
				var resourceId = $(this).attr('data-resource-id');
				try {
					top.opener.setClassId('${tagId}', resourceId);
				} catch(e){
					alert("부모창이 변경되었거나 스크립트에 오류가 있습니다.");
				}
				
				top.close();
			}); 
		</#if>
	});
</@y.script>
</head>
<body>
	<form id="resource_search_form" action='/resource/search/submit' method='get'>
		<@selectCell name='resourceType' values=resourceTypes selectedValue=criteria.resourceType />
		<@selectCell name='searchType' values=searchTypes selectedValue=criteria.searchType />
		<input name="keyword" value="${criteria.keyowrd!}"/>
		<button id="search_button">검색</button>
	</form>
	
	<table id="resource_search_result">
		<thead>
			<tr>
				<th style="width:100px">ID</th>
				<th style="width:150px">이름</th>
				<th>설명</th>
			</tr>
		</thead>
		<tbody>
			<tr id="result_row_template" style="display:none">
				<td>
					<#if popup>
						<a class="_resource_id" href="#" data-resource-id="${r"${id}"}">${r"${id}"}</a>
					<#else>
						${r"${id}"}
					</#if>
				</td>
				<td>${r"${name}"}</td>
				<td>${r"${description}"}</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
