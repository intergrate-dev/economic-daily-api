package com.founder.econdaily.modules.magazine.controller;

import com.founder.econdaily.common.annotation.Validate;
import com.founder.econdaily.common.util.ResponseObject;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.common.entity.PageResult;
import com.founder.econdaily.modules.magazine.entity.MagazineParam;
import com.founder.econdaily.modules.magazine.service.MagazineService;
import com.founder.econdaily.modules.newspaper.entity.NewsPaperParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "期刊接口")
@RestController
@RequestMapping(value = "/magazine")
public class MagazineController extends BaseController {

    @Autowired
    private MagazineService magazineService;

    @ApiOperation(value = "1. 获取所有种类期刊最新一期信息")
    @RequestMapping(value = "/newtests", method = RequestMethod.POST)
    @Validate
    //@ResponseBody
    public ResponseObject magazineNewtests() throws Exception {
        Map<String, Object> resMap = magazineService.queryMagNewtests();
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "2. 获取某一期刊及往期期刊信息")
    @GetMapping("/{magCode}/{pdDate}")
    @Validate
    /*@ResponseBody*/
    /*public ResponseObject queryMagazineDetail(@PathVariable("magCode") @RequestParam(name = "magCode", required = true, defaultValue = "zgjjxx") String magCode,
                                              @PathVariable("pdDate") @RequestParam(name = "pdDate", required = true, defaultValue = "2015-12-16") String pdDate) throws Exception {*/
    //public ResponseObject queryMagazineDetail(@PathVariable("magCode") String magCode, @PathVariable("pdDate") String pdDate) throws Exception {
    public ResponseObject queryMagazineDetail(@Valid MagazineParam param, BindingResult validResult) throws Exception {
        /*if (validResult.hasErrors()) {
            return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, validErrorMsg(validResult));
        }*/
        Map<String, Object> resMap = magazineService.queryByPdDateAndMagCode(param.getPdDate(), param.getMagCode());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "3. 获取某一期刊、往期期刊信息及稿件信息")
    @GetMapping("/{articleId}")
    @Validate
    /*@ResponseBody*/
    //public ResponseObject queryMagazineDetailAndArticle(@PathVariable("articleId") String articleId) throws Exception {
    public ResponseObject queryMagazineDetailAndArticle(@Valid MagazineParam param, BindingResult validResult) throws Exception {
        Map<String, Object> resMap = magazineService.queryMagazineDetailAndArticle(param.getArticleId());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "4. 获取往期杂志页面的具体数据")
    @GetMapping("/{magCode}/detail")
    @Validate
    /*@ResponseBody*/
    //public ResponseObject queryMagazineInfo(@PathVariable("magCode") String magCode) throws Exception {
    public ResponseObject queryMagazineInfo(@Valid MagazineParam param, BindingResult validResult) throws Exception {
        Map<String, Object> resMap = magazineService.queryMagazineInfoByMagCode(param.getMagCode());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "5. 根据选择的年限查询期刊内容")
    @GetMapping("/select/{magCode}/{year}")
    @Validate
    /*@ResponseBody*/
    //public ResponseObject queryMagazines(@PathVariable("magCode") String magCode, @PathVariable("pdDate") String pdDate) throws Exception {
    public ResponseObject queryMagazines(@Valid MagazineParam param, BindingResult validResult) throws Exception {
        //Map<String, Object> resMap = magazineService.queryMagazineInfoByMagCodeAndYear(param.getMagCode(), param.getPdDate());
        Map<String, Object> resMap = magazineService.queryMagazineInfoByMagCodeAndYear(param.getMagCode(), param.getYear());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "6. 根据条件查询期刊记录")
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    //@ResponseBody
    public ResponseObject queryByParams(@RequestParam(name = "magName", required = false) String magName,
                                        @RequestParam(name = "pdDate", required = false) String pdDate,
                                        @RequestParam(name = "pageNo", required = false) Integer pageNo,
                                        @RequestParam(name = "limit", required = false) Integer limit) throws Exception {
        PageResult result = new PageResult();
        Map<String, Object> queryMap = magazineService.queryByParams(magName, pdDate, pageNo, limit);
        result.setPageNo(pageNo);
        result.setPageSize(limit);
        result.setResult((List) queryMap.get("list"));
        result.setTotalCount(Long.valueOf((Integer) queryMap.get("totalCount")));
        return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }
}
