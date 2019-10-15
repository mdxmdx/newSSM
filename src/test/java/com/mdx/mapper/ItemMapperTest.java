package com.mdx.mapper;

import com.mdx.SpringTests;
import com.mdx.entity.Item;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ItemMapperTest extends SpringTests {

    @Autowired
    private ItemMapper itemMapper;

    @Test
    public void findByName() {
        List<Item> list = itemMapper.findByName("硕");
        for (Item item : list) {
            System.out.println(item);
        }
    }
}