package com.founder.econdaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TreeMenu {
    private static final Logger logger = LoggerFactory.getLogger(TreeMenu.class);
    public static void main(String[] args){
        StringBuffer buffer = new StringBuffer("[");
        buffer.append("{'muld': 5, 'muSign': 'admin', 'composeSign': 'admin', 'menuName': '系统管理', 'muUrl': '#', 'muIndex': 1, 'muType': 0, 'muGrade': 1, 'mnIconStyle': 'fa-github', 'parentId': 1}");
        buffer.append("{'muld': 6, 'muSign': 'dd', 'composeSign': 'admin:dd', 'menuName': '菜单管理', 'muUrl': 'sysMenu/layout', 'muIndex': 1, 'muType': 0, 'muGrade': 1, 'mnIconStyle': 'fa-github', 'parentId': 5}");
        buffer.append("{'muld': 11, 'muSign': 'Test', 'composeSign': 'admin:Test', 'menuName': '测试菜单', 'muUrl': '/123', 'muIndex': 1, 'muType': 0, 'muGrade': 2, 'mnIconStyle': 'fa-github', 'parentId': 5}");
        buffer.append("{'muld': 12, 'muSign': 'add', 'composeSign': 'dd:add', 'menuName': '添加', 'muUrl': '123', 'muIndex': 1, 'muType': 1, 'muGrade': 2, 'mnIconStyle': 'fa-github', 'parentId': 6}");
        buffer.append("{'muld': 13, 'muSign': 'entl', 'composeSign': 'dd:entl', 'menuName': '修改', 'muUrl': '/', 'muIndex': 456, 'muType': 1, 'muGrade': 2, 'mnIconStyle': 'fa-github', 'parentId': 6}");
        buffer.append("{'muld': 14, 'muSign': 'Test2', 'composeSign': 'Test:Test2', 'menuName': '测试菜单2', 'muUrl': '/456', 'muIndex': 1, 'muType': 0, 'muGrade': 3, 'mnIconStyle': 'fa-github', 'parentId': 11}");
        buffer.append("{'muld': 15, 'muSign': 'log', 'composeSign': 'log', 'menuName': '日志管理', 'muUrl': '/log', 'muIndex': 1, 'muType': 0, 'muGrade': 1, 'mnIconStyle': 'fa-github', 'parentId': 1}");
        buffer.append("]");

        JSONArray array = JSONArray.parseArray(buffer.toString());
        //logger.info("==================== {} =====================", array.toJSONString());
        JSONArray array_x = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject json = array.getJSONObject(i);
            if(json.getInteger("parentId").equals(1)) {
                array_x.add(json);
            }
        }

        logger.info("==================== start {} =====================", array_x.toJSONString());

        handler(array_x, array);

        logger.info("==================== complete {} =====================", array_x.toJSONString());
        
    }

    private static void handler(JSONArray array_x, JSONArray array) {
        JSONArray array_app = null;
        for (int i = 0; i < array_x.size(); i++) {
            array_app = new JSONArray();
            for (int j = 0; j < array.size(); j++) {
                if(array_x.getJSONObject(i).getInteger("muld").equals(array.getJSONObject(j)
                        .getInteger("parentId"))) {
                    array_app.add(array.getJSONObject(j));
                }
            }
            if(array_app.size() > 0) {
                handler(array_app, array);
                array_x.getJSONObject(i).put("children", array_app);
            }
        }
    }
}