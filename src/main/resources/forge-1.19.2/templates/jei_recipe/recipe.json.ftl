<#include "../mcitems.ftl">
{
  "type": "${modid}:${data.category}",
  "ingredients": [
    <#assign items = "">
    <#list data.ingredients as item>
        <#if item.getUnmappedValue() == "Blocks.AIR">
            <#assign items += "{\"tag\": \"${modid}:jei_empty_tag\"},">
        <#else>
            <#assign items += "{${mappedMCItemToIngameItemName(item)}},">
        </#if>
    </#list>
    ${items[0..(items?last_index_of(',') - 1)]}
  ],
  "output": {
    ${mappedMCItemToIngameItemName(data.result)},
    "count": ${data.count}
  }
}