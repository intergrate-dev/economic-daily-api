### run environment
#### database configure
${tomcat_home}/conf/Catalina/localhost/Root.xml


#### login count
http://localhost:8080/e5workspce/Login.jsp
dengcc  code >>>> 
test5	3DES{479C3EC81F515F95F35D6436CD33A72A}====


http://localhost:8080/e5sys/SysLogin.jsp?enabled=1
sysAdmin
空


### sql
doclibid		|doclibname			|doclibtable            |doctypeid  
:----			|:---			    |:---	                |:---
60				|期刊库			    |xy_magazine            |59
61	            |期刊日期库	        |xy_magDate	            |60
62	            |期刊栏目库	        |xy_magColumn	        |61
63	            |期刊稿件库	        |xy_magArticle	        |62


message						|string			|&nbsp;
data						|object			|&nbsp;
&emsp;magazines				|object			    |
&emsp;&emsp;magName			|string			    |期刊名称
&emsp;&emsp;coverPic		|string			    |期刊封面
&emsp;&emsp;pdDate	        |date             |日期

doclibs
doclibid    |doclibname          |doctypeid
:----						|:---			|:---	
60	期刊库	xy_magazine	        59
61	期刊日期库	xy_magDate	    60
62	期刊栏目库	xy_magColumn	61
63	期刊稿件库	xy_magArticle	62



#### 电子报
##### 1. 提供电子报纸最新一期时间、封面
点击进入栏目（电子报），接口提供出所有种类的电子版的最新一期的
电子报名称，封面图片，日期，版面数量

备选
```
报纸封面 与 头版 (pl_layout ASC) ?


SELECT SYS_DOCUMENTID, pl_paper, pl_paperID, pl_date, pl_layout
FROM xy_paperlayout
where pl_paperID =1
order by pl_date DESC, pl_layout ASC
limit 1


头版图片
SELECT SYS_DOCUMENTID, att_url FROM xy_paperattachment
where att_articleID = 406678
and att_articleLibID = 49
and att_type = 5;


版面数量
select count(1) sum from (SELECT count(1) cnt FROM xy_paperlayout
where pl_paperID = 1
and pl_date = '2019-04-30'
group by pl_layout) aa;
```


use
***
```
SELECT SYS_DOCUMENTID as id , pl_paper as plPaper, pl_paperID as plPaperID, pl_date as plDate from 
( SELECT /* parallel */ pl_paperID, SYS_DOCUMENTID, pl_paper, pl_date FROM `xy_paperlayout` order by pl_date) aa 
group by aa.pl_paperID


SELECT SYS_DOCUMENTID, pl_paper, pl_paperID, pl_date 
from v_paperlayout


SELECT * from (SELECT * FROM xy_paperdate
order by pd_date DESC  limit 1000000) aa
group by aa.pd_paperID

select lay.SYS_DOCUMENTID, lay.pl_date, lay.pl_paperID, lay.pl_layoutName
,arti.SYS_DOCUMENTID as arti_ID, arti.a_layout, arti.a_columnID, arti.SYS_TOPIC
,att.SYS_DOCUMENTID, att.att_url, att.att_type
FROM xy_paperlayout as lay
join xy_paperarticle as arti
on lay.SYS_DOCUMENTID = arti.a_layoutID
left join xy_attachment as att
on att.att_articleID = arti.SYS_DOCUMENTID
where lay.pl_date = '2019-04-30'
and lay.pl_paperID = 1


select * from xy_attachment as att, (select lay.pl_date, lay.pl_paperID, lay.pl_layoutName, 
arti.SYS_DOCUMENTID as arti_ID, arti.a_layout, arti.a_columnID, arti.SYS_TOPIC
FROM xy_paperarticle as arti
join xy_paperlayout as lay
on lay.SYS_DOCUMENTID = arti.a_layoutID
where 1=1
and lay.pl_date = '2019-04-30'
and lay.pl_paperID = 1) as aa
where att.att_articleID = aa.arti_ID

```
***

##### 2. 提供某一报纸的日历（有内容的）
在列表中点击某一 报纸封面 后，报纸内容展示界面的日历位置

```
select att.att_url, att.att_content, arti.a_paper, lay.pl_date
from xy_paperattachment as att
join xy_paperarticle as arti 
on att.att_articleID = arti.SYS_DOCUMENTID
join xy_paperlayout as lay
on arti.a_layoutID = lay.SYS_DOCUMENTID 
where att.SYS_DOCUMENTID = 645503

(xy_paperarticle.content)


根据年、月提供出有内容的天数
select GROUP_CONCAT(aa.pd_day) as dayList, count(1) as count from(select pd_day from xy_paperdate
where pd_year = 2015
and pd_month = 10
group by pd_date
order by pd_date) aa


// ignore
select count(1) from (select distinct pd_day from xy_paperdate
where pd_year = 2015
and pd_month = 10
order by pd_date) as aa
```

##### 3. 提供某一期报纸的头版版面小图、内容坐标、版面目录、头版内容列表、版面原图
内容坐标 ？
版面原图 （本版、本期各版）？

```
版面目录
SELECT SYS_DOCUMENTID, pl_layout, pl_layoutName FROM xy_paperlayout
where pl_paperID = 1
and pl_date = '2019-04-30'
group by pl_layout


头版内容列表
SELECT SYS_DOCUMENTID, SYS_TOPIC, a_content, a_paperID FROM `xy_paperarticle`
where a_layoutID = 406678;

头版版面小图
SELECT SYS_DOCUMENTID, att_url FROM xy_paperattachment
where att_articleID = 406678
and att_articleLibID = 49
and att_type = 5;
```

##### 4. 提供某一期报纸的某一版面的小图、内容坐标、版面内容列表、版面原图

```
内容坐标
SELECT SYS_DOCUMENTID, pl_layout, pl_layoutName, pl_date FROM xy_paperlayout
where pl_paperID = 1
and pl_date = '2019-04-30'
and pl_layout = 1


本版内容列表
SELECT SYS_DOCUMENTID, SYS_TOPIC, a_content, a_paperID FROM `xy_paperarticle`
where a_layoutID = 406678;


版面目录
SELECT SYS_DOCUMENTID, pl_layout, pl_layoutName FROM xy_paperlayout
where pl_paperID = 1
and pl_date = '2019-04-30'
group by pl_layout


本版小图
SELECT SYS_DOCUMENTID, att_url FROM xy_paperattachment
where att_articleID = 406678
and att_articleLibID = 49
and att_type = 5;
```


##### 5. 提供某一篇内容的标题、摘要、日期、来源、内容、图集图片、本版内容坐标、内容图片等。以及版面小图

```
http://www.jjrbgroup.cn/paper/ncjrsb/c/201510/26/c962.html

select SYS_DOCUMENTID as id, SYS_TOPIC as title, a_subTitle as title, a_leadTitle as leadTitle, a_abstract as abstra,
a_source as source, SYS_AUTHORS as authors, a_pubTime as pubTime, a_layoutID as layoutId from xy_paperarticle
where SYS_DOCUMENTID = 962

本版面小图（xy_paperarticle.SYS_DOCUMENTID >> a_layoutID）
本版内容坐标(xy_paperlayout.pd_paperID, .pl_date, .pl_layout)

文章标题(SYS_TOPIC)
文章引题(a_leadTitle)
文章副题(a_subTitle)
文章日期(a_pubTime --->> date)
文章摘要(a_abstract)
内容来源(a_source)
文章作者(SYS_AUTHORS)

图集图片???

文章内容(a_content)
正文图片
SELECT SYS_DOCUMENTID, att_articleID, att_articleLibID, att_url FROM `xy_paperattachment`
where att_articleID = 962
and att_articleLibID = 50;

```



#### 电子期刊接口
##### 1. 提供电子期刊最新一期时间、封面

提供出所有种类的电子期刊的最新一期的：
期刊名称
封面图片
日期

```
select pd_paperID, pd_date, pd_url, pd_JName, pd_JType
from xy_magdate
where pd_paperID = 1
order by pd_date DESC (期刊)
limit 1

SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
where att_articleLibID = 60
-- order by att_objID DESC;
and att_articleID = 1
and att_objID = 20151216
and att_type = 5
limit 1
```

##### 2. 提供某一期刊往期杂志、期刊名称、时间、目录结构、封面图
入参：2015-12-16   magCode=zgjjxx
该期期刊的刊物名称、发布时间日期、刊物封面图、目录结构及文章、往期刊物图片、日期等

```
刊物名称
SELECT pa_name, pa_code FROM `xy_magazine`
where SYS_DOCUMENTID = 1

刊物发布日期
	2015-12-16
刊物封面
SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
where att_articleLibID = 60
and att_articleID = 1
and att_type = 5
and att_objID = 20151216
limit 1



目录结构
SELECT GROUP_CONCAT(SYS_TOPIC), a_column, a_columnID
FROM `xy_magarticle`
where a_magazineID = 1
and a_pubTime = '2015-12-16 00:00:00'
group by a_columnID
order by SYS_DOCUMENTID DESC

如： 酷科技,天下,连线,政策解读,媒体,读懂中国制造2025,影响力,趋势,箴言	资讯	6353


文章结构  ?



往期杂志图
/*SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
where att_articleLibID = 60
and att_articleID = 1
and att_objID = 20151201
and att_type = 5
limit 1*/


SELECT SYS_DOCUMENTID, att_articleID, att_url, att_objID FROM `xy_attachment`
where att_articleLibID = 60
and att_articleID = 1
and att_objID LIKE '201512%'
and att_objID <> 20151216
and att_type = 5
group by att_objID



往期杂志时间
/* select pd_paperID, pd_date, pd_url, pd_JName, pd_JType
from xy_magdate
where pd_paperID = 1
and pd_date <> '2015-12-16'
group by pd_date
order by pd_date DESC */

select pd_paperID, pd_date, pd_url, pd_JName, pd_JType
from xy_magdate
where pd_paperID = 1
and pd_year = 2015
and pd_month = 12
and pd_date <> '2015-12-16'
group by pd_date
order by pd_date DESC
```

