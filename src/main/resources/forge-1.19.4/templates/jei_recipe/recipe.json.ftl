<#include "../mcitems.ftl">
{
  "type": "${modid}:${data.category}",
  "ingredients": [
    <#assign items = "">
    <#list data.ingredients as item>
        <#assign items += "{${mappedMCItemToIngameItemName(item)}},">
    </#list>
    ${items[0..(items?last_index_of(',') - 1)]}
  ],
  "output": {
    ${mappedMCItemToIngameItemName(data.result)},
    "count": ${data.count}
  }
}