package com.wjy.ebook.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wjy.ebook.domain.Ebook;
import com.wjy.ebook.domain.EbookExample;
import com.wjy.ebook.mapper.EbookMapper;
import com.wjy.ebook.req.EbookQueryReq;
import com.wjy.ebook.req.EbookSaveReq;
import com.wjy.ebook.resp.EbookQueryResp;
import com.wjy.ebook.resp.PageResp;
import com.wjy.ebook.utils.CopyUtil;
import com.wjy.ebook.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource//spingboot自带注入
    private EbookMapper ebookMapper;

    @Autowired//jdk自带注入
    private SnowFlake snowFlake;

    /**
     * 查询接口
     * @param req
     * @return
     */

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数:{}", pageInfo.getTotal());
        LOG.info("总页数{}", pageInfo.getPages());

        //列表数据
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    /**
     * 新增保存接口
     */
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else {
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除接口
     */
    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }



}
