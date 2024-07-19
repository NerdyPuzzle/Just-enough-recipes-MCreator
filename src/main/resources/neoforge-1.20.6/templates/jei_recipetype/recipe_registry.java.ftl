package ${package}.init;

@EventBusSubscriber(modid = ${JavaModName}.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ${JavaModName}RecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, "${modid}");

    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		event.enqueueWork(() -> {
		    SERIALIZERS.register(bus);

            <#list jeirecipetypes as type>
                SERIALIZERS.register("${type.getModElement().getRegistryName()}", () -> ${type.getModElement().getName()}Recipe.Serializer.INSTANCE);
            </#list>
		});
    }

}