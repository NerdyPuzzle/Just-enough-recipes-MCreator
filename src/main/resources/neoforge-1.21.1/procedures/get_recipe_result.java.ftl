<#include "mcitems.ftl">
<#assign recipeName = field$recipe?replace("CUSTOM:", "")>
/*@ItemStack*/(new Object() {

    public ItemStack getResult() {
        if (world instanceof Level _lvl) {
            net.minecraft.world.item.crafting.RecipeManager rm = _lvl.getRecipeManager();
            List<${recipeName}Recipe> recipes = rm.getAllRecipesFor(${recipeName}Recipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
            for (${recipeName}Recipe recipe : recipes) {
                NonNullList<Ingredient> _ingredients = recipe.getIngredients();
                <#list input_list$entry as entry>
                    if (!_ingredients.get(${entry?index}).test(${mappedMCItemToItemStackCode(entry)}))
                        continue;
                </#list>
                return recipe.getResultItems().get(0).copy();
            }
        }
        return ItemStack.EMPTY;
    }

}.getResult())