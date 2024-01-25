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
            ItemStack potion = new ItemStack(Items.POTION);
            ItemStack potion2 = new ItemStack(Items.POTION);
		    <#list brewingRecipes as recipe>
		        <#if recipe.brewingReturnStack?starts_with("POTION:") || recipe.brewingInputStack?starts_with("POTION:")>
		                <#if recipe.brewingReturnStack?starts_with("POTION:") && recipe.brewingInputStack?starts_with("POTION:")>
                            PotionUtils.setPotion(potion, ${generator.map(recipe.brewingInputStack?replace("POTION:",""), "potions")});
                            PotionUtils.setPotion(potion2, ${generator.map(recipe.brewingReturnStack?replace("POTION:",""), "potions")});
                            brewingRecipes.add(factory.createBrewingRecipe(
                                List.of(${mappedMCItemToItemStackCode(recipe.brewingIngredientStack)}), potion.copy(), potion2.copy()));
		                <#elseif recipe.brewingReturnStack?starts_with("POTION:")>
		                    PotionUtils.setPotion(potion, ${generator.map(recipe.brewingReturnStack?replace("POTION:",""), "potions")});
		                    brewingRecipes.add(factory.createBrewingRecipe(
                                List.of(${mappedMCItemToItemStackCode(recipe.brewingIngredientStack)}), ${mappedMCItemToItemStackCode(recipe.brewingInputStack)}, potion.copy()));
		                <#else>
		                    PotionUtils.setPotion(potion, ${generator.map(recipe.brewingInputStack?replace("POTION:",""), "potions")});
		                    brewingRecipes.add(factory.createBrewingRecipe(
                                List.of(${mappedMCItemToItemStackCode(recipe.brewingIngredientStack)}), potion.copy(), ${mappedMCItemToItemStackCode(recipe.brewingReturnStack)}));
		                </#if>
		        <#else>
                    brewingRecipes.add(factory.createBrewingRecipe(
                        List.of(${mappedMCItemToItemStackCode(recipe.brewingIngredientStack)}), ${mappedMCItemToItemStackCode(recipe.brewingInputStack)}, ${mappedMCItemToItemStackCode(recipe.brewingReturnStack)}));
		        </#if>
		    </#list>
		    registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
		</#if>
    }

}