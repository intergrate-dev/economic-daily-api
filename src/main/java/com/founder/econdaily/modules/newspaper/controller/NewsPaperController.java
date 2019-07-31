package com.founder.econdaily.modules.newspaper.controller;

import com.founder.econdaily.common.annotation.Validate;
import com.founder.econdaily.common.util.ResponseObject;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.modules.newspaper.entity.NewsPaperParam;
import com.founder.econdaily.modules.newspaper.service.NewsPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(tags = "电子报接口")
@RestController
@RequestMapping(value = "/newsPaper")
public class NewsPaperController extends BaseController {

    @Autowired
    private NewsPaperService newsPaperService;

    @ApiOperation(value = "1. 获取所有报纸最新版信息")
    @RequestMapping(value = "/newtests",method = RequestMethod.POST)
    @Validate
    //@ResponseBody
    public ResponseObject newtests() throws Exception{
        Map<String, Object> resMap = newsPaperService.findNewtests();
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "2. 获取某一报纸信息")
    @GetMapping(value = "/{attachId}/paperDates")
    @Validate
    //@ResponseBody
    //public ResponseObject queryPaperDates(@PathVariable("attachId") String attachId) throws Exception {
    public ResponseObject queryPaperDates(@Valid NewsPaperParam param, BindingResult validResult) throws Exception {
        Map<String, Object> resMap = newsPaperService.findPaperDatesByAttachId(param.getAttachId());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "3. 获取某一期版面信息")
    @GetMapping("/{paperCode}/{plDate}")
    @Validate
    /*@ResponseBody*/
    //public ResponseObject queryByCodeAndPlDate(@PathVariable("paperCode") String paperCode, @PathVariable("plDate") String plDate) throws Exception{
    public ResponseObject queryByCodeAndPlDate(@Valid NewsPaperParam param, BindingResult validResult) throws Exception {
        Map<String, Object> resMap = newsPaperService.queryTopLayoutByPaperCodeAndPlDate(param.getPaperCode(), param.getPlDate());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "4. 获取某一期报纸某一版面信息")
    @GetMapping("/{paperCode}/{plDate}/{layout}")
    @Validate
    /*@ResponseBody*/
    /*public ResponseObject queryByCodeAndPlDateAndLayout(@PathVariable("paperCode") String paperCode, @PathVariable("plDate") String plDate,
                                                        @PathVariable("layout") Integer layout) throws Exception{*/
    public ResponseObject queryByCodeAndPlDateAndLayout(@Valid NewsPaperParam param, BindingResult validResult) throws Exception {
        Map<String, Object> resMap = newsPaperService.queryByPaperIdAndPlDate(param.getPlDate(), param.getPaperCode(), param.getLayout());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "5. 获取某一文章信息")
    @GetMapping("/queryArticle/{articleId}")
    @Validate
    /*@ResponseBody*/
    //public ResponseObject queryByArticleId(@PathVariable("articleId") String articleId) throws Exception{
    public ResponseObject queryByArticleId(@Valid NewsPaperParam param, BindingResult validResult) throws Exception {
        Map<String, Object> resMap = newsPaperService.queryByArticleId(param.getArticleId());
        return ResponseObject.newSuccessResponseObject(resMap, SystemConstant.REQ_SUCCESS);
    }

}
