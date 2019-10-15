package com.mdx.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mdx.entity.Item;
import com.mdx.enums.ExceptionInfoEnum;
import com.mdx.exception.SsmException;
import com.mdx.mapper.ItemMapper;
import com.mdx.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;


    @Override
    public PageInfo<Item> findItem(Integer page, Integer size, String name) {
        PageHelper.startPage(page, size);
        List<Item> itemList = itemMapper.findByName(name);
        PageInfo<Item> pageInfo = new PageInfo<>(itemList);
        return pageInfo;
    }

    @Override
    @Transactional
    public void addItem(Item item) {
        int count = itemMapper.insertSelective(item);
        if (count != 1) {
            log.info("【添加商品】  添加商品失败  item={}", item);
            throw new SsmException(ExceptionInfoEnum.ITEM_ADD_ERROR);
        }
    }

    @Override
    public void deleteItemById(Integer id) {
        Item item = new Item();
        item.setId(id);
        int count = itemMapper.delete(item);
        if (count != 1) {
            log.info("【删除商品】  删除商品失败  id={}", id);
            throw new SsmException(ExceptionInfoEnum.ITEM_DELETE_ERROR);
        }
    }

    @Override
    public Item getItemById(Integer id) {
        Item item = new Item();
        item.setId(id);
        Item item1 = itemMapper.selectOne(item);
        if (item1 == null) {
            log.info("【查找商品】  查找单个商品信息失败  id={}", id);
            throw new SsmException(ExceptionInfoEnum.CHECK_BY_ID_ERROR);
        }
        return item1;
    }

    @Override
    public void update(Item item) {

        int count = itemMapper.updateByPrimaryKeySelective(item);
        if (count != 1) {
            log.info("【修改商品】  修改商品失败  item={}", item);
            throw new SsmException(ExceptionInfoEnum.UPDATE_ERROR);
        }
    }

}
