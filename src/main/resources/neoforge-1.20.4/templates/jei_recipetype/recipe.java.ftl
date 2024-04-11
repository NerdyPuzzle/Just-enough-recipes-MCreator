package ${package}.jei_recipes;

<#compress>

import javax.annotation.Nullable;

public class ${name}Recipe implements Recipe<SimpleContainer> {
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public ${name}Recipe(ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess access) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return output.copy();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Type implements RecipeType<${name}Recipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "${data.getModElement().getRegistryName()}";
    }

    public static class Serializer implements RecipeSerializer<${name}Recipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final Codec<${name}Recipe> CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                        ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                        Ingredient.CODEC_NONEMPTY
                            .listOf()
                            .fieldOf("ingredients")
                            .flatXmap(
                                ingredients -> {
                                    Ingredient[] aingredient = ingredients
                                              .toArray(Ingredient[]::new); // Skip the empty check and create the array.
                                    if (aingredient.length == 0) {
                                        return DataResult.error(() -> "No ingredients found in custom recipe");
                                    } else {
                                        return DataResult.success(NonNullList.of(Ingredient.EMPTY, aingredient));
                                    }
                                },
                                DataResult::success
                            )
                            .forGetter(recipe -> recipe.recipeItems)
                    )
                    .apply(builder, ${name}Recipe::new)
        );

        @Override
        public Codec<${name}Recipe> codec() {
            return CODEC;
        }

        @Override
        public @Nullable ${name}Recipe fromNetwork(FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            return new ${name}Recipe(buf.readItem(), inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ${name}Recipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ing : recipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItem(recipe.getResultItem(null));
        }
    }

}</#compress>