package com.founder.econdaily.modules.magazine.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.founder.ark.common.utils.DateUtil;
import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.magazine.dto.MagazineVo;
import com.founder.econdaily.modules.magazine.dto.MagzineDateVo;
import com.founder.econdaily.modules.magazine.entity.MagCatalog;
import com.founder.econdaily.modules.magazine.entity.Magazine;
import com.founder.econdaily.modules.magazine.entity.MagazineArticle;
import com.founder.econdaily.modules.magazine.repository.MagazineAttachmentRepository;
import com.founder.econdaily.modules.magazine.repository.MagazineDateRepository;
import com.founder.econdaily.modules.magazine.repository.MagazineRepository;
import com.founder.econdaily.modules.magazine.service.MagazineService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description ：
 */

@Service
public class MagazineServiceImpl implements MagazineService {

	/**
	 * 日志实例
	 */
	private static final Logger logger = LoggerFactory.getLogger(MagazineServiceImpl.class);

	@Autowired
	private MagazineRepository magazineRepository;

	@Autowired
	private MagazineAttachmentRepository magAttachmentRepository;

	@Autowired
	private MagazineDateRepository magazineDateRepository;

	private  MagazineVo parseEntity(Magazine magazine) {
		MagazineVo mv = new MagazineVo();
		mv.setPdDate(DateParseUtil.dateToString(magazine.getPdDate()));
		mv.setCoverPic(magazine.getCoverPic());
		mv.setMagName(magazine.getPdJName());
		String sr = magazine.getPdJName();
		if (sr.startsWith("《")) {
			sr = sr.substring(sr.indexOf("《") + 1, sr.indexOf("》"));
		}
		MagazineVo magazineVo = magazineRepository.queryByMagName(sr);
		if (magazineVo != null) {
			mv.setMagCode(magazineVo.getMagCode());
		}
		return mv;
	}

