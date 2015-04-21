<#macro classFinder name classId="" multi=false>
	<#assign id = "c" + y.serial.next()/>
	
	<@y.resource src="/js/jquery.yeon.class_finder.js"/>
	<@y.resource src="/js/jquery.popupWindow.js"/>
	<@y.script>
		$(function() {
			$('._${id}').popupWindow({
				windowURL:'/class/search/popup?tagId=${id}', 
				windowName:'Class 검색',
				centerBrowser: 1
			});
		});
		
		function setClassId(tagId, classId) {
			var tag = $('#' + tagId);
			if(tag) {
				<#if multi>
					if(tag.val() == "") {
						tag.val(classId);
					} else {
						tag.val(tag.val() + ", " + classId);
					}
				<#else>
					tag.val(classId);
				</#if>
			} else {
				alert('Class ID를 설정할 tag를 찾지 못하였습니다.');
			}
		};
	</@y.script>
	
	<#if multi>
		<input id="${id}" name="${name}" value="${classId}" style="width:80%"/>
	<#else>
		<input id="${id}" class="_${id}" name="${name}" value="${classId}" readonly="readonly"/>
	</#if>
	<button class="_${id}">선택</button>
	<button>새로 생성</button>
</#macro>