<#include "../mcitems.ftl">
{
  "type": "${modid}:${data.category}",
  "ingredients": [
    <#assign items = "">
    <#list data.ingredients as item>
        <#assign items += "{${mappedMCItemToItemObjectJSON(item)}},">
    </#list>
    ${items[0..(items?last_index_of(',') - 1)]}
  ],
  "output": {
    ${mappedMCItemToItemObjectJSON(data.result)},
    "count": ${data.count}
  }
}