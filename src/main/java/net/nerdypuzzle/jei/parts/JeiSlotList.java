package net.nerdypuzzle.jei.parts;

import net.mcreator.element.types.Potion;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.minecraft.JEntriesList;
import net.mcreator.ui.minecraft.potions.JPotionListEntry;
import net.nerdypuzzle.jei.elements.JeiRecipeType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JeiSlotList extends JEntriesList {
    private final List<JeiSlotListEntry> entryList = new ArrayList();
    private final JPanel entries = new JPanel(new GridLayout(0, 1, 5, 5));
    public JeiSlotList(MCreator mcreator, IHelpContext gui) {
        super(mcreator, new BorderLayout(), gui);
        this.add.setText(L10N.t("elementgui.jeirecipetype.add_entry", new Object[0]));
        JPanel topbar = new JPanel(new FlowLayout(0));
        topbar.setBackground((Color)UIManager.get("MCreatorLAF.LIGHT_ACCENT"));
        topbar.add(this.add);
        this.add("North", topbar);
        this.entries.setOpaque(false);
        this.add.addActionListener((e) -> {
            JeiSlotListEntry entry = new JeiSlotListEntry(mcreator, gui, this.entries, this.entryList);
            this.registerEntryUI(entry);
        });
        this.add("Center", PanelUtils.pullElementUp(this.entries));
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder((Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"), 1), L10N.t("elementgui.potion.effects", new Object[0]), 0, 0, this.getFont().deriveFont(12.0F), (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR")));
    }

    public List<JeiRecipeType.JeiSlotListEntry> getEntries() {
        return (List)this.entryList.stream().map(JeiSlotListEntry::getEntry).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void setEntries(List<JeiRecipeType.JeiSlotListEntry> pool) {
        pool.forEach((e) -> {
            JeiSlotListEntry entry = new JeiSlotListEntry(this.mcreator, this.gui, this.entries, this.entryList);
            this.registerEntryUI(entry);
            entry.setEntry(e);
        });
    }
}
