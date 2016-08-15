package org.springframework.jotdown.form;

import java.io.Serializable;

/**
 * アイテム更新フォーム
 */
public class EditItemForm implements Serializable {
    private static final long serialVersionUID = 1L;

    /** アイテムID */
    private Long itemId;

    /** モード */
    private String editMode;

    /** タイトル */
    private String title;

    /** メモ */
    private String memo;

    /**
     * @return itemId
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * @param itemId セットする itemId
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * @return editMode
     */
    public String getEditMode() {
        return editMode;
    }

    /**
     * @param editMode セットする editMode
     */
    public void setEditMode(String editMode) {
        this.editMode = editMode;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title セットする title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo セットする memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

}
