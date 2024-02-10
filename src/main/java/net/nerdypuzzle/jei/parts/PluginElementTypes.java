package net.nerdypuzzle.jei.parts;

import net.mcreator.element.ModElementType;
import net.nerdypuzzle.jei.elements.*;

import static net.mcreator.element.ModElementTypeLoader.register;

public class PluginElementTypes {
    public static ModElementType<?> JEIRECIPETYPE;
    public static ModElementType<?> JEIRECIPE;
    public static ModElementType<?> ANVILRECIPE;
    public static ModElementType<?> JEIINFORMATION;

    public static void load() {

        JEIRECIPETYPE = register(
                new ModElementType<>("jeirecipetype", (Character) 'C', JeiRecipeTypeGUI::new, JeiRecipeType.class)
        );

        JEIRECIPE = register(
                new ModElementType<>("jeirecipe", (Character) 'R', JeiRecipeGUI::new, JeiRecipe.class)
        );

        ANVILRECIPE = register(
                new ModElementType<>("anvilrecipe", (Character) 'A', AnvilRecipeGUI::new, AnvilRecipe.class)
        );

        JEIINFORMATION = register(
                new ModElementType<>("jeiinformation", (Character) 'I', JeiInformationGUI::new, JeiInformation.class)
        );

    }

}