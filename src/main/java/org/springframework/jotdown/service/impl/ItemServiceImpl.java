package org.springframework.jotdown.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jotdown.constants.ErrorCode;
import org.springframework.jotdown.constants.ItemEditMode;
import org.springframework.jotdown.dao.dto.ItemDto;
import org.springframework.jotdown.dao.entity.Item;
import org.springframework.jotdown.dao.repository.ItemRepository;
import org.springframework.jotdown.exception.SystemException;
import org.springframework.jotdown.form.EditItemForm;
import org.springframework.jotdown.service.ItemService;
import org.springframework.jotdown.util.ConvertUtils;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItemList(Long userInfoId) {
        List<Item> list = itemRepository.findByUserInfoIdOrderBySortorder(userInfoId);
        return (list == null) ? null : ConvertUtils.copyProperties(list, ItemDto.class);
    }

    @Override
    public void editItem(Long userInfoId, EditItemForm form) {
        Item item = new Item();
        if (ItemEditMode.ADD.getMode().equals(form.getEditMode())) {
            Integer count = itemRepository.selectMaxSortorder(userInfoId);
            item.setUserInfoId(userInfoId);
            item.setSortorder((count == null) ? 1 : count + 1);
        } else {
            item = itemRepository.findOne(form.getItemId());
            if (!item.getUserInfoId().equals(userInfoId)) {
                throw new SystemException(//
                        ErrorCode.CONSISTENCYERROR_GENERAL.name(), "データ不整合が発生しました。");
            }
        }
        item.setTitle(form.getTitle());
        item.setMemo(form.getMemo());
        itemRepository.save(item);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.delete(itemId);
    }

}
