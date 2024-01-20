package net.nerdypuzzle.jei.parts;

import net.mcreator.element.BaseType;
import net.mcreator.element.ModElementType;
import net.nerdypuzzle.jei.elements.JeiRecipe;
import net.nerdypuzzle.jei.elements.JeiRecipeGUI;
import net.nerdypuzzle.jei.elements.JeiRecipeType;
import net.nerdypuzzle.jei.elements.JeiRecipeTypeGUI;

import static net.mcreator.element.ModElementTypeLoader.register;

public class PluginElementTypes {
    public static ModElementType<?> JEIRECIPETYPE;
    public static ModElementType<?> JEIRECIPE;

    public static void load() {

        JEIRECIPETYPE = register(
                new ModElementType<>("jeirecipetype", (Character) 'C', BaseType.OTHER , JeiRecipeTypeGUI::new, JeiRecipeType.class)
        );

        JEIRECIPE = register(
                new ModElementType<>("jeirecipe", (Character) 'R', BaseType.OTHER, JeiRecipeGUI::new, JeiRecipe.class)
        );

    }

}