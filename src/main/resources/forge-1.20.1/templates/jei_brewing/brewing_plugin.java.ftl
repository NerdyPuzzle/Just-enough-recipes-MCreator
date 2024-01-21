<#include "../mcitems.ftl">

package ${package}.init;

@JeiPlugin
public class ${JavaModName}BrewingRecipes implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
    	return new ResourceLocation("${modid}:brewing_recipes");
    }

    <#assign brewingRecipes = []>
    <#list recipes as recipe>
        <#if recipe.recipeType == "Brewing">
            <#assign brewingRecipes += [recipe]>
        </#if>
    </#list>

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        <#if brewingRecipes??>
            IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
            List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
		    <#list brewingRecipes as recipe>
		        <#if !recipe.brewingReturnStack?starts_with("POTION:") && !recipe.brewingInputStack?starts_with("POTION:")>
                    brewingRecipes.add(factory.createBrewingRecipe(
                        List.of(${mappedMCItemToItemStackCode(recipe.brewingIngredientStack)}), ${mappedMCItemToItemStackCode(recipe.brewingInputStack)}, ${mappedMCItemToItemStackCode(recipe.brewingReturnStack)}));
		        </#if>
		    </#list>
		    registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
		</#if>
    }

}