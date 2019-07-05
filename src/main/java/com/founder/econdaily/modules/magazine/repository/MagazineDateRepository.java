package com.founder.econdaily.modules.magazine.repository;

import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.magazine.dto.MagazineVo;
import com.founder.econdaily.modules.magazine.dto.MagzineDateVo;
import com.founder.econdaily.modules.magazine.entity.Magazine;
import com.founder.econdaily.modules.magazine.entity.MagazineDateStatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MagazineDateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(MagazineDateRepository.class);

    /**
     * 查询所有报纸最新一期的版面信息
     *
     * @return
     */
    public List<Magazine> findNewtestPaper() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT pd_paperID as id , pd_JName as pdJName, pd_paperID as pdPaperID, pd_date as pdDate from ( ")
                .append("SELECT pd_paperID, pd_date, pd_JName FROM `xy_magdate` order by pd_date DESC limit 100000 ")
                .append(") aa group by aa.pd_paperID");
        List<Magazine> list = null;
        list = jdbcTemplate.query(sql.toString(), new Object[]{}, new BeanPropertyRowMapper(Magazine.class));
        return list;
    }

    /**
     * 查询所有期刊最新一期的期刊信息
     *
     * @return
     */
    public MagzineDateVo queryDatesByYearAndMonth(String pdPaperId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select max(md.pd_year) as latestYear, m.pa_name as magName, md.pd_date as pdDate from xy_magdate as md ")
                .append("join xy_magazine as m on m.SYS_DOCUMENTID = md.pd_paperID where pd_paperID = ? ");
        List<MagzineDateVo> list = jdbcTemplate.query(sql.toString(), new Object[]{pdPaperId}, new BeanPropertyRowMapper(MagzineDateVo.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

    public List<MagzineDateVo> queryDatesByPdPaperIdAndPdYear(String pdPaperId, String pdYear) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pd_date as pdDate, pd_JName as magName from xy_magdate where pd_paperID = ? " +
                "and pd_year = ? order by pd_date DESC");
        return jdbcTemplate.query(sql.toString(), new Object[]{pdPaperId, pdYear}, new BeanPropertyRowMapper(MagzineDateVo.class));
    }

    /*select DISTINCT md.pd_year, md.pd_paperID, m.pa_name
    from xy_magdate as md, xy_magazine as m
    where md.pd_paperID = m.SYS_DOCUMENTID
    and m.pa_code = 'zgjjxx'
    order by md.pd_year DESC*/
    public List<MagzineDateVo> queryDatesByPdPaperId(String magCode) {
        StringBuffer sql = new StringBuffer();
        sql.append("select DISTINCT md.pd_year as latestYear, md.pd_paperID as magId from xy_magdate as md, xy_magazine as m where md.pd_paperID = m.SYS_DOCUMENTID " +
                "and m.pa_code = ? order by md.pd_year DESC ");
        return jdbcTemplate.query(sql.toString(), new Object[]{magCode}, new BeanPropertyRowMapper(MagzineDateVo.class));
    }


    public List<Magazine> findByPaperIdAndPdDate(String paperId, String pdDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT pd_paperID as pdPaperID, pd_date as pdDate, pd_url as pdUrl FROM xy_magdate where pd_paperID = ? ")
                .append("and pd_year = ? and pd_date <> ? group by pd_date order by pd_date DESC ");
        List<Magazine> list = null;
        String[] split = pdDate.split(RegxUtil.STRIP_SPLIT);
        list = jdbcTemplate.query(sql.toString(), new Object[]{paperId, split[0], pdDate}, new BeanPropertyRowMapper(Magazine.class));
        return list;
    }

    public List<MagazineVo> queryByParams(StringBuffer sql, List<Object> params, Integer pageNo, Integer limit) {
        return jdbcTemplate.query(sql.toString(), params.toArray(), new BeanPropertyRowMapper(MagazineVo.class));
    }

    public Integer countByExample(StringBuffer sql, List<Object> params) {
        return this.jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }


    public MagazineDateStatis queryDatesByYearAndMonth(String year, String month) {
        StringBuffer sql = new StringBuffer();
        sql.append("select GROUP_CONCAT(aa.pd_day) as days, count(1) as total from(select pd_day from xy_paperdate ")
                .append("where pd_year = ? and pd_month = ? group by pd_date order by pd_date) aa ");
        List<MagazineDateStatis> list = null;
        list = jdbcTemplate.query(sql.toString(), new Object[]{year, month}, new BeanPropertyRowMapper(MagazineDateStatis.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
}
