<#include "../mcitems.ftl">
<#assign recipeElement = w.getWorkspace().getModElementByName(data.recipetype).getGeneratableElement()>
{
  "type": "${modid}:${data.category}",
  "ingredients": [
    <#assign items = "">
    <#list data.ingredients as item>
        <#if item.getUnmappedValue() == "Blocks.AIR">
            <#assign items += "\"#${modid}:jei_empty_tag\",">
        <#else>
            <#assign items += "\"${mappedMCItemToRegistryName(item, true)}\",">
        </#if>
    </#list>
    ${items[0..(items?last_index_of(',') - 1)]}
  ],
  <#if recipeElement.enableIntList>
    "integers": [
        <#list data.integers as integer>
            ${integer}<#sep>,
        </#list>
    ],
  </#if>
  <#if recipeElement.enableStringList>
    "strings": [
        <#list data.strings as string>
            "${string}"<#sep>,
        </#list>
    ],
  </#if>
  "outputs": [
    <#list 0..data.results?size-1 as i>
      <#if data.results[i].getUnmappedValue() != "Blocks.AIR">
        {
          ${mappedMCItemToItemObjectJSON(data.results[i], "id")},
          "count": <#if data.resultCounts[i]??>${data.resultCounts[i]}<#else>1</#if>
        }<#sep>,
      </#if>
    </#list>
  ]
}