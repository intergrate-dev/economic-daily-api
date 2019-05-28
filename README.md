# economic-daily-api
引入jwt，引用链接：  
[前后端分离微服务认证之JWT](https://blog.52itstyle.com/archives/2157/)

![输入图片说明](https://gitee.com/uploads/images/2018/0316/184958_d43bfa15_87650.png "488490-20170117211606396-1300480328.png")


接口postman发布地址
https://documenteY.getpostman.com/view/7509577/S1LyTnJN
https://lively-satellite-1966.postman.co/collections/7509577-8b9f4444-9840-4381-828c-27bd9d21a0d8?workspace=d28b4872-7589-4b27-8a07-4c092352253c


### JWT
reference link: http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html

JWT 的几个特点
（1）JWT 默认是不加密，但也是可以加密的。生成原始 Token 以后，可以用密钥再加密一次。

（2）JWT 不加密的情况下，不能将秘密数据写入 JWT。

（3）JWT 不仅可以用于认证，也可以用于交换信息。有效使用 JWT，可以降低服务器查询数据库的次数。

（4）JWT 的最大缺点是，由于服务器不保存 session 状态，因此无法在使用过程中废止某个 token，或者更改 token 的权限。也就是说，一旦 JWT 签发了，在到期之前就会始终有效，除非服务器部署额外的逻辑。

（5）JWT 本身包含了认证信息，一旦泄露，任何人都可以获得该令牌的所有权限。为了减少盗用，JWT 的有效期应该设置得比较短。对于一些比较重要的权限，使用时应该再次对用户进行认证。

（6）为了减少盗用，JWT 不应该使用 HTTP 协议明码传输，要使用 HTTPS 协议传输。


### springboot https
springboot https 后台配置与调用
https://blog.51cto.com/7308310/2333550
https://blog.csdn.net/shouldnotappearcalm/article/details/78047047



### 实体与数据库表
https://www.cnblogs.com/beibidewomen/p/9729686.html
https://blog.csdn.net/weixin_39568559/article/details/79609916

### spring data jpa反向生成实体
https://www.cnblogs.com/bodhitree/p/9469052.html
https://www.jianshu.com/p/2aa3d2dd83bd
https://segmentfault.com/a/1190000008632485
http://www.cnblogs.com/dreamroute/p/5173896.html


### spring data jpa反向生成
https://blog.csdn.net/qq_27474851/article/details/86660993
https://www.cnblogs.com/cl-rr/p/10397107.html
/generaCode


### spring boot jdbc
https://blog.csdn.net/forezp/article/details/70477821
https://segmentfault.com/a/1190000009732344
https://www.jianshu.com/p/3609c9a3f3be


### spring boot全局异常处理
https://www.cnblogs.com/java-zhao/p/5769018.html


### swagger
springboot 生成html接口文档
https://blog.csdn.net/qq_29534483/article/details/81227308

#### 快速编写接口api规范文档工具(Markdown)
https://blog.51cto.com/wuxueqing/1972073
http://www.itdaan.com/blog/2018/04/10/ebb2ca2413729c655ab9c2959f4d2633.html
http://www.itdaan.com/blog/2018/04/03/954ba7e0f97a840ddd99efcc730b05c.html

#### api restfull sample
http://os.opensns.cn/book/index/read/id/5.html
 

### 生产环境禁用swagger
https://www.jianshu.com/p/fa3230ffb27c


### swagger header
https://blog.csdn.net/qq_33759042/article/details/80101941

### swagger header参数  
https://www.jianshu.com/p/6e5ee9dd5a61  
[swagger忽略某个header] https://www.oschina.net/question/262436_2301706


### jwt
https://gitee.com/52itstyle/economic-daily-api
https://www.cnblogs.com/wangrudong003/p/10122706.html


### swagger 注解
@ApiImplicitParam(name = "openid", value = "openid", required = true, dataType = "String",paramType="header")
https://www.wang1314.com/doc/topic-20733576-1.html
https://my.oschina.net/dlam/blog/808315

### mysql 视图
https://www.cnblogs.com/zhangdk/p/5907434.html


### extend  ???
monotoring: log, performance act on


@Valid 校验参数
http://www.cnblogs.com/winner-0715/p/10145594.html

Springboot + AOP 实现参数统一非空校验
https://www.jianshu.com/p/c13a530d97f7


## 正则
https://www.jianshu.com/p/583998f435d0
https://www.cnblogs.com/lr393993507/p/5234857.html
github

### Mysql
MySql数据库开启binlog
Mysql表 UPDATE_TIME字段
量级数据处理方案
https://blog.csdn.net/chivydrs/article/details/81670475
http://www.onexsoft.com/zh/oneproxy-mysql-parallel-query.html
https://cloud.tencent.com/developer/article/1423703
https://cloud.tencent.com/developer/article/1060217
https://cloud.tencent.com/developer/information/mysql%20%E5%B9%B6%E8%A1%8C%E6%9F%A5%E8%AF%A2

### github
https://github.com/zaiyunduan123/springboot-seckill
https://github.com/search?q=springboot+redis
https://blog.csdn.net/plei_yue/article/details/79362372
http://www.cnblogs.com/leeSmall/p/8728231.html



### ElasticSearch
mysql >> ES
https://elasticsearch.cn/article/756

### Multify Thread
https://segmentfault.com/q/1010000008179970?_ea=1578732
https://blog.csdn.net/u011163372/article/details/73995897
https://blog.csdn.net/AHAU10/article/details/52550430
java多线程压缩
https://blog.csdn.net/lyx2007825/article/details/7618151


高效压缩
https://blog.csdn.net/AHAU10/article/details/52550430
```
 ZipArchiveEntry archiveEntry = new ZipArchiveEntry(entryName);
archiveEntry.setMethod(ZipMethod.STORED.getCode());
```
注：开发环境，资源文件、压缩文件都在本机，性能得到很大提升。线上环境，资源文件是挂载网络位置，
    接口还是很慢（考虑将merge文件生成到服务机器磁盘下，但资源文件数据庞大）。  
之前接口代码如下：
```
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
    this.recursion(path, map);
    long time_2 = System.currentTimeMillis();
    logger.info("recursion take time: {}", time_2 - time_1);


    String fileName = "magazine-".concat(param.getMagCode().concat("-").concat(param.getPdDate()).concat(".zip"));
    response.setContentType("text/html; charset=UTF-8");
    response.setContentType("application/x-msdownload;");
    response.setHeader("Content-disposition", "attachment;filename=".concat(fileName));
    OutputStream out = response.getOutputStream();

    //String mergePath = zipBasePath += "merge/";
    try {
        String pdfZip = zipBasePath.concat("pdf.zip");
        this.queryFilesAndZip(map.get("pdfList"), pdfZip);
        String xmlZip = zipBasePath.concat("xmlZip.zip");
        this.queryFilesAndZip(map.get("xmlList"), xmlZip);
        String contentAttach = zipBasePath.concat("contentAttach.zip");
        this.queryFilesAndZip(map.get("contAttList"), contentAttach);
        long time_3 = System.currentTimeMillis();
        logger.info("queryFilesAndZip list take time: {}", time_3 - time_2);

        List<String> files = new ArrayList<String>();
        files.add(pdfZip);
        files.add(xmlZip);
        files.add(contentAttach);
        String compressFile = zipBasePath.concat("compress-files.zip");
        this.queryFilesAndZip(files, compressFile);
        long time_4 = System.currentTimeMillis();
        logger.info("queryFilesAndZip compress-files.zip take time: {}", time_4 - time_3);

        files = new ArrayList<String>();
        //files.add(mergePath);
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
```    

## issues
@PathVariable
(defaultValue)参数值被持久化了，地址变化了，参数值不变

```
@ApiOperation(value = "获取某一期报纸某一版面信息")
@GetMapping("/{paperCode}/{plDate}/{layout}")
public ResponseObject queryByCodeAndPlDateAndLayout(@PathVariable("paperCode") @RequestParam(name = "paperCode", required = true, defaultValue = "jjrb") String paperCode,
     @PathVariable("plDate") @RequestParam(name = "plDate", required = true, defaultValue = "2019-04-30") String plDate,
     @PathVariable("layout") @RequestParam(name = "layout", required = true, defaultValue = "10") Integer layout) throws Exception{
     

修改为     
public ResponseObject queryByCodeAndPlDateAndLayout(@PathVariable("paperCode") String paperCode, @PathVariable("plDate") String plDate,
                                                    @PathVariable("layout") Integer layout) throws Exception{
                                                    
```                                              
                                                    
refer:  
[@RequestParam @RequestBody @PathVariable的作用](https://segmentfault.com/a/1190000016001510?utm_source=tag-newest)                                                    
           
           
           
                                                     
