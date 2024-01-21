package net.nerdypuzzle.jei.parts;

import net.mcreator.element.BaseType;
import net.mcreator.element.ModElementType;
import net.nerdypuzzle.jei.elements.*;

import static net.mcreator.element.ModElementTypeLoader.register;

public class PluginElementTypes {
    public static ModElementType<?> JEIRECIPETYPE;
    public static ModElementType<?> JEIRECIPE;
    public static ModElementType<?> ANVILRECIPE;

    public static void load() {

        JEIRECIPETYPE = register(
                new ModElementType<>("jeirecipetype", (Character) 'C', BaseType.OTHER , JeiRecipeTypeGUI::new, JeiRecipeType.class)
        );

        JEIRECIPE = register(
                new ModElementType<>("jeirecipe", (Character) 'R', BaseType.OTHER, JeiRecipeGUI::new, JeiRecipe.class)
        );

        ANVILRECIPE = register(
                new ModElementType<>("anvilrecipe", (Character) 'A', BaseType.OTHER, AnvilRecipeGUI::new, AnvilRecipe.class)
        );

    }

}