package org.springframework.jotdown.service;

import java.util.List;

import org.springframework.jotdown.dao.dto.ItemDto;
import org.springframework.jotdown.form.EditItemForm;

public interface ItemService {
    
    List<ItemDto> getItemList(Long userInfoId);
    
    void editItem(Long userInfoId, EditItemForm form);
    
    void deleteItem(Long itemId);

}
