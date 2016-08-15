package org.springframework.jotdown.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jotdown.auth.CustomUserDetailsService;
import org.springframework.jotdown.constants.ItemEditMode;
import org.springframework.jotdown.dao.dto.UserInfoDto;
import org.springframework.jotdown.form.EditItemForm;
import org.springframework.jotdown.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Controller
@RequestMapping(value = "/item")
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ItemService itemService;

    /**
     * 一覧表示
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/list")
    public String getItemList(Model model) {
        LOGGER.info("START:/item/list");
        UserInfoDto userInfoDto = customUserDetailsService.getAuthUserInfo();
        model.addAttribute("itemList", itemService.getItemList(userInfoDto.getId()));
        return "item/list";
    }

    /**
     * 編集
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/edit")
    public String editItem(Model model, EditItemForm form, RedirectAttributes attributes) {
        LOGGER.info("START:/item/edit");

        List<String> alertMessageList = Lists.newArrayList();
        if (form.getItemId() == null) {
            alertMessageList.add("対象が不正です");
        }
        if (!ItemEditMode.isContain(form.getEditMode())) {
            alertMessageList.add("処理が不正です");
        }
        if (Strings.isNullOrEmpty(form.getTitle())) {
            alertMessageList.add("titleが未入力です");
        }
        
        if (alertMessageList.isEmpty()) {
            UserInfoDto userInfoDto = customUserDetailsService.getAuthUserInfo();
            itemService.editItem(userInfoDto.getId(), form);
        } else {
            model.addAttribute("alertMessageList", alertMessageList);
        }

        return "redirect:/item/list";
    }

    /**
     * 削除
     *
     * @return 遷移先テンプレート
     */
    @RequestMapping(value = "/delete")
    public String deleteItem(Model model, EditItemForm form, RedirectAttributes attributes) {
        LOGGER.info("START:/item/delete");

        List<String> alertMessageList = Lists.newArrayList();
        if (form.getItemId() == null) {
            alertMessageList.add("対象が不正です");
        }
        
        if (alertMessageList.isEmpty()) {
            itemService.deleteItem(form.getItemId());
        } else {
            model.addAttribute("alertMessageList", alertMessageList);
        }

        return "redirect:/item/list";
    }

}
