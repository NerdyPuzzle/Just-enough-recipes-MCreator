package net.nerdypuzzle.jei.parts;

import net.mcreator.generator.mapping.MappableElement;
import net.mcreator.minecraft.MCItem;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.init.UIRES;
import net.mcreator.ui.validation.IValidable;
import net.mcreator.ui.validation.Validator;
import net.mcreator.util.FilenameUtilsPatched;
import net.mcreator.util.StringUtils;
import net.mcreator.util.image.ImageUtils;

import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class JItemListFieldMulti<T> extends JPanel implements IValidable {
    private final JButton bt;
    private final JButton bt2;
    private final JButton bt3;
    private final JToggleButton include;
    private final JToggleButton exclude;
    private Validator validator;
    private Validator.ValidationResult currentValidationResult;
    private final DefaultListModel<T> elementsListModel;
    protected final JList<T> elementsList;
    protected final MCreator mcreator;
    private final List<ChangeListener> listeners;

    protected JItemListFieldMulti(MCreator mcreator) {
        this(mcreator, false);
    }

    protected JItemListFieldMulti(MCreator mcreator, boolean excludeButton) {
        this.bt = new JButton(UIRES.get("18px.add"));
        this.bt2 = new JButton(UIRES.get("18px.remove"));
        this.bt3 = new JButton(UIRES.get("18px.removeall"));
        this.include = L10N.togglebutton("elementgui.common.include", new Object[0]);
        this.exclude = L10N.togglebutton("elementgui.common.exclude", new Object[0]);
        this.validator = null;
        this.currentValidationResult = null;
        this.elementsListModel = new DefaultListModel();
        this.elementsList = new JList(this.elementsListModel);
        this.listeners = new ArrayList();
        this.mcreator = mcreator;
        this.setLayout(new BorderLayout());
        this.elementsList.setSelectionMode(1);
        this.elementsList.setVisibleRowCount(1);
        this.elementsList.setLayoutOrientation(2);
        this.elementsList.setCellRenderer(new CustomListCellRenderer());
        this.bt.setOpaque(false);
        this.bt.setMargin(new Insets(0, 0, 0, 0));
        this.bt.setBorder(BorderFactory.createEmptyBorder());
        this.bt.setContentAreaFilled(false);
        this.bt2.setOpaque(false);
        this.bt2.setMargin(new Insets(0, 0, 0, 0));
        this.bt2.setBorder(BorderFactory.createEmptyBorder());
        this.bt2.setContentAreaFilled(false);
        this.bt3.setOpaque(false);
        this.bt3.setMargin(new Insets(0, 0, 0, 0));
        this.bt3.setBorder(BorderFactory.createEmptyBorder());
        this.bt3.setContentAreaFilled(false);
        this.bt.addActionListener((e) -> {
            List<T> list = this.getElementsToAdd();
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                T el = (T) var3.next();
                this.elementsListModel.addElement(el);
            }

            if (!list.isEmpty()) {
                this.listeners.forEach((l) -> {
                    l.stateChanged(new ChangeEvent(e.getSource()));
                });
            }

        });
        this.bt2.addActionListener((e) -> {
            List<T> elements = this.elementsList.getSelectedValuesList();
            Iterator var3 = elements.iterator();

            while(var3.hasNext()) {
                T element = (T) var3.next();
                if (element != null) {
                    this.elementsListModel.removeElement(element);
                    this.listeners.forEach((l) -> {
                        l.stateChanged(new ChangeEvent(e.getSource()));
                    });
                }
            }

        });
        this.bt3.addActionListener((e) -> {
            this.elementsListModel.removeAllElements();
            this.listeners.forEach((l) -> {
                l.stateChanged(new ChangeEvent(e.getSource()));
            });
        });
        final JScrollPane pane = new JScrollPane(PanelUtils.totalCenterInPanel(this.elementsList));
        pane.setHorizontalScrollBarPolicy(31);
        pane.setVerticalScrollBarPolicy(21);
        pane.setWheelScrollingEnabled(false);
        pane.addMouseWheelListener(new MouseAdapter() {
            public void mouseWheelMoved(MouseWheelEvent evt) {
                int amount = evt.getScrollAmount();
                int value;
                if (evt.getWheelRotation() == 1) {
                    value = pane.getHorizontalScrollBar().getValue() + pane.getHorizontalScrollBar().getBlockIncrement() * amount;
                    if (value > pane.getHorizontalScrollBar().getMaximum()) {
                        value = pane.getHorizontalScrollBar().getMaximum();
                    }

                    pane.getHorizontalScrollBar().setValue(value);
                } else if (evt.getWheelRotation() == -1) {
                    value = pane.getHorizontalScrollBar().getValue() - pane.getHorizontalScrollBar().getBlockIncrement() * amount;
                    if (value < 0) {
                        value = 0;
                    }

                    pane.getHorizontalScrollBar().setValue(value);
                }

            }
        });
        pane.setPreferredSize(this.getPreferredSize());
        JComponent buttons = PanelUtils.totalCenterInPanel(PanelUtils.join(new Component[]{this.bt, this.bt2, this.bt3}));
        buttons.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, (Color)UIManager.get("MCreatorLAF.MAIN_TINT")));
        buttons.setOpaque(true);
        buttons.setBackground((Color)UIManager.get("MCreatorLAF.BLACK_ACCENT"));
        if (excludeButton) {
            this.include.setSelected(true);
            ButtonGroup group = new ButtonGroup();
            group.add(this.include);
            group.add(this.exclude);
            this.include.setMargin(new Insets(0, 1, 0, 1));
            this.exclude.setMargin(new Insets(0, 1, 0, 1));
            JComponent incexc = PanelUtils.totalCenterInPanel(PanelUtils.join(new Component[]{this.include, this.exclude}));
            incexc.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, (Color)UIManager.get("MCreatorLAF.MAIN_TINT")));
            this.add(incexc, "West");
        }

        this.add(pane, "Center");
        this.add(buttons, "East");
    }

    protected abstract List<T> getElementsToAdd();

    public void setEnabled(boolean enabled) {
        this.bt.setEnabled(enabled);
        this.bt2.setEnabled(enabled);
        this.bt3.setEnabled(enabled);
        this.include.setEnabled(enabled);
        this.exclude.setEnabled(enabled);
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.listeners.add(changeListener);
    }

    public List<T> getListElements() {
        List<T> retval = new ArrayList();

        for(int i = 0; i < this.elementsListModel.size(); ++i) {
            T element = this.elementsListModel.get(i);
            if (!(element instanceof MappableElement) || ((MappableElement)element).canProperlyMap()) {
                retval.add(this.elementsListModel.get(i));
            }
        }

        return retval;
    }

    public void setListElements(@Nullable List<T> elements) {
        if (elements != null) {
            this.elementsListModel.removeAllElements();
            Iterator var2 = elements.iterator();

            while(var2.hasNext()) {
                T el = (T) var2.next();
                this.elementsListModel.addElement(el);
            }

        }
    }

    public boolean isExclusionMode() {
        return this.exclude.isSelected();
    }

    public void setExclusionMode(boolean isExcluded) {
        this.exclude.setSelected(isExcluded);
        this.include.setSelected(!isExcluded);
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (this.currentValidationResult != null) {
            if (this.currentValidationResult.getValidationResultType() == Validator.ValidationResultType.WARNING) {
                g.drawImage(UIRES.get("18px.warning").getImage(), 0, 0, 13, 13, (ImageObserver)null);
                g.setColor(new Color(238, 229, 113));
            } else if (this.currentValidationResult.getValidationResultType() == Validator.ValidationResultType.ERROR) {
                g.drawImage(UIRES.get("18px.remove").getImage(), 0, 0, 13, 13, (ImageObserver)null);
                g.setColor(new Color(204, 108, 108));
            } else if (this.currentValidationResult.getValidationResultType() == Validator.ValidationResultType.PASSED) {
                g.drawImage(UIRES.get("18px.ok").getImage(), 0, 0, 13, 13, (ImageObserver)null);
                g.setColor(new Color(79, 192, 121));
            }

            if (this.currentValidationResult.getValidationResultType() == Validator.ValidationResultType.ERROR || this.currentValidationResult.getValidationResultType() == Validator.ValidationResultType.WARNING) {
                g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            }
        }

    }

    public Validator.ValidationResult getValidationStatus() {
        Validator.ValidationResult validationResult = this.validator == null ? null : this.validator.validateIfEnabled(this);
        this.currentValidationResult = validationResult;
        this.repaint();
        return validationResult;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidator() {
        return this.validator;
    }

    class CustomListCellRenderer extends JLabel implements ListCellRenderer<T> {
        CustomListCellRenderer() {
        }

        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setOpaque(true);
            this.setBackground(isSelected ? (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR") : (Color)UIManager.get("MCreatorLAF.LIGHT_ACCENT"));
            this.setForeground(isSelected ? (Color)UIManager.get("MCreatorLAF.BLACK_ACCENT") : (Color)UIManager.get("MCreatorLAF.BRIGHT_COLOR"));
            this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 5, 0, 5, (Color)UIManager.get("MCreatorLAF.DARK_ACCENT")), BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            this.setHorizontalAlignment(0);
            this.setVerticalAlignment(0);
            this.setIcon((Icon)null);
            if (value instanceof MappableElement mappableElement) {
                mappableElement.getDataListEntry().ifPresentOrElse((dataListEntry) -> {
                    this.setText(dataListEntry.getReadableName());
                }, () -> {
                    this.setText(mappableElement.getUnmappedValue().replace("CUSTOM:", "").replace("Blocks.", "").replace("Items.", ""));
                });
                if (mappableElement.getUnmappedValue().contains("CUSTOM:")) {
                    this.setIcon(new ImageIcon(ImageUtils.resizeAA(MCItem.getBlockIconBasedOnName(mcreator.getWorkspace(), mappableElement.getUnmappedValue()).getImage(), 18)));
                }

                if (!mappableElement.canProperlyMap()) {
                    this.setIcon(UIRES.get("18px.warning"));
                }
            } else if (value instanceof File) {
                this.setText(FilenameUtilsPatched.removeExtension(((File)value).getName()));
            } else {
                this.setText(StringUtils.machineToReadableName(value.toString().replace("CUSTOM:", "")));
                if (value.toString().contains("CUSTOM:")) {
                    this.setIcon(new ImageIcon(ImageUtils.resizeAA(MCItem.getBlockIconBasedOnName(mcreator.getWorkspace(), value.toString()).getImage(), 18)));
                }
            }

            return this;
        }
    }
}

