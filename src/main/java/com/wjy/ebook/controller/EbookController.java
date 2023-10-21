package com.wjy.ebook.controller;

import com.wjy.ebook.domain.Ebook;
import com.wjy.ebook.req.EbookReq;
import com.wjy.ebook.resp.CommonResp;
import com.wjy.ebook.resp.EbookResp;
import com.wjy.ebook.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")
    public CommonResp list(EbookReq req) {
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        List<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
