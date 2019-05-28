package com.founder.econdaily.modules.newspaper.entity;

import com.founder.econdaily.common.entity.BasePrefix;

public class NewsPaperKey extends BasePrefix {

    private NewsPaperKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static NewsPaperKey netestPapers = new NewsPaperKey(3600, "NP:");

    public static final String NEWS_PAPER_KEY = "netestPapers";
}
