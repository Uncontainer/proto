<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ko">
	<head>
		<@tiles.insertAttribute name="meta"/>
        	<link type="text/css" rel="stylesheet" href="/css/pulsarang_common.css">
            <link type="text/css" rel="stylesheet" href="/css/common.css">
        	<#--
            <link type="text/css" rel="stylesheet" href="${urls['static']}/css/service/manager_admin/nap/tree.css">
            <link type="text/css" rel="stylesheet" href="${urls['static']}/css/component/uio_calendar.css">
            <link type="text/css" rel="stylesheet" href="${urls['static']}/css/component/grid.css">
            <link type="text/css" rel="stylesheet" href="${urls['static']}/js/component/example/NMarketCash-Calendar/css/cashcenter.css">
            -->
            <style>
                h2 {background: none}
                h3 {background: none}
            </style>
		<title><@tiles.getAsString name="title"/></title>
		<#include "/WEB-INF/layouts/includes/common_top_script.ftl"/>
	</head>
	<body>
		<#include "/WEB-INF/layouts/includes/common_bottom_script.ftl"/>
		<table border='0'>
			<tr>
				<td style="width:150px;vertical-align:top;padding-left:5px;padding-top:50px">
					<@tiles.insertAttribute name="left"/>
				</td>
				<td style="vertical-align:top;padding-top:10px;padding-right:5px">
					<@tiles.insertAttribute name="body"/>
				</td>
			</tr>
		</table>
		<#--
		<@script flush="true"/>
		-->
		<@tiles.insertAttribute name="service.script"/>
	</body>
</html>
