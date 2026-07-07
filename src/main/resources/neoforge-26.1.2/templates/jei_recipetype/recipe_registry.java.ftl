package ${package}.init;

@EventBusSubscriber
public class ${JavaModName}RecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, "${modid}");
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "${modid}");
    public static RecipeMap recipes = null;

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        IEventBus bus = ModList.get().getModContainerById("${modid}").get().getEventBus();
		event.enqueueWork(() -> {
		    RECIPE_TYPES.register(bus);
		    SERIALIZERS.register(bus);

            <#list jeirecipetypes as type>
                RECIPE_TYPES.register("${type.getModElement().getRegistryName()}", () -> ${type.getModElement().getName()}Recipe.Type.INSTANCE);
                SERIALIZERS.register("${type.getModElement().getRegistryName()}", () -> ${type.getModElement().getName()}Recipe.SERIALIZER);
            </#list>
		});
    }

    @SubscribeEvent
    public static void syncRecipes(OnDatapackSyncEvent event) {
        <#list jeirecipetypes as type>
            event.sendRecipes(${type.getModElement().getName()}Recipe.Type.INSTANCE);
        </#list>
    }

    @EventBusSubscriber(value = Dist.CLIENT)
    public static class RecipeReceiver {

        @SubscribeEvent
        public static void receiveRecipes(RecipesReceivedEvent event) {
            recipes = event.getRecipeMap();
        }

        @SubscribeEvent
        public static void clearRecipes(ClientPlayerNetworkEvent.LoggingOut event) {
            recipes = null;
        }

    }

}