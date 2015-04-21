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
			var searchForm = $(this).closest('#class_search_form');
			
			$.get('/class/search/submit', 
				searchForm.serialize(),
				function(response) {
					$('#class_search_result tbody').html('');
					
					if (response.success || false) {
						if(response.value.content.length > 0) {
							for(var i=0; i<response.value.content.length; i++) {
								var entry = response.value.content[i];
								$('#class_search_result tbody')
									.append($('<tr/>').css({'vertical-align':'top'}))
									.append($.tmpl("entryTemplate", entry));
							}
						} else {
							$('#class_search_result tbody').append($('<tr><td colspan="3" style="text-align:center">검색 결과가 없습니다.</td></tr>'));
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
			$('._class_id').live('click', function(ev) {
				var classId = $(this).attr('data-class-id');
				try {
					top.opener.setClassId('${tagId}', classId);
				} catch(e){
					alert("부모창이 변경되었거나 스크립트에 오류가 있습니다.");
				}
				
				top.close();
			}); 
		<#else>
			$('._class_id').live('click', function(ev) {
				var classId = $(this).attr('data-class-id');
				window.location = "/class/read?classId=" + classId;
			});
		</#if>
	});
</@y.script>
</head>
<body>
	<form id="class_search_form" action='/class/search/submit' method='get'>
		<@selectCell name='type' values=types selectedValue=criteria.type />
		<input name="keyword" value="${criteria.keyowrd!}"/>
		<button id="search_button">검색</button>
	</form>
	
	<table id="class_search_result">
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
					<a class="_class_id" href="#" data-class-id="${r"${id}"}">${r"${id}"}</a>
				</td>
				<td>${r"${name}"}</td>
				<td>${r"${description}"}</td>
			</tr>
		</tbody>
	</table>
</body>
</html>
