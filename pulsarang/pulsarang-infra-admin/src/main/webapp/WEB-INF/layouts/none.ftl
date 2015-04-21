<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<#include "/WEB-INF/layouts/includes/common_top_script.ftl"/>
<#include "/WEB-INF/layouts/includes/common_bottom_script.ftl"/>
<@tiles.insertAttribute name="body"/>
<@script flush="true"/>
<@tiles.insertAttribute name="service.script" />