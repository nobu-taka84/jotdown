package org.springframework.jotdown.constants;

/**
 * ItemEditMode
 */
public enum ItemEditMode {

    /** 追加 */
    ADD("add"),

    /** 編集 */
    EDIT("edit");

    /** mode */
    private final String mode;

    private ItemEditMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return this.mode;
    }

    public static boolean isContain(String mode) {
        ItemEditMode[] enumArray = ItemEditMode.values();
        for (ItemEditMode enumElement : enumArray) {
            if (enumElement.mode.equals(mode)) {
                return true;
            }
        }
        return false;
    }

}
