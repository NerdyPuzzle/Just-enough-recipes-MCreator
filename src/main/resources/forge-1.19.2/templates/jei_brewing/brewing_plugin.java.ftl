<#include "../mcitems.ftl">

package ${package}.init;

@JeiPlugin
public class ${JavaModName}BrewingRecipes implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
    	return new ResourceLocation("${modid}:brewing_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        <#if w.getRecipesOfType("Brewing")??>
            IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
            List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
		    <#list w.getRecipesOfType("Brewing") as recipe>
                brewingRecipes.add(factory.createBrewingRecipe(
                    List.of(${mappedMCItemToItemStackCode(recipe.getGeneratableElement().brewingIngredientStack)}), ${mappedMCItemToItemStackCode(recipe.getGeneratableElement().brewingInputStack)}, ${mappedMCItemToItemStackCode(recipe.getGeneratableElement().brewingReturnStack)}));
		    </#list>
		    registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
		</#if>
    }

}