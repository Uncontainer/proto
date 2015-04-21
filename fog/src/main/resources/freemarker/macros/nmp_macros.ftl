<#macro pagingNavigation criteria={}>
<script>
function page(pageNo){
    document.location.href="?<#if criteria?has_content>paging.</#if>current=" + pageNo;
}
</script>
<#if criteria?has_content>
    <#assign paging=criteria.paging />
</#if>
<#if paging??>
    <a onclick="javascript:page(1)">◀</a>
    <a onclick="javascript:page(${paging.prevBlock})">◁</a>
<#list paging.startBlock..paging.endBlock as pageNo>
<#if pageNo == paging.current><b>${pageNo}</b><#else><a onclick="javascript:page(${pageNo})">${pageNo}</a></#if><#if paging.endBlock != pageNo> | </#if>
</#list>
    <a onclick="javascript:page(${paging.nextBlock})">▷</a>
    <a onclick="javascript:page(${paging.totalPage})">▶</a>
</#if>
</#macro>