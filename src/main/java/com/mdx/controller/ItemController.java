package com.mdx.controller;

import com.github.pagehelper.PageInfo;
import com.mdx.entity.Item;

import static com.mdx.enums.ExceptionInfoEnum.*;

import com.mdx.exception.SsmException;
import com.mdx.service.ItemService;
import com.mdx.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.genid.GenId;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/item")
@Slf4j
public class ItemController {
    @Autowired
    private ItemService itemService;


    @Value("${pic.maxSize}")
    private Long picMaxSize;

    @Value("${pic.types}")
    private String types;


    @RequestMapping("/list")
    public String limitedList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "5") Integer size,
                              String name,
                              HttpSession session) {
        PageInfo<Item> pageInfo = itemService.findItem(page, size, name);
        if (pageInfo.getList().size() == 0) {
            log.info("【查询】 未查询出结果！ name={}", name);
            throw new SsmException(CHECK_BY_NAME_ERROR);
        }
        session.setAttribute("name", name);
        session.setAttribute("pageInfo", pageInfo);
        return "/item/item_list";
    }


    @RequestMapping("/add-ui")
    public String addUI() {
        return "item/item_add";
    }


    @PostMapping("/add")
    @ResponseBody
    public ResultVo add(MultipartFile picFile,
                        @Valid Item item,
                        BindingResult bindingResult,
                        HttpServletRequest request) throws IOException {
        if (picFile == null || picFile.getSize() == 0) {
            log.info("【添加】 文件添加失败 未获取到头像文件！");
            throw new SsmException(ITEM_ADD_ERROR.getCode(), "未获取到头像文件！");
        }
//        4. 接收普通表单项Item item,并校验.
        if (bindingResult.hasErrors()) {
            log.info("【添加】 文件添加失败 未获取到商品信息！");
            throw new SsmException(ITEM_ADD_ERROR.getCode(), "未获取到商品信息！");
        }
//        5. 文件上传项.
//            5.1 大小判断.
        if (picFile.getSize() > picMaxSize) {
            log.info("【添加】 图片大小过大！ size={}", picFile.getSize());
            throw new SsmException(ITEM_ADD_OVERSIZE);
        }
//            5.2 类型判断.
        List<String> typeList = Arrays.asList(types.split(","));
        String filename = picFile.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        if (!typeList.contains(fileType)) {
            log.info("【添加】 图片类型错误！！ type={}", fileType);
            throw new SsmException(ITEM_ADD_TYPE_ERROR);
        }
//            5.3 是否损坏.

        BufferedImage read = ImageIO.read(picFile.getInputStream());
        if (read == null) {
            log.info("【添加】 图片已损坏！！ ");
            throw new SsmException(ITEM_ADD_DAMAGED_ERROR);
        }
//            5.4 新名字.
        String codes = UUID.randomUUID().toString();
        String newName = codes + "_" + filename;
        String path = request.getServletContext().getRealPath("/") + "static/images/" + newName;
//            5.5 保存到本地.
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        IOUtils.copy(picFile.getInputStream(), new FileOutputStream(file));
//            5.6 将图片的访问路径设置到item对象中.
        String pic = request.getContextPath() + "/static/images/" + newName;
//        6. 校验普通表单项.
        item.setPic(pic);
//        7. 调用serivce保存.
        itemService.addItem(item);
//        8. 响应数据.
        return new ResultVo(0, "成功", null);
    }

    @DeleteMapping("/del/{id}")
    @ResponseBody
    public ResultVo delete(@PathVariable Integer id) {
        itemService.deleteItemById(id);
        return new ResultVo(0, "成功", null);
    }

    @GetMapping("/update-ui")
    public String updateUI(Integer id, HttpSession session) {
        Item item = itemService.getItemById(id);
        session.setAttribute("item", item);
        return "/item/item_update";
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultVo update(MultipartFile picFile,
                           @Valid Item item,
                           BindingResult bindingResult,
                           HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            log.info("【修改】 文件添加失败 未获取到商品信息！");
            throw new SsmException(ITEM_ADD_ERROR.getCode(), "未获取到商品信息！");
        }
        if (picFile != null && picFile.getSize() > 0) {
            if (picFile.getSize() > picMaxSize) {
                log.info("【修改】 图片大小过大！ size={}", picFile.getSize());
                throw new SsmException(ITEM_ADD_OVERSIZE);
            }
//            5.2 类型判断.
            List<String> typeList = Arrays.asList(types.split(","));
            String filename = picFile.getOriginalFilename();
            String fileType = filename.substring(filename.lastIndexOf(".") + 1);
            if (!typeList.contains(fileType)) {
                log.info("【修改】 图片类型错误！！ type={}", fileType);
                throw new SsmException(ITEM_ADD_TYPE_ERROR);
            }
//            5.3 是否损坏.

            BufferedImage read = ImageIO.read(picFile.getInputStream());
            if (read == null) {
                log.info("【修改】 图片已损坏！！ ");
                throw new SsmException(ITEM_ADD_DAMAGED_ERROR);
            }
//            5.4 新名字.
            String codes = UUID.randomUUID().toString();
            String newName = codes + "_" + filename;
            String path = request.getServletContext().getRealPath("/") + "static/images/" + newName;
//            5.5 保存到本地.
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            IOUtils.copy(picFile.getInputStream(), new FileOutputStream(file));
//            5.6 将图片的访问路径设置到item对象中.
            String pic = request.getContextPath() + "/static/images/" + newName;
//        6. 校验普通表单项.
            item.setPic(pic);
        }else {
            item.setPic(null);
        }
        itemService.update(item);
        return new ResultVo(0, "成功", null);
    }

}