	@Override
	public Map<String, Object> queryMagNewtests() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Magazine> magazines = magazineDateRepository.findNewtestPaper();
		List<MagazineVo> mvs = new ArrayList<MagazineVo>();
		for (Magazine mag : magazines) {
			if (mag.getPdDate() != null) {
				mag.setCoverPic(magAttachmentRepository.findCoverByArticleIdAndLibId(mag.getId(), Magazine.LAYOUT_LIB_ID,
						DateParseUtil.dateToStringWithSplit(mag.getPdDate())));
			}
			mvs.add(this.parseEntity(mag));
		}
		map.put("magNewtests", mvs);
		return map;
	}

	@Override
	public Map<String, Object> queryByPdDateAndMagCode(String pdDate, String magCode) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		MagazineVo mag = magazineRepository.queryByMagCode(magCode);
		if (mag == null) {
			return map;
		}
		String objId = DateParseUtil.stringSplit(pdDate);
        mag.setCoverPic(magAttachmentRepository.findCoverByArticleIdAndLibId(mag.getMagId(), Magazine.LAYOUT_LIB_ID, objId));
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> cMap = null;
		List<MagCatalog> magCatalogs = magazineRepository.queryCatalogsByPdDate(pdDate, mag.getMagId(), false);
		for (MagCatalog magCatalog : magCatalogs) {
			cMap = new HashMap<String, Object>();
			cMap.put("theme", magCatalog.getColumnName());
			cMap.put("artiTopics", null);
			JSONArray array = new JSONArray();
			JSONObject json = null;
			if (!StringUtils.isEmpty(magCatalog.getArtiTopics())) {
				String[] split = magCatalog.getArtiTopics().split(RegxUtil.COMMA_SPLIT);
				for (int i = 0; i < split.length; i++) {
					String con = split[i];
					if (con.contains(RegxUtil.STRIP_SPLIT)) {
						String[] sp = con.split(RegxUtil.STRIP_SPLIT);
						json = new JSONObject();
						json.put("id", sp[1]);
						json.put("title", sp[0]);
						array.add(json);
					}
				}
				cMap.put("artiTopics", array);
			}
			list.add(cMap);
		}

		mag.setMagCatalogs(list);
		//文章结构

		map.put("magazine", mag);

        this.queryOtherMags(pdDate, map, mag.getMagId());
		return map;
	}

    private void queryOtherMags(String pdDate, Map<String, Object> map, String paperId) {
        Map<String, String> nMap = null;
        List<Map<String, String>> otherMap = new ArrayList<Map<String, String>>();
        List<Magazine> otherPdDateMags = magazineDateRepository.findByPaperIdAndPdDate(paperId, pdDate);
        for (Magazine magazine : otherPdDateMags) {
            nMap = new HashMap<String, String>();
            nMap.put("pdDate", DateParseUtil.dateToString(magazine.getPdDate()));
            // nMap.put("coverPic", magAttachmentRepository.queryCoversByOtherPdDay(mag.getMagId(), Magazine.LAYOUT_LIB_ID, objId));
            nMap.put("coverPic", magazine.getPdUrl());
            otherMap.add(nMap);
        }
        map.put("otherMags", otherMap);
    }

    @Override
	public Map<String, Object> queryMagazineDetailAndArticle(String articleId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		MagazineArticle article = magazineRepository.queryArticleByArticleId(articleId);
        String pubTime = DateFormatUtils.format(article.getPubTime(), "yyyy-MM-dd");
		Map<String, Object> cMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<MagCatalog> magCatalogs = magazineRepository.queryCatalogsByPdDate(pubTime, article.getType(), true);
		if (magCatalogs != null && magCatalogs.size() > 0) {
			for (MagCatalog magCatalog : magCatalogs) {
				cMap = new HashMap<String, Object>();
				cMap.put("theme", magCatalog.getColumnName());
				cMap.put("artiTopics", null);
				if (!StringUtils.isEmpty(magCatalog.getArtiTopics())) {
					cMap.put("artiTopics", magCatalog.getArtiTopics().split(RegxUtil.COMMA_SPLIT));
				}
				list.add(cMap);
			}
		}
		/*mag.setMagCatalogs(list);
		map.put("magazine", mag);*/
		map.put("magCatalog", list);
		//图集
		JSONArray array = new JSONArray();
		JSONObject json = null;
		String[] split = magAttachmentRepository.findCoverByArticle(articleId).split(RegxUtil.COMMA_SPLIT);
		for (int i = 0; i < split.length; i++) {
			String con = split[i];
			if (con.contains("&")) {
				String[] sp = con.split("&");
				json = new JSONObject();
				json.put("url", sp[0]);
				if (sp.length > 1) {
					json.put("desc", sp[1]);
				}
				array.add(json);
			}
		}
		map.put("magPics", array);
		map.put("content", article.getContent());
		// TODO 正文（内容）图？
		// map.put("contentPics", new ArrayList<String>());

		/*Map<String, String> nMap = null;
		List<Map<String, String>> otherMap = new ArrayList<Map<String, String>>();
		List<Magazine> otherPdDateMags = magazineDateRepository.findByPaperIdAndPdDate(article.getType(), pubTime);
		for (Magazine magazine : otherPdDateMags) {
			nMap = new HashMap<String, String>();
			nMap.put("pdDate", DateUtil.formatDate(magazine.getPdDate(), "yyyy-MM-dd"));
			nMap.put("coverPic", magAttachmentRepository.queryCoversByOtherPdDay(article.getType(), Magazine.LAYOUT_LIB_ID,
					DateParseUtil.dateToStringWithSplit(magazine.getPdDate())));
			otherMap.add(nMap);
		}
		map.put("otherMags", otherMap);*/
        this.queryOtherMags(pubTime, map, article.getType());
		return map;
	}

    @Override
    public Map<String, Object> queryMagazineInfoByMagCode(String magCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		MagazineVo mag = magazineRepository.queryByMagCode(magCode);
		if (mag == null) {
			return map;
		}

		MagzineDateVo magDateVo = magazineDateRepository.queryDatesByYearAndMonth(mag.getMagId());
		if (magDateVo == null) {
			return map;
		}
		MagazineVo magazine = null;
		List<MagazineVo> list = new ArrayList<MagazineVo>();
		List<MagzineDateVo> magaZineDateVos = magazineDateRepository.queryDatesByPdPaperIdAndPdYear(mag.getMagId(), magDateVo.getLatestYear());
		for (MagzineDateVo magVo : magaZineDateVos) {
			magazine = new MagazineVo();
			magazine.setMagName(magVo.getMagName());
			//magazine.setPdDate(magVo.getPdDate());
			magazine.setPdDate(DateParseUtil.dateToString(magVo.getPdDate()));
			magazine.setCoverPic(magAttachmentRepository.findCoverByArticleIdAndLibId(mag.getMagId(), Magazine.LAYOUT_LIB_ID,
					DateParseUtil.dateToStringWithSplit(magVo.getPdDate())));
			list.add(magazine);

		}
		map.put("latestYear", magDateVo.getLatestYear());
		map.put("magazines", list);
		return map;
    }

	@Override
	public Map<String, Object> queryMagazineInfoByMagCodeAndYear(String magCode, String pdDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MagazineVo> mvList = new ArrayList<MagazineVo>();
		List<MagzineDateVo> magzineDateVos = magazineDateRepository.queryDatesByPdPaperId(magCode);
		for (MagzineDateVo magzineDateVo : magzineDateVos) {
			// pdDate: year
			List<MagzineDateVo> list = magazineDateRepository.queryDatesByPdPaperIdAndPdYear(magzineDateVo.getMagId(), pdDate);
			MagazineVo magazine = null;
			for (MagzineDateVo dateVo : list) {
				magazine = new MagazineVo();
				if (dateVo.getPdDate() != null) {
					/*magazine.setCoverPic(magAttachmentRepository.queryCoversAll(magzineDateVo.getMagId(), Magazine.LAYOUT_LIB_ID,
							DateParseUtil.dateToStringWithSplit(dateVo.getPdDate())));*/
					/*magazine.setCoverPic(magAttachmentRepository.queryCoversMagCoverPic(magzineDateVo.getMagId(),
							Magazine.LAYOUT_LIB_ID, DateParseUtil.dateToStringWithSplit(dateVo.getPdDate())));*/
					magazine.setCoverPic(magAttachmentRepository.findCoverByArticleIdAndLibId(magzineDateVo.getMagId(),
							Magazine.LAYOUT_LIB_ID, DateParseUtil.dateToStringWithSplit(dateVo.getPdDate())));
				}
				magazine.setMagName(dateVo.getMagName());
				//magazine.setPdDate(dateVo.getPdDate());
				magazine.setPdDate(DateParseUtil.dateToString(dateVo.getPdDate()));
				mvList.add(magazine);
			}
		}
		map.put("magazines", mvList);
		return map;
	}

    @Override
    public Map<String, Object> queryByParams(String magName, String pdDate, Integer pageNo, Integer limit) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql_count = new StringBuffer();
		sql.append("SELECT pd_paperID AS magId, pd_date AS pdDate, pd_JName as magName FROM xy_magdate WHERE 1 = 1 ");
		sql_count.append("SELECT count(1) FROM xy_magdate WHERE 1 = 1 ");
		List<Object> params = new ArrayList<Object>();
		if (!org.apache.commons.lang3.StringUtils.isEmpty(pdDate)) {
			params.add(pdDate);
			sql.append("and pd_date = ? ");
			sql_count.append("and pd_date = ? ");
		}
		if (!org.apache.commons.lang3.StringUtils.isEmpty(magName)) {
			params.add("%".concat(magName).concat("%"));
			sql.append("and pd_JName like ? ");
			sql_count.append("and pd_JName like ? ");
		}

		Integer total = magazineDateRepository.countByExample(sql_count, params);
		if (pageNo != null && limit != null) {
			sql.append("limit ".concat(String.valueOf((pageNo - 1) * limit)).concat(", ").concat(String.valueOf(limit)));
		}
		List<MagazineVo> magazines = magazineDateRepository.queryByParams(sql, params, pageNo, limit);
		for (MagazineVo magazine : magazines) {
			/*magazine.setCoverPic(magAttachmentRepository.findCoverByArticleIdAndLibId(magazine.getMagId(), Magazine.LAYOUT_LIB_ID,
					DateParseUtil.dateToStringWithSplit(magazine.getPdDate())));*/
			magazine.setCoverPic(magAttachmentRepository.findCoverByArticleIdAndLibId(magazine.getMagId(), Magazine.LAYOUT_LIB_ID,
					DateParseUtil.stringSplit(magazine.getPdDate())));
		}
		map.put("list", magazines);
		map.put("totalCount", total);
		return map;
    }

}