##### 3. 提供某一期刊物具体内容等
在期刊目录中点击某一具体内容后，进入页面需要展示的刊物目录结构、文章结构、日期、标题、图集图片、内容正文、内容图片、往期杂志图、往期杂志时间等

```
-2： 期刊目录(见上 [a_pubTime, a_type])
SELECT GROUP_CONCAT(SYS_TOPIC), a_column, a_columnID
FROM `xy_magarticle`
where a_magazineID = 1
and a_pubTime = '2015-12-16 00:00:00'
group by a_columnID
order by SYS_DOCUMENTID DESC


  文章结构 ？
  
  
标题
引题
副题
作者
摘要
发布时间
正文
-1： select SYS_TOPIC, a_subTitle, a_leadTitle, SYS_AUTHORS, a_abstract, a_pubTime, a_content
from xy_magarticle
where SYS_DOCUMENTID = 606522

596568
 
图集图片
select SYS_DOCUMENTID, att_articleID, att_url from xy_attachment
where att_articleID = 606522
and att_articleLibID = 63
and att_type = 5


正文图（内容图）  ?


往期刊物图（见上）
  往期刊物日期 （见上）

SELECT SYS_DOCUMENTID, SYS_TOPIC, a_type, a_pubTime, a_subTitle, a_leadTitle, a_abstract, a_url, a_source, a_column, a_columnID, a_content
FROM `xy_magarticle`
where a_magazineID = 1
and a_pubTime = '2015-12-16 00:00:00'
-- order by a_pubTime DESC;
```

##### 4. 提供往期杂志页面的具体数据，默认当前进入的刊物
点击进入期刊往期杂志页面后，根据点击的刊物名称给出默认的内容，并默认年限为最新

```
全部期刊名称
选择年份（默认最新）
select max(md.pd_year), m.pa_name
from xy_magdate as md
join xy_magazine as m
on m.SYS_DOCUMENTID = md.pd_paperID 
where pd_paperID = 1
group by pd_paperID


刊物时间
select pd_paperID, pd_date, pd_url, pd_JName, pd_JType
from xy_magdate
where pd_paperID = 1
and pd_year = 2015
order by pd_date DESC


刊物封面
SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
where att_articleLibID = 60
and att_articleID = 1
and att_objID = 20151216
and att_type = 5
limit 1
```

##### 5. 提供年限选择接口，根据年限选择提供对应的内容
在往期刊物展示页面，点击时间选择后，根据选择的时间（年），给出对应的输出结果
刊物图片
刊物时间

http://www.jjrbgroup.cn/magazine/zgjjxx/date/mag1.20150816.html

```
入参：zgjjxx       20150816
年份接口
select DISTINCT md.pd_year, md.pd_paperID, m.pa_name
from xy_magdate as md, xy_magazine as m
where md.pd_paperID = m.SYS_DOCUMENTID
and m.pa_code = 'zgjjxx'
order by md.pd_year DESC



SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
where att_articleLibID = 60
and att_articleID = 1
and att_objID like '2015%'
and att_type = 5
order by SYS_DOCUMENTID DESC



select pd_paperID, pd_date, pd_url, pd_JName, pd_JType
from xy_magdate
where pd_paperID = 1
and pd_year = 2015
order by pd_date DESC
```

##### 6. 提供点击不同刊物的封面，对应进入刊物的内容
往期刊物页面，只展示了图片、日期，需要根据对应条件，如：当前刊物名、时间 给出对应的接口1的内容。

```
期刊名称
封面图片
日期
  ...

?????

SELECT pd_paperID AS pdPaperID, pd_date AS pdDate, pd_JName, pd_url
FROM xy_magdate WHERE pd_date = '2015-01-01' and pd_JName like '%经济%'


SELECT SYS_DOCUMENTID, att_articleID, att_url FROM `xy_attachment`
where att_articleLibID = 60
and att_articleID = 1
and att_objID = '20150101'
and att_type = 5
order by SYS_DOCUMENTID DESC

```


#### 历史资源
##### 1. 文章资源
历史资源（电子报、期刊）的全部文章及其对应字段及对应内容，图片等
已入库的
新增入库的

xy_attachment
图片存储;xy/  (z:/pic/xy/)        
期刊存储;imported/  (z:/magazine/imported/)

xy_paperattachment
报纸存储;  (z:/paper/)



版次（版面图）
文章-附件：.doc
文章-图片：jpg



##### 2. 期刊原件
期刊原始文件：pdf、xlm、内容附件等，需要压缩并通过接口提供，存入区块链中。
期刊：以日为单位，根据期刊名称/年-月-日进行压缩
已入库的
新增入库的





###### 3. 电子报原件
电子报的原始文件：pdf、xlm、内容附件等，需要压缩并通过接口提供，存入区块链中。
报纸：以日为单位，根据报纸名称/年-月-日进行压缩
已入库的
新增入库的


historySource/newsPapers/query?pageNo=1&limit=10
historySource/article/query?pageNo=1&limit=10 (405没有捕获)

