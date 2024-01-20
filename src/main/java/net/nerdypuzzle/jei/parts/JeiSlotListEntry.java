package net.nerdypuzzle.jei.parts;

import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.workspace.Workspace;
import net.nerdypuzzle.jei.elements.JeiRecipeType;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class JeiSlotListEntry extends JPanel {

    private final JComboBox<String> type = new JComboBox<>(new String[]{"INPUT", "OUTPUT"});
    private final JSpinner x = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    private final JSpinner y = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    private final JSpinner slotid = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    private final Workspace workspace;

    public JeiSlotListEntry(MCreator mcreator, IHelpContext gui, JPanel parent, List<net.nerdypuzzle.jei.parts.JeiSlotListEntry> entryList) {
        super(new FlowLayout(0));
        this.workspace = mcreator.getWorkspace();
        JComponent container = PanelUtils.expandHorizontally(this);
        parent.add(container);
        entryList.add(this);
        this.add(HelpUtils.wrapWithHelpButton(gui.withEntry("jei/slot_type"), L10N.label("elementgui.jeirecipetype.slot_type", new Object[0])));
        this.add(this.type);
        this.add(new JLabel("X:"));
        this.add(this.x);
        this.add(new JLabel("Y:"));
        this.add(this.y);
        this.add(HelpUtils.wrapWithHelpButton(gui.withEntry("jei/slot_id"), L10N.label("elementgui.jeirecipetype.slot_id", new Object[0])));
        this.add(this.slotid);

        JButton remove = new JButton(UIRES.get("16px.clear"));
        remove.setText(L10N.t("elementgui.jeirecipetype.remove_entry", new Object[0]));
        remove.addActionListener((e) -> {
            entryList.remove(this);
            parent.remove(container);
            parent.revalidate();
            parent.repaint();
        });
        this.add(remove);
        parent.revalidate();
        parent.repaint();

        this.type.addActionListener((e) -> {
            slotid.setEnabled(type.getSelectedItem().equals("INPUT"));
        });
    }

    protected void setEntryEnabled(boolean enabled) {
        this.type.setEnabled(enabled);
        this.x.setEnabled(enabled);
        this.y.setEnabled(enabled);
        this.slotid.setEnabled(enabled);
    }

    public JeiRecipeType.JeiSlotListEntry getEntry() {
        JeiRecipeType.JeiSlotListEntry entry = new JeiRecipeType.JeiSlotListEntry();
        entry.type = (String) type.getSelectedItem();
        entry.x = (int) x.getValue();
        entry.y = (int) y.getValue();
        entry.slotid = (int) slotid.getValue();
        return entry;
    }

    public void setEntry(JeiRecipeType.JeiSlotListEntry e) {
        type.setSelectedItem(e.type);
        x.setValue(e.x);
        y.setValue(e.y);
        slotid.setValue(e.slotid);
    }
}
