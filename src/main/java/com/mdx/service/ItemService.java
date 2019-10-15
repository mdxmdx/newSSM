package com.mdx.service;

import com.github.pagehelper.PageInfo;
import com.mdx.entity.Item;

import java.util.List;

public interface ItemService {

    //根据条件查询商品
    PageInfo<Item> findItem(Integer page, Integer size, String name);


    //添加商品
    void addItem(Item item);


//    删除商品
    void deleteItemById(Integer id);

    //获得单个商品信息
    Item getItemById(Integer id);

    //修改商品
    void update(Item item);
}
