<#assign tiles=JspTaglibs["http://tiles.apache.org/tags-tiles"]>
<#include "/layout/includes/common_top_script.ftl"/>
<#include "/layout/includes/common_bottom_script.ftl"/>
<@tiles.insertAttribute name="body"/>
<@resource flush="true"/>
<@tiles.insertAttribute name="service.script" />