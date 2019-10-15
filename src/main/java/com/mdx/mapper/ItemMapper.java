package com.mdx.mapper;

import com.mdx.entity.Item;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ItemMapper extends Mapper<Item> {

    //根据商品名称查询
    List<Item> findByName(@Param("name") String name);



}
