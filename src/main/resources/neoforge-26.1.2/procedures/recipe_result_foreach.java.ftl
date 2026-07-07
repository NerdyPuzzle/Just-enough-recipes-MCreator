<#include "mcitems.ftl">
<#assign recipeName = field$recipe?replace("CUSTOM:", "")>
{
    List<${recipeName}Recipe> recipes = null;
    if (world instanceof ServerLevel _lvl) {
        recipes = _lvl.recipeAccess().recipeMap().byType(${recipeName}Recipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
    } else {
        recipes = ${JavaModName}RecipeTypes.recipes.byType(${recipeName}Recipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
    }
    for (${recipeName}Recipe recipe : recipes) {
        List<Ingredient> ingredients = recipe.getIngredients();
        <#list input_list$entry as entry>
            if (!ingredients.get(${entry?index}).test(${mappedMCItemToItemStackCode(entry)}))
                continue;
        </#list>
        List<ItemStack> reciperesult = recipe.getResultItems();
        ${statement$foreach}
        break;
    }
}