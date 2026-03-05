package net.nerdypuzzle.jei.parts.gui.components;

import net.mcreator.blockly.data.Dependency;
import net.mcreator.element.parts.gui.EntityModel;
import net.mcreator.element.parts.gui.GUIComponent;
import net.mcreator.ui.component.util.PanelUtils;
import net.mcreator.ui.dialogs.wysiwyg.AbstractWYSIWYGDialog;
import net.mcreator.ui.help.HelpUtils;
import net.mcreator.ui.help.IHelpContext;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.procedure.AbstractProcedureSelector;
import net.mcreator.ui.procedure.ProcedureSelector;
import net.mcreator.ui.validation.ValidationResult;
import net.mcreator.ui.validation.Validator;
import net.mcreator.ui.validation.validators.ProcedureSelectorValidator;
import net.mcreator.workspace.elements.VariableTypeLoader;
import net.nerdypuzzle.jei.parts.gui.JeiGuiEditor;

import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

public class JeiEntityModelDialog extends AbstractWYSIWYGDialog<EntityModel> {

    public JeiEntityModelDialog(JeiGuiEditor editor, @Nullable EntityModel model) {
        super(editor.getFakeEditor(), model);
        setModal(true);
        setSize(500, 270);
        setLocationRelativeTo(editor.mcreator);
        setTitle(L10N.t("dialog.gui.add_entity_model"));

        JPanel options = new JPanel(new BorderLayout(15, 15));

        AbstractProcedureSelector.ReloadContext context = AbstractProcedureSelector.ReloadContext.create(
                editor.mcreator.getWorkspace());

        ProcedureSelector entityModel = new ProcedureSelector(IHelpContext.NONE.withEntry("gui/entity_model"),
                editor.mcreator, L10N.t("dialog.gui.entity_model_procedure"), ProcedureSelector.Side.CLIENT, false,
                VariableTypeLoader.BuiltInTypes.ENTITY,
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/strings:stringlist"));
        entityModel.refreshList(context);
        entityModel.setValidator(new ProcedureSelectorValidator(entityModel));

        ProcedureSelector displayCondition = new ProcedureSelector(
                IHelpContext.NONE.withEntry("gui/entity_model_display_condition"), editor.mcreator,
                L10N.t("dialog.gui.model_display_condition"), ProcedureSelector.Side.CLIENT, false,
                VariableTypeLoader.BuiltInTypes.LOGIC,
                Dependency.fromString("x:number/y:number/z:number/world:world/entity:entity/strings:stringlist"));
        displayCondition.refreshList(context);

        JSpinner scale = new JSpinner(new SpinnerNumberModel(30, 1, 100, 1));
        JSpinner rotationX = new JSpinner(new SpinnerNumberModel(0, -360, 360, 1));

        JCheckBox followMouseMovement = new JCheckBox();
        followMouseMovement.setOpaque(false);

        JButton ok = new JButton(UIManager.getString("OptionPane.okButtonText"));

        getRootPane().setDefaultButton(ok);

        JButton cancel = new JButton(UIManager.getString("OptionPane.cancelButtonText"));

        JPanel opts = new JPanel(new GridLayout(0, 2, 2, 2));

        opts.add(HelpUtils.wrapWithHelpButton(IHelpContext.NONE.withEntry("gui/entity_model_scale"),
                L10N.label("dialog.gui.model_scale")));
        opts.add(scale);

        opts.add(HelpUtils.wrapWithHelpButton(IHelpContext.NONE.withEntry("gui/entity_model_rotation"),
                L10N.label("dialog.gui.model_rotation_x")));
        opts.add(rotationX);

        opts.add(HelpUtils.wrapWithHelpButton(IHelpContext.NONE.withEntry("gui/entity_model_follow_mouse"),
                L10N.label("dialog.gui.model_follow_mouse")));
        opts.add(followMouseMovement);

        JComboBox<GUIComponent.AnchorPoint> anchor = new JComboBox<>(GUIComponent.AnchorPoint.values());
        anchor.setSelectedItem(GUIComponent.AnchorPoint.CENTER);

        options.add("North", PanelUtils.join(entityModel, displayCondition));
        options.add("Center", PanelUtils.join(FlowLayout.LEFT, opts));
        options.add("South", PanelUtils.join(ok, cancel));

        add("Center", options);

        if (model != null) {
            ok.setText(L10N.t("dialog.common.save_changes"));
            entityModel.setSelectedProcedure(model.entityModel);
            displayCondition.setSelectedProcedure(model.displayCondition);
            scale.setValue(model.scale);
            rotationX.setValue(model.rotationX);
            followMouseMovement.setSelected(model.followMouseMovement);
            anchor.setSelectedItem(model.anchorPoint);
        }

        cancel.addActionListener(e -> dispose());
        ok.addActionListener(e -> {
            if (entityModel.getValidationStatus().type() != ValidationResult.Type.ERROR) {
                dispose();
                if (model == null) {
                    EntityModel component = new EntityModel(0, 0, entityModel.getSelectedProcedure(),
                            displayCondition.getSelectedProcedure(), (int) scale.getValue(), (int) rotationX.getValue(),
                            followMouseMovement.isSelected());
                    setEditingComponent(component);
                    editor.editor.addComponent(component);
                    editor.list.setSelectedValue(component, true);
                    editor.editor.moveMode();
                } else {
                    int idx = editor.components.indexOf(model);
                    editor.components.remove(model);
                    EntityModel modelNew = new EntityModel(model.getX(), model.getY(),
                            entityModel.getSelectedProcedure(), displayCondition.getSelectedProcedure(),
                            (int) scale.getValue(), (int) rotationX.getValue(), followMouseMovement.isSelected());
                    editor.components.add(idx, modelNew);
                    setEditingComponent(modelNew);
                }
            }
        });

        setVisible(true);
    }

}
