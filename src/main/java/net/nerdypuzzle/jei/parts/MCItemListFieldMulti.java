package net.nerdypuzzle.jei.parts;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.minecraft.MCItem;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.JItemListField;
import net.mcreator.ui.dialogs.MCItemSelectorDialog;
import net.mcreator.util.image.ImageUtils;

public class MCItemListFieldMulti extends JItemListField<MItemBlock> {
    private final MCItem.ListProvider supplier;
    private final boolean supportTags;

    public MCItemListFieldMulti(MCreator mcreator, MCItem.ListProvider supplier) {
        this(mcreator, supplier, false, false);
    }

    public MCItemListFieldMulti(MCreator mcreator, MCItem.ListProvider supplier, boolean excludeButton, boolean supportTags) {
        super(mcreator, excludeButton);
        this.supplier = supplier;
        this.supportTags = supportTags;
        this.elementsList.setCellRenderer(new CustomListCellRenderer());
    }

    public List<MItemBlock> getElementsToAdd() {
        return (List)MCItemSelectorDialog.openMultiSelectorDialog(this.mcreator, this.supplier, this.supportTags).stream().map((e) -> {
            return new MItemBlock(this.mcreator.getWorkspace(), e.getName());
        }).collect(Collectors.toList());
    }

    class CustomListCellRenderer extends JLabel implements ListCellRenderer<MItemBlock> {
        CustomListCellRenderer() {
        }

        public Component getListCellRendererComponent(JList<? extends MItemBlock> list, MItemBlock value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setOpaque(isSelected);
            this.setBackground(isSelected ? (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR") : (Color)UIManager.get("MCreatorLAF.LIGHT_ACCENT"));
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, (Color)UIManager.get("MCreatorLAF.DARK_ACCENT")), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
            this.setHorizontalAlignment(0);
            this.setVerticalAlignment(0);
            this.setToolTipText(value.getUnmappedValue().replace("CUSTOM:", "").replace("Blocks.", "").replace("Items.", ""));
            this.setIcon(new ImageIcon(ImageUtils.resizeAA(MCItem.getBlockIconBasedOnName(mcreator.getWorkspace(), value.getUnmappedValue()).getImage(), 25)));
            this.setText("" + index);
            return this;
        }
    }
}
