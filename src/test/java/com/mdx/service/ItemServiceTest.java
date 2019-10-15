package com.mdx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mdx.SpringTests;
import com.mdx.entity.Item;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ItemServiceTest extends SpringTests {

    @Autowired
    private ItemService itemService;
    @Test
    public void findItem() {


    }
}