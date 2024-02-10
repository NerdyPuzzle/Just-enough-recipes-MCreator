package net.nerdypuzzle.jei.parts;

import net.mcreator.ui.component.TechnicalButton;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.component.VTextField;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class JStringListField extends JPanel {

    private final DefaultListModel<String> entriesModel = new DefaultListModel<>();

    private final TechnicalButton edit = new TechnicalButton(UIRES.get("18px.edit"));
    private final TechnicalButton clear = new TechnicalButton(UIRES.get("18px.removeall"));

    private final List<ChangeListener> changeListeners = new ArrayList<>();

    private boolean uniqueEntries = false;

    /**
     * Sole constructor.
     *
     * @param parent    The parent window that the editor dialog would be shown over.
     * @param validator Function that returns a validator for each list entry when editing them in the dialog,
     *                  {@code null} means no validation.
     */
    public JStringListField(Window parent, @Nullable Function<VTextField, Validator> validator) {
        super(new BorderLayout());

        JList<String> entriesList = new JList<>(entriesModel);
        entriesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        entriesList.setVisibleRowCount(1);
        entriesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        entriesList.setCellRenderer(new CustomListCellRenderer());

        JScrollPane pane = new JScrollPane(PanelUtils.totalCenterInPanel(entriesList));
        pane.setPreferredSize(getPreferredSize());
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pane.setWheelScrollingEnabled(false);
        pane.addMouseWheelListener(new MouseAdapter() {
            @Override public void mouseWheelMoved(MouseWheelEvent evt) {
                int value = pane.getHorizontalScrollBar().getValue();
                if (evt.getWheelRotation() == 1) {
                    value += pane.getHorizontalScrollBar().getBlockIncrement() * evt.getScrollAmount();
                    if (value > pane.getHorizontalScrollBar().getMaximum())
                        value = pane.getHorizontalScrollBar().getMaximum();
                } else if (evt.getWheelRotation() == -1) {
                    value -= pane.getHorizontalScrollBar().getBlockIncrement() * evt.getScrollAmount();
                    if (value < 0)
                        value = 0;
                }
                pane.getHorizontalScrollBar().setValue(value);
            }
        });

        edit.setOpaque(false);
        edit.setMargin(new Insets(0, 0, 0, 0));
        edit.setBorder(BorderFactory.createEmptyBorder());
        edit.setContentAreaFilled(false);
        edit.addActionListener(e -> {
            List<String> newTextList = ListEditorDialog.open(parent, entriesModel.elements(), validator, uniqueEntries);
            if (newTextList != null)
                setTextList((Collection<String>)newTextList);
        });

        clear.setOpaque(false);
        clear.setMargin(new Insets(0, 0, 0, 0));
        clear.setBorder(BorderFactory.createEmptyBorder());
        clear.setContentAreaFilled(false);
        clear.addActionListener(e -> {
            entriesModel.clear();
            changeListeners.forEach(l -> l.stateChanged(new ChangeEvent(this)));
        });

        JPanel controls = PanelUtils.totalCenterInPanel(PanelUtils.join(edit, clear));
        controls.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, (Color)UIManager.get("MCreatorLAF.MAIN_TINT")));
        controls.setOpaque(true);
        controls.setBackground((Color)UIManager.get("MCreatorLAF.BLACK_ACCENT"));

        add("Center", pane);
        add("East", controls);
    }

    @Override public void setEnabled(boolean b) {
        super.setEnabled(b);
        edit.setEnabled(b);
        clear.setEnabled(b);
    }

    /**
     * @param listener Listener object to be registered for listening to value changes of this component.
     */
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    /**
     * @param uniqueEntries Whether duplicate string entries in the list are forbidden.
     * @return This field instance.
     */
    public JStringListField setUniqueEntries(boolean uniqueEntries) {
        this.uniqueEntries = uniqueEntries;
        return this;
    }

    /**
     * @return List of string entries stored in this component.
     */
    public List<String> getTextList() {
        return Collections.list(entriesModel.elements());
    }

    /**
     * @param newTextList List of string entries to be stored in this component.
     */
    public void setTextList(Collection<String> newTextList) {
        entriesModel.clear();
        entriesModel.addAll(newTextList);
        changeListeners.forEach(l -> l.stateChanged(new ChangeEvent(this)));
    }

    private static class CustomListCellRenderer extends JLabel implements ListCellRenderer<String> {

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setOpaque(true);
            setBackground(isSelected ?
                    (Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR") :
                    (Color) UIManager.get("MCreatorLAF.LIGHT_ACCENT"));
            setForeground(isSelected ?
                    (Color) UIManager.get("MCreatorLAF.BLACK_ACCENT") :
                    (Color) UIManager.get("MCreatorLAF.BRIGHT_COLOR"));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 5, (Color) UIManager.get("MCreatorLAF.DARK_ACCENT")),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            setText(value);
            return this;
        }
    }
}
