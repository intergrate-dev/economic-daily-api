package com.founder.econdaily;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TreeMenu {
    public static void main(String[] args) {
        List<Person> persons = new ArrayList();
        //用来临时存储person的id
        persons.add(new Person(1, "name1", 10));
        persons.add(new Person(2, "name2", 21));
        persons.add(new Person(5, "name5", 55));
        persons.add(new Person(3, "name3", 34));
        persons.add(new Person(1, "name1", 10));

        List<Integer> ids = new ArrayList<>();
        List<Person> personList = persons.stream().filter(
                v -> {
                    boolean flag = !ids.contains(v.getId());
                    ids.add(v.getId());
                    return flag;
                }
        ).collect(Collectors.toList());
        System.out.println(personList);
    }

    private static class Person {
        private Integer id;
        private String name;
        private Integer age;

        public Person(Integer id, String mane, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}