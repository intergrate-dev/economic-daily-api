package com.founder.econdaily.modules.historySource.controller;

import com.founder.econdaily.common.util.CustomException;
import com.founder.econdaily.common.util.ResponseObject;
import com.founder.econdaily.common.annotation.Validate;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.common.entity.PageResult;
import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.historySource.config.PlatformParamConfig;
import com.founder.econdaily.modules.historySource.service.HistorySourceService;
import com.founder.econdaily.modules.magazine.entity.BaseParam;
import com.founder.econdaily.modules.magazine.entity.CommonParam;
import com.founder.econdaily.modules.magazine.entity.MagazineParamDL;
import com.founder.econdaily.modules.magazine.entity.NewsPaperParamDL;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "历史资源接口")
@RestController
@RequestMapping(value = "/historySource")
public class HistorySourceController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(HistorySourceController.class);
    @Autowired
    private HistorySourceService historySourceService;

    @ApiOperation(value = "1. 电子报文章资源")
    @RequestMapping(value = "/newsPapers/query", method = RequestMethod.POST)
    @Validate
    public ResponseObject queryPaperArticles(CommonParam param) throws Exception {
        PageResult result = new PageResult();
        Map<String, Object> queryMap = historySourceService.queryPaperArticles(param.getPageNo(), param.getLimit(),
                param.getBeginTime(), param.getEndTime());
        result.setPageNo(param.getPageNo());
        result.setPageSize(param.getLimit());
        result.setResult((List) queryMap.get("list"));
        result.setTotalCount(Long.valueOf((Integer) queryMap.get("totalCount")));
        return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "2. 期刊文章资源")
    @RequestMapping(value = "/magazines/query", method = RequestMethod.POST)
    @Validate
    public ResponseObject queryMagazineArticles(CommonParam param) throws Exception {
        PageResult result = new PageResult();
        Map<String, Object> queryMap = historySourceService.queryMagazineArticles(param.getPageNo(), param.getLimit(),
                param.getBeginTime(), param.getEndTime());
        result.setPageNo(param.getPageNo());
        result.setPageSize(param.getLimit());
        result.setResult((List) queryMap.get("list"));
        result.setTotalCount(Long.valueOf((Integer) queryMap.get("totalCount")));
        return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "3. 电子报原件下载")
    @GetMapping(value = "/newspaper/download/{paperCode}/{beginTime}/{endTime}")
    @Validate
    public ResponseObject downloadPapers(NewsPaperParamDL param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> files = new ArrayList<String>();
        String rootPath = PlatformParamConfig.configsMap.get(PlatformParamConfig.PAPER_ROOT);
        String filePrefix = "newspaper-";
        List<String> dates = this.getDateRange(param, null);
        for (String date : dates) {
            try {
                this.extractCompFileByDate(param.getPaperCode(), date, rootPath, true, files, response);
            } catch (CustomException e) {
                 logger.error("================================= CustomException errorMsg: {} ============================", e.getErrorMessage());
                continue;
            }
        }
        if (files.size() == 0) {
            return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, "当前日期范围内为检索到资源文件！");
        }
        String serverPath = request.getServletContext().getRealPath("/");
        this.writeOutputWithChannel(files, serverPath, response, true, filePrefix.concat(param.getPaperCode()
                .concat(RegxUtil.STRIP_SPLIT).concat("data").concat(".zip")));
        return ResponseObject.newSuccessResponseObject(null, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "4. 期刊原件下载")
    @GetMapping(value = "/magazine/download/{magCode}/{beginTime}/{endTime}")
    @Validate
    public ResponseObject downloadMagazines(MagazineParamDL param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<String> files = new ArrayList<String>();
        String rootPath = PlatformParamConfig.configsMap.get(PlatformParamConfig.MAGAZINE_ROOT);
        String filePrefix = "magazine-";
        List<String> dates = this.getDateRange(param, null);
        for (String date : dates) {
            try {
                this.extractCompFileByDate(param.getMagCode(), date, rootPath, false, files, response);
            } catch (CustomException e) {
                logger.error("================================= CustomException errorMsg: {} ============================", e.getErrorMessage());
                continue;
            }
        }
        if (files.size() == 0) {
            return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, "当前日期范围内为检索到资源文件！");
        }
        String serverPath = request.getServletContext().getRealPath("/");
        this.writeOutputWithChannel(files, serverPath, response, true, filePrefix.concat(param.getMagCode()
                .concat(RegxUtil.STRIP_SPLIT).concat("data").concat(".zip")));
        return ResponseObject.newSuccessResponseObject(null, SystemConstant.REQ_SUCCESS);
    }

    private List<String> getDateRange(BaseParam param, String earlyDate) throws ParseException {
        List<String> dates = new ArrayList<>();
        Date startDate = DateParseUtil.stringToDate(earlyDate);
        Date endDate = new Date();
        if (!StringUtils.isEmpty(param.getBeginTime())) {
            startDate = DateParseUtil.stringToDate(param.getBeginTime());
        }
        if (!StringUtils.isEmpty(param.getEndTime())) {
            endDate = DateParseUtil.stringToDate(param.getEndTime());
        }
        if (!StringUtils.isEmpty(param.getBeginTime()) || !StringUtils.isEmpty(param.getEndTime()) ||
                param.getBeginTime().equals(param.getEndTime())){
            dates.add(param.getBeginTime());
            return dates;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (calendar.getTime().before(endDate)) {
            dates.add(DateParseUtil.dateToString(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    private void extractCompFileByDate(String code, String date, String rootPath, Boolean isDelete, List<String> files, HttpServletResponse response) throws Exception, CustomException {
        long time_1 = System.currentTimeMillis();
        String zipBasePath = this.extractPath(code, date, rootPath, isDelete);


        String path = zipBasePath;
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        this.searchAndCopyFiles(path, date, map);

        long time_2 = System.currentTimeMillis();
        logger.info("recursion take time: {}", time_2 - time_1);
        String compressFile = zipBasePath.concat("compress-files").concat("_").concat(date).concat(".zip");
        this.createZipFile(path.concat("/merger/"), date, new File(compressFile));

        long time_4 = System.currentTimeMillis();
        logger.info("queryFilesAndZip compress-files: {}.zip take time: {}", compressFile, time_4 - time_2);
        files.add(compressFile);
    }

    private void searchAndCopyFiles(String root, String date, Map<String, List<String>> map) throws IOException, CustomException {
        String target_pdf = root.concat("/merger/").concat("/pdf/");
        String target_xml = root.concat("/merger/").concat("/xml/");
        String target_contAtt = root.concat("/merger/").concat("/contAtt/");
        File file = new File(root);
        if (file.exists()) {
            File[] subFile = file.listFiles();
            for (int i = 0; i < subFile.length; i++) {
                if (subFile[i].isDirectory()) {
                    recursion(subFile[i].getAbsolutePath(), map);
                } else {
                    String filename = subFile[i].getName();
                    if (filename.endsWith(RegxUtil.FILE_PDF)) {
                        FileUtils.copyFile(subFile[i], new File(target_pdf.concat(filename)));
                    }
                    if (filename.endsWith(RegxUtil.FILE_XML)) {
                        FileUtils.copyFile(subFile[i], new File(target_xml.concat(filename)));
                    }
                    if (filename.endsWith(RegxUtil.FILE_JPG) || filename.endsWith(RegxUtil.FILE_DOC)) {
                        FileUtils.copyFile(subFile[i], new File(target_contAtt.concat(filename)));
                    }

                }
            }
        } else {
            logger.info("============================== this file: {} is not exist! ================", root);
            throw new CustomException(-1, "file path: ".concat(root).concat(" is not exist"));
        }
    }

}
