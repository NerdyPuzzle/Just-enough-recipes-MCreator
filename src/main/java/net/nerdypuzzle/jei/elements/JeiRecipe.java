package net.nerdypuzzle.jei.elements;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.workspace.elements.ModElement;

import java.util.List;

public class JeiRecipe extends GeneratableElement {

    public String category;
    public MItemBlock result;
    public int count;
    public List<MItemBlock> ingredients;

    public JeiRecipe(ModElement element) {
        super(element);
    }

}
