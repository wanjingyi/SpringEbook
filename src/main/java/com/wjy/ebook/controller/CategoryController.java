package com.wjy.ebook.controller;

import com.wjy.ebook.domain.Category;
import com.wjy.ebook.req.CategoryQueryReq;
import com.wjy.ebook.req.CategorySaveReq;
import com.wjy.ebook.req.EbookSaveReq;
import com.wjy.ebook.resp.CategoryQueryResp;
import com.wjy.ebook.resp.CommonResp;
import com.wjy.ebook.resp.PageResp;
import com.wjy.ebook.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/all")
    public CommonResp getall(CategoryQueryReq req) {
        CommonResp< List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp getCategories(CategoryQueryReq req) {
        CommonResp< PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> categories = categoryService.getCategories(req);
        resp.setContent(categories);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody @Valid CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
