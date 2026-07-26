<#include "mcitems.ftl">
<#assign recipeName = field$recipe?replace("CUSTOM:", "")>
<#assign shapeless = (field$shapeless!"FALSE") == "TRUE">
{
	if (world instanceof Level _lvl) {
		net.minecraft.world.item.crafting.RecipeManager rm = _lvl.getRecipeManager();
		List<${recipeName}Recipe> recipes = rm.getAllRecipesFor(${recipeName}Recipe.Type.INSTANCE).stream().map(RecipeHolder::value).collect(Collectors.toList());
		for (${recipeName}Recipe recipe : recipes) {
			<#if shapeless>
				List<Ingredient> _ingredients = new ArrayList<>(recipe.getIngredients());
				<#assign hasCounts = w.getWorkspace().getModElementByName(recipeName).getGeneratableElement().enableIntList>
				<#if hasCounts>
					List<Integer> _counts = new ArrayList<>(recipe.integers());
					List<Integer> _orderedCounts = new ArrayList<>();
				</#if>
				boolean _match = true;
				<#list input_list$entry as entry>
					if (_match) {
						ItemStack ingredient = ${mappedMCItemToItemStackCode(entry)};
						boolean found = false;
						<#if hasCounts>
							int index = 0;
						</#if>
						for (Iterator<Ingredient> it = _ingredients.iterator(); it.hasNext();) {
							if (it.next().test(ingredient)) {
								it.remove();
								<#if hasCounts>
									_orderedCounts.add(_counts.remove(index));
								</#if>
								found = true;
								break;
							}
							<#if hasCounts>
								index++;
							</#if>
						}
						if (!found) {
							_match = false;
						}
					}
				</#list>
				if (!_match || !_ingredients.isEmpty()) {
				continue;
				}
			<#else>
				List<Ingredient> _ingredients = recipe.getIngredients();
				List<Integer> _orderedCounts = new ArrayList<>();
				<#list input_list$entry as entry>
					if (!_ingredients.get(${entry?index}).test(${mappedMCItemToItemStackCode(entry)}))
						continue;
				</#list>
			</#if>
			${shapeless?then("boolean _shapeless = true;", "boolean _shapeless = false;")}
			List<ItemStack> reciperesult = recipe.getResultItems();
			${statement$foreach}
			break;
		}
	}
}