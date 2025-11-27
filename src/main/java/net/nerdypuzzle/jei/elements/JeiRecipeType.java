package net.nerdypuzzle.jei.elements;

import net.mcreator.element.GeneratableElement;
import net.mcreator.element.parts.GridSettings;
import net.mcreator.element.parts.MItemBlock;
import net.mcreator.element.parts.gui.GUIComponent;
import net.mcreator.element.types.interfaces.IGUI;
import net.mcreator.ui.workspace.resources.TextureType;
import net.mcreator.workspace.elements.ModElement;
import net.mcreator.workspace.references.ModElementReference;
import net.mcreator.workspace.references.TextureReference;
import net.nerdypuzzle.jei.parts.gui.JeiGui;

import java.util.ArrayList;
import java.util.List;

public class JeiRecipeType extends GeneratableElement implements IGUI {

    @TextureReference(TextureType.SCREEN)
    public String textureSelector;
    public int width;
    public int height;
    public MItemBlock icon;
    public MItemBlock craftingtable;
    public List<MItemBlock> craftingtables;
    public boolean enableCraftingtable;
    public boolean enableIntList;
    public boolean enableStringList;
    public boolean disableJeiBorder;
    public String title;
    public List<JeiSlotListEntry> slotList = new ArrayList<>();

    // Gui editor
    @ModElementReference
    @TextureReference(TextureType.SCREEN) public List<GUIComponent> components;

    public GridSettings gridSettings;

    public final transient int W;
    public final transient int H;

    public static class JeiSlotListEntry {
        public JeiSlotListEntry() {}

        public String type;
        public int x;
        public int y;
        public int slotid;
    }


    public JeiRecipeType(ModElement element) {
        super(element);
        this.W = JeiGui.W;
        this.H = JeiGui.H;
        this.gridSettings = new GridSettings();
        this.components = new ArrayList<>();
    }

    public int getIngredientCount() {
        int count = 0;

        for (int i = 0; i < slotList.size(); i++) {
            JeiSlotListEntry entry = slotList.get(i);

            if (entry.type.equals("INPUT")) {
                boolean isUnique = true;
                for (int j = 0; j < i; j++) {
                    JeiSlotListEntry previousEntry = slotList.get(j);
                    if (previousEntry.type.equals("OUTPUT"))
                        continue;
                    if (entry.slotid == previousEntry.slotid) {
                        isUnique = false;
                        break;
                    }
                }

                if (isUnique) {
                    count++;
                }
            }
        }

        return count;
    }

    public int getResultCount() {
        int count = 0;

        for (int i = 0; i < slotList.size(); i++) {
            JeiSlotListEntry entry = slotList.get(i);

            if (entry.type.equals("OUTPUT")) {
                boolean isUnique = true;
                for (int j = 0; j < i; j++) {
                    JeiSlotListEntry previousEntry = slotList.get(j);
                    if (previousEntry.type.equals("INPUT"))
                        continue;
                    if (entry.slotid == previousEntry.slotid) {
                        isUnique = false;
                        break;
                    }
                }

                if (isUnique) {
                    count++;
                }
            }
        }

        return count;
    }

    @Override public List<GUIComponent> getComponents() {
        return components;
    }

}

