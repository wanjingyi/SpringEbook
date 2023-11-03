package com.wjy.ebook.controller;

import com.wjy.ebook.req.EbookQueryReq;
import com.wjy.ebook.req.EbookSaveReq;
import com.wjy.ebook.resp.CommonResp;
import com.wjy.ebook.resp.EbookQueryResp;
import com.wjy.ebook.resp.PageResp;
import com.wjy.ebook.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
}
