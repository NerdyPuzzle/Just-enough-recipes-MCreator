package net.nerdypuzzle.jei.elements;

import net.mcreator.minecraft.ElementUtil;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JEmptyBox;
import net.mcreator.ui.component.JMinMaxSpinner;
import net.mcreator.ui.component.SearchableComboBox;
import net.mcreator.ui.component.util.ComponentUtils;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.laf.renderer.WTextureComboBoxRenderer;
import net.mcreator.ui.minecraft.MCItemHolder;
import net.mcreator.ui.modgui.ModElementGUI;
import net.mcreator.ui.validation.AggregatedValidationResult;
import net.mcreator.ui.validation.ValidationGroup;
import net.mcreator.ui.validation.component.VComboBox;
import net.mcreator.ui.validation.component.VTextField;
import net.mcreator.ui.validation.validators.MCItemHolderValidator;
import net.mcreator.ui.validation.validators.TextFieldValidator;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.util.StringUtils;
import net.mcreator.workspace.elements.ModElement;
import net.nerdypuzzle.jei.parts.JeiSlotList;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class JeiRecipeTypeGUI extends ModElementGUI<JeiRecipeType> {

    private final VTextField title;
    private MCItemHolder icon;
    private MCItemHolder craftingtable;
    private JCheckBox enableCraftingtable;
    private final VComboBox<String> textureSelector;
    private JeiSlotList slotList;
    private JSpinner width;
    private JSpinner height;
    private final ValidationGroup page1group = new ValidationGroup();

    public JeiRecipeTypeGUI(MCreator mcreator, ModElement modElement, boolean editingMode) {
        super(mcreator, modElement, editingMode);
        title = new VTextField(24);
        textureSelector= new SearchableComboBox((String[])mcreator.getFolderManager().getTexturesList(TextureType.SCREEN).stream().map(File::getName).toArray((x$0) -> {
            return new String[x$0];
        }));
        slotList = new JeiSlotList(this.mcreator, this);
        width = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        height = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        enableCraftingtable = L10N.checkbox("elementgui.common.enable", new Object[0]);
        this.initGUI();
        super.finalizeGUI();
    }

    protected void initGUI() {
        textureSelector.setRenderer(new WTextureComboBoxRenderer.TypeTextures(mcreator.getWorkspace(), TextureType.SCREEN));
        icon = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);
        craftingtable = new MCItemHolder(mcreator, ElementUtil::loadBlocksAndItems);

        JPanel pane1 = new JPanel(new BorderLayout());
        pane1.setOpaque(false);
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 0, 2));
        mainPanel.setOpaque(false);
        mainPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/title"), L10N.label("elementgui.jeirecipetype.title", new Object[0])));
        mainPanel.add(title);
        mainPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/icon"), L10N.label("elementgui.jeirecipetype.icon", new Object[0])));
        mainPanel.add(icon);

        JPanel subPanel3 = new JPanel(new GridLayout(1, 2, 0, 2));
        subPanel3.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/crafting_table"), L10N.label("elementgui.jeirecipetype.crafting_table", new Object[0])));
        subPanel3.setOpaque(false);
        enableCraftingtable.setOpaque(false);
        subPanel3.add(enableCraftingtable);
        mainPanel.add(subPanel3);
        mainPanel.add(craftingtable);

        enableCraftingtable.addActionListener((e) -> {
            craftingtable.setEnabled(enableCraftingtable.isSelected());
        });

        mainPanel.add(HelpUtils.wrapWithHelpButton(this.withEntry("jei/background"), L10N.label("elementgui.jeirecipetype.background", new Object[0])));
        mainPanel.add(textureSelector);
        JPanel subPanel1 = new JPanel(new GridLayout(1, 3, 0, 1));
        subPanel1.setOpaque(false);
        JPanel subPanel2 = new JPanel(new GridLayout(1, 2, 0, 1));
        subPanel2.setOpaque(false);
        subPanel1.add(L10N.label("elementgui.jeirecipetype.width", new Object[0]));
        subPanel1.add(width);
        subPanel1.add(new JEmptyBox());
        subPanel2.add(L10N.label("elementgui.jeirecipetype.height", new Object[0]));
        subPanel2.add(height);
        mainPanel.add(subPanel1);
        mainPanel.add(subPanel2);
        ComponentUtils.deriveFont(title, 16.0F);

        JComponent mainEditor = PanelUtils.northAndCenterElement(HelpUtils.wrapWithHelpButton(this.withEntry("jei/slots"), L10N.label("elementgui.jeirecipetype.slots", new Object[0])), this.slotList);
        mainEditor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pane1.add(PanelUtils.northAndCenterElement(PanelUtils.join(0, new Component[]{mainPanel}), mainEditor));

        this.title.setValidator(new TextFieldValidator(this.title, L10N.t("elementgui.jeirecipetype.recipetype_needs_title", new Object[0])));
        this.title.enableRealtimeValidation();
        this.page1group.addValidationElement(title);
        this.icon.setValidator(new MCItemHolderValidator(this.icon));
        this.page1group.addValidationElement(icon);
        this.craftingtable.setValidator(new MCItemHolderValidator(this.craftingtable));
        this.page1group.addValidationElement(craftingtable);

        if (!this.isEditingMode()) {
            title.setText(StringUtils.machineToReadableName(this.modElement.getName()));
        }

        addPage(L10N.t("elementgui.common.page_properties", new Object[0]), pane1);
    }

    public void reloadDataLists() {
        super.reloadDataLists();
        slotList.reloadDataLists();
    }

    protected AggregatedValidationResult validatePage(int page) {
        if (!mcreator.getWorkspaceSettings().getDependencies().contains("jei"))
            return new AggregatedValidationResult.FAIL(L10N.t("elementgui.jei.needs_api", new Object[0]));
        else if (textureSelector.getSelectedItem().equals("")) {
            return new AggregatedValidationResult.FAIL(L10N.t("elementgui.jeirecipetype.missing_texture", new Object[0]));
        }
        return new AggregatedValidationResult(new ValidationGroup[]{this.page1group});
    }

    @Override
    protected void openInEditingMode(JeiRecipeType jeiRecipe) {
        title.setText(jeiRecipe.title);
        icon.setBlock(jeiRecipe.icon);
        craftingtable.setBlock(jeiRecipe.craftingtable);
        enableCraftingtable.setSelected(jeiRecipe.enableCraftingtable);
        textureSelector.setSelectedItem(jeiRecipe.textureSelector);
        slotList.setEntries(jeiRecipe.slotList);
        width.setValue(jeiRecipe.width);
        height.setValue(jeiRecipe.height);

        craftingtable.setEnabled(enableCraftingtable.isSelected());
    }

    public JeiRecipeType getElementFromGUI() {
        JeiRecipeType recipe = new JeiRecipeType(this.modElement);
        recipe.textureSelector = textureSelector.getSelectedItem();
        recipe.icon = icon.getBlock();
        recipe.craftingtable = craftingtable.getBlock();
        recipe.enableCraftingtable = enableCraftingtable.isSelected();
        recipe.title = title.getText();
        recipe.slotList = slotList.getEntries();
        recipe.width = (int) width.getValue();
        recipe.height = (int) height.getValue();
        return recipe;
    }


}

