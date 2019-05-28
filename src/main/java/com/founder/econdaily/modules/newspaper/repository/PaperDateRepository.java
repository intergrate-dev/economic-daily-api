package com.founder.econdaily.modules.newspaper.repository;

import com.founder.econdaily.modules.newspaper.entity.PaperDateStatis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaperDateRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 日志实例
     */
    private static final Logger logger = LoggerFactory.getLogger(PaperDateRepository.class);

    /**
     * 查询所有报纸最新一期的版面信息
     *
     * @return
     */
    public PaperDateStatis queryDatesByYearAndMonth(String year, String month) {
        StringBuffer sql = new StringBuffer();
        sql.append("select GROUP_CONCAT(aa.pd_day) as days, count(1) as total from(select pd_day from xy_paperdate ")
                .append("where pd_year = ? and pd_month = ? group by pd_date order by pd_date) aa ");
        List<PaperDateStatis> list = null;
        list = jdbcTemplate.query(sql.toString(), new Object[]{year, month}, new BeanPropertyRowMapper(PaperDateStatis.class));
        return list != null && list.size() > 0 ? list.get(0) : null;
    }

}
