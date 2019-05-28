package com.founder.econdaily.modules.historySource.controller;

import com.founder.ark.common.utils.bean.ResponseObject;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.common.entity.PageResult;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.historySource.config.PlatformParamConfig;
import com.founder.econdaily.modules.historySource.service.HistorySourceService;
import com.founder.econdaily.modules.magazine.entity.MagazineParam;
import com.founder.econdaily.modules.newspaper.entity.NewsPaperParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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

    @ApiOperation(value = "电子报文章资源")
    @RequestMapping(value = "/newsPapers/query", method = RequestMethod.POST)
    public ResponseObject queryPaperArticles(@RequestParam(name = "pageNo", required = false) Integer pageNo,
                                             @RequestParam(name = "limit", required = false) Integer limit) throws Exception {
        PageResult result = new PageResult();
        Map<String, Object> queryMap = historySourceService.queryPaperArticles(pageNo, limit);
        result.setPageNo(pageNo);
        result.setPageSize(limit);
        result.setResult((List) queryMap.get("list"));
        result.setTotalCount(Long.valueOf((Integer) queryMap.get("totalCount")));
        return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "期刊文章资源")
    @RequestMapping(value = "/magazines/query", method = RequestMethod.POST)
    public ResponseObject queryMagazineArticles(@RequestParam(name = "pageNo", required = false) Integer pageNo,
                                                @RequestParam(name = "limit", required = false) Integer limit) throws Exception {
        PageResult result = new PageResult();
        Map<String, Object> queryMap = historySourceService.queryMagazineArticles(pageNo, limit);
        result.setPageNo(pageNo);
        result.setPageSize(limit);
        result.setResult((List) queryMap.get("list"));
        result.setTotalCount(Long.valueOf((Integer) queryMap.get("totalCount")));
        return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }

    @ApiOperation(value = "期刊原件下载")
    @GetMapping(value = "/magazine/download/{magCode}/{pdDate}")
    public ResponseObject downloadMagazines(@Valid MagazineParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        long time_1 = System.currentTimeMillis();
        PageResult result = new PageResult();
        //String rootPath = "D:/z-files/magazine/imported/";
        String zipBasePath = this.extractPath(param.getMagCode(), param.getPdDate(), PlatformParamConfig.configsMap.get(PlatformParamConfig.MAGAZINE_ROOT),
                false);

        String path = zipBasePath;
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        this.recursion_1(path, map);
        long time_2 = System.currentTimeMillis();
        logger.info("recursion take time: {}", time_2 - time_1);


        String fileName = "magazine-".concat(param.getMagCode().concat("-").concat(param.getPdDate()).concat(".zip"));
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment;filename=".concat(fileName));
        OutputStream out = response.getOutputStream();

        try {

            String compressFile = zipBasePath.concat("compress-files.zip");
            createZipFile(path.concat("merge/"), new File(compressFile));
            long time_4 = System.currentTimeMillis();
            logger.info("queryFilesAndZip compress-files.zip take time: {}", time_4 - time_2);

            List<String> files = new ArrayList<String>();
            files.add(compressFile);
            this.writeOutputWithChannel(files, out, true);
            long time_5 = System.currentTimeMillis();
            logger.info("writeOutputWithChannel take time: {}", time_5 - time_4);
            logger.info("request total take time: {}", time_5 - time_1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }


    @ApiOperation(value = "电子报原件下载")
    @GetMapping(value = "/newspaper/download/{paperCode}/{plDate}")
    public void downloadPapers(@Valid NewsPaperParam param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        long time_1 = System.currentTimeMillis();
        long time_5 = 0;
        //PageResult result = new PageResult();
        String zipBasePath = this.extractPath(param.getPaperCode(), param.getPlDate(), PlatformParamConfig.configsMap.get(PlatformParamConfig.PAPER_ROOT),
                true);

        String path = zipBasePath;
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        this.recursion_1(path, map);
        long time_2 = System.currentTimeMillis();
        logger.info("recursion take time: {}", time_2 - time_1);

        String fileName = "paper-".concat(param.getPaperCode().concat("-").concat(param.getPlDate()).concat(".zip"));
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-disposition", "attachment;filename=".concat(fileName));
        OutputStream out = response.getOutputStream();

        try {
            long time_3 = System.currentTimeMillis();
            logger.info("queryFilesAndZip list take time: {}", time_3 - time_2);

            String compressFile = zipBasePath.concat("compress-files.zip");
            this.createZipFile(path.concat("merge/"), new File(compressFile));
            long time_4 = System.currentTimeMillis();
            logger.info("queryFilesAndZip compress-files.zip take time: {}", time_4 - time_3);

            List<String> files = new ArrayList<String>();
            files.add(compressFile);
            this.writeOutputWithChannel(files, out, true);
            time_5 = System.currentTimeMillis();
            logger.info("writeOutputWithChannel take time: {}", time_5 - time_4);
            logger.info("request total take time: {}", time_5 - time_1);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //return ResponseObject.newSuccessResponseObject(result, SystemConstant.REQ_SUCCESS);
    }

    public void recursion_1(String root, Map<String, List<String>> map) throws IOException {
        String target_pdf = root.concat("merge/pdf/");
        String target_xml = root.concat("merge/xml/");
        String target_contAtt = root.concat("merge/contAtt/");

        String path = root;
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
            logger.info("this file is not exit!");
        }
    }

}
