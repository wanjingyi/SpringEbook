package com.wjy.ebook.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wjy.ebook.domain.Category;
import com.wjy.ebook.domain.CategoryExample;
import com.wjy.ebook.domain.Ebook;
import com.wjy.ebook.mapper.CategoryMapper;
import com.wjy.ebook.req.CategoryQueryReq;
import com.wjy.ebook.req.CategorySaveReq;
import com.wjy.ebook.req.EbookSaveReq;
import com.wjy.ebook.resp.CategoryQueryResp;
import com.wjy.ebook.resp.PageResp;
import com.wjy.ebook.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.wjy.ebook.utils.SnowFlake;
import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        // 列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;
    }

    public PageResp<CategoryQueryResp> getCategories(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categories = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categories);
        LOG.info("总行数:{}", pageInfo.getTotal());
        LOG.info("总页数{}", pageInfo.getPages());

        List<CategoryQueryResp> categoryQueryResps = CopyUtil.copyList(categories, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(categoryQueryResps);
        return pageResp;
    }


    /**
     * 新增保存接口
     */
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else {
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除接口
     */
    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
