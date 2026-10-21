<#include "../mcitems.ftl">

package ${package}.init;

@JeiPlugin
public class ${JavaModName}AnvilRecipes implements IModPlugin {

    @Override
    public Identifier getPluginUid() {
    	return Identifier.parse("${modid}:anvil_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiAnvilRecipe> anvilRecipes = new ArrayList<>();
        ItemStack rightItem = ItemStack.EMPTY;
        <#list anvilrecipes as recipe>
            rightItem = ${mappedMCItemToItemStackCode(recipe.rightitem)};
            rightItem.setCount(${recipe.rightcost});
            anvilRecipes.add(factory.createAnvilRecipe(${mappedMCItemToItemStackCode(recipe.leftitem)}, List.of(rightItem.copy()), List.of(${mappedMCItemToItemStackCode(recipe.output)}), Identifier.parse("${modid}:${recipe.getModElement().getRegistryName()}")));
        </#list>
        registration.addRecipes(RecipeTypes.ANVIL, anvilRecipes);
    }

}