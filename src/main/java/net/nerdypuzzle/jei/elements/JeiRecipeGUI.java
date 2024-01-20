package net.nerdypuzzle.jei.elements;

import net.mcreator.element.ModElementType;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComboBoxUtil;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.minecraft.MCItemListField;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.util.ListUtils;
import net.mcreator.workspace.elements.ModElement;
import net.nerdypuzzle.jei.parts.PluginElementTypes;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class JeiRecipeGUI extends ModElementGUI<JeiRecipe> {
    private final SearchableComboBox<String> category = new SearchableComboBox<>();
    private MCItemHolder result;
    private final JSpinner count = new JSpinner(new SpinnerNumberModel(1, 1, 64, 1));
    private MCItemListField ingredients;

    public JeiRecipeGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        this.initGUI();
        super.finalizeGUI();
    }

    protected void initGUI() {
        ingredients = new MCItemListField(this.mcreator, ElementUtil::loadBlocksAndItemsAndTags, false, true);
        result = new MCItemHolder(this.mcreator, ElementUtil::loadBlocksAndItems);

        JPanel pane3 = new JPanel(new BorderLayout());
        pane3.setOpaque(false);
        JPanel selp = new JPanel(new GridLayout(4, 2, 10, 10));
        selp.setOpaque(false);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/recipe_category"), L10N.label("elementgui.jeirecipe.category", new Object[0])));
        selp.add(category);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/recipe_result"), L10N.label("elementgui.jeirecipe.result", new Object[0])));
        selp.add(result);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/result_count"), L10N.label("elementgui.jeirecipe.result_count", new Object[0])));
        selp.add(count);
        selp.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/recipe_ingredients"), L10N.label("elementgui.jeirecipe.ingredients", new Object[0])));
        selp.add(ingredients);
        pane3.add(PanelUtils.totalCenterInPanel(selp));
        this.addPage(pane3);

    }

    public void reloadDataLists() {
        super.reloadDataLists();
        ComboBoxUtil.updateComboBoxContents(this.category, ListUtils.merge(Collections.singleton("No category"), (Collection)this.mcreator.getWorkspace().getModElements().stream().filter((var) -> {
            return var.getType() == PluginElementTypes.JEIRECIPETYPE;
        }).map(ModElement::getRegistryName).collect(Collectors.toList())), "No category");
    }

    protected AggregatedValidationResult validatePage(int page) {
        if (!mcreator.getWorkspaceSettings().getDependencies().contains("jei"))
            return new AggregatedValidationResult.FAIL(L10N.t("elementgui.jei.needs_api", new Object[0]));
        else if (category.getSelectedItem().equals("No category"))
            return new AggregatedValidationResult.FAIL(L10N.t("elementgui.jeirecipe.no_category", new Object[0]));
        else if (ingredients.getListElements().isEmpty())
            return new AggregatedValidationResult.FAIL(L10N.t("elementgui.jeirecipe.no_ingredients", new Object[0]));
        else if (result.getBlock().isEmpty())
            return new AggregatedValidationResult.FAIL(L10N.t("elementgui.jeirecipe.no_result", new Object[0]));
        return new AggregatedValidationResult.PASS();
    }

    public void openInEditingMode(JeiRecipe tab) {
        category.setSelectedItem(tab.category);
        result.setBlock(tab.result);
        count.setValue(tab.count);
        ingredients.setListElements(tab.ingredients);
    }

    public JeiRecipe getElementFromGUI() {
        JeiRecipe tab = new JeiRecipe(this.modElement);
        tab.category = category.getSelectedItem();
        tab.result = result.getBlock();
        tab.count = (int) count.getValue();
        tab.ingredients = ingredients.getListElements();
        return tab;
    }

}
