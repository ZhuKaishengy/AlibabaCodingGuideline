package com.percent.chapter1;

import com.sun.deploy.util.StringUtils;
import lombok.*;
import lombok.experimental.Accessors;
import org.junit.Test;

import javax.swing.*;
import java.security.Key;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author: kaisheng.zhu
 * @Date: 2018-7-31 15:09
 * @Description:
 */
public class CodingGuidelineTest {

    /**
     * 说明：推荐使用 引入的工具类 java.util.Objects#equals（JDK7），避免空指针
     */
    @Test
    public void testEquals(){

        String str1 = "";
        String str2 = " ";

        boolean flag = Objects.equals(str1, str2);
        System.out.println(flag);
    }

    /**
     * 慎用的方法来拷贝对象。Object clone
     *
     * 实现克隆的几种方式
     * 1、对于基本数据类型
     * 2、引用数据类型的浅克隆
     * 3、引用数据类型的深克隆
     * 总结：深度浅度克隆针对的是引用数据类型的对象，这个对象其中还包含引用数据类型的对象
     * 浅克隆不能克隆对象的对象
     * 浅克隆能克隆对象的对象
     *
     *  one example of deep clone{@link java.util.Date#clone()}
     */
    @Test
    public void testClone() throws CloneNotSupportedException {

        // 对于基本数据类型：创建一个变量指向原变量的引用
        int num = 5;
        int cloneNum = num;

        num = 6;
        System.out.println("num:"+num+" cloneNum:"+cloneNum);

        // 对于引用数据类型的变量实现浅度克隆
        User user1 = new User();
        Dept dept1 = new Dept();
        dept1.setDeptId("9001")
                .setDeptName("aisino");
        user1.setUserId("1001")
                .setUserName("zks")
                .setDept(dept1);
        System.out.println("user1:"+user1);

        User user2 = user1.clone();
        System.out.println("user2:"+user2);

        // 改变原对象中的引用数据类型的属性，克隆对象中对应的属性也会发生变化，
        // 说明这个属性没有被克隆，指向的是同一个引用!!!而其基本数据类型的属性没有跟着变化
        dept1.setDeptId("9002").setDeptName("percent");
        user1.setDept(dept1).setUserId("1002");

        System.out.println("change...user1:"+user1);
        System.out.println("change...user2:"+user2);

        // 对于引用数据类型的变量实现深度克隆
        Person p1 = new Person();
        Company company1 = new Company();
        company1.setCompanyId("9001").setCompanyName("aisino");
        p1.setPersonId("1001").setPersonName("zks").setCompany(company1);
        System.out.println("p1:"+p1);

        Person p2 = p1.clone();
        System.out.println("p2:"+p2);
        // 改变原对象中的引用数据类型的属性，克隆对象中对应的属性不会发生变化，
        company1.setCompanyId("9002").setCompanyName("percent");
        p1.setCompany(company1);

        System.out.println("change...p1:"+p1);
        System.out.println("change...p2:"+p2);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Accessors(chain = true)
    class User implements Cloneable {

        private String userId;
        private String userName;
        private Dept dept;

        @Override
        protected User clone() throws CloneNotSupportedException {
            User user = null;
            try {
                user = (User) super.clone();
            } catch (CloneNotSupportedException ignored) {} //won't happen
            return user;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Accessors(chain = true)
    class Person implements Cloneable {

        private String personId;
        private String personName;
        private Company company;

        @Override
        protected Person clone() throws CloneNotSupportedException {
            Person p = null;
            try {
                p = (Person) super.clone();
                if (this.company != null){
                    p.company = company.clone();
                }
            } catch (CloneNotSupportedException ignored) {}
            return p;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Accessors(chain = true)
    private class Dept {

        private String deptId;
        private String deptName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Accessors(chain = true)
    private class Company implements Cloneable {

        private String companyId;
        private String companyName;

        @Override
        protected Company clone() throws CloneNotSupportedException {
            return (Company) super.clone();
        }
    }

    /**
     * 使用集合转数组的方法，必须使用集合的toArray，传入的是类型完全一样的数组，大小就是list.size()
        声明一个集合，最好给定初始大小，声明一个map，一定给定初始initialCapacity，不知道给定16
     */
    @Test
    public void testListToArray() {

        // 初始化一个数组
        List<String> list = new ArrayList<>();
        list.add("zks");
        list.add("sjx");
        list.add("rl");

        // 将其转为集合
        String[] strs = new String[list.size()];
        strs = list.toArray(strs);
        System.out.println(Arrays.toString(strs));

        // 初始化一个map集合 size=15*0.75=11.25
        Map<String,String> map = new HashMap<>(16);
        int loopCount = 30;
        for (int i = 0; i < loopCount; i++) {
            map.put(String.valueOf(i),String.valueOf(i));
        }
        System.out.println(map.toString());
    }

    /**
     * 使用工具类把Arrays.asList()数组转换成集合时，不能使用其修改集合相关的方法，
     * 它的add/remove/clear方法会抛出UnsupportedOperationException异常。
     */
    @Test
    public void testArrayToList() {

        // 定义一个数组
        String[] args = new String[]{"1","2","3","4","5"};
        // 将其转换为集合
        List<String> list = Arrays.asList(args);
        System.out.println(list.toString());
        // 调用其add/remove/clear方法
        try {
            list.add("6");
            list.remove("5");
            list.clear();
        } catch (Exception e) { // always happen
            e.printStackTrace();
        }

        System.out.println(list.toString());
    }

    /**
     * 不要在foreach循环里进行元素的add/remove操作。remove元素请使用Iterator方式，如果并发操作，需要对Iterator对象加锁。
     * {@code
     *  while (iterator.hasNext()) {
            String tmp = iterator.next();
            if (Objects.equals(tmp,"haha")) {
                iterator.remove();
            }
        }
     * }这种写法可以简化成
     * {@code list.removeIf(tmp -> Objects.equals(tmp, "haha"));}
     */
    @Test
    public void testForEachAndIterator() {

        // 初始化一个list
        List<String> list = new ArrayList<>(4);
        list.add("zks");
        list.add("sjx");
        list.add("rl");
        list.add("haha");
        System.out.println(list.toString());
        // 使用foreach遍历并进行add/remove操作
        for (String name : list) {
            if (Objects.equals(name,"haha1")) {
                // 会抛出异常java.util.ConcurrentModificationException，移除后集合size发生变化，导致异常
                list.remove(name);
            }
        }
        System.out.println(list);
        // 使用iterator遍历并进行add/remove操作
        list.removeIf(tmp -> Objects.equals(tmp, "haha"));

        System.out.println(list);
        // TODO 测试多线程并发执行
    }

    /**
     * Comparator实现类要满足如下三个条件，不然Arrays.sort，Collections.sort会报IllegalArgumentException异常。
     * 说明：三个条件如下
     * 1） x，y的比较结果和y，x的比较结果相反。
     * 2） x>y，y>z，则x>z。
     * 3） x=y，则x，z比较结果和y，z比较结果相同。
     */
    @Test
    public void testComparator() {

        Comparator<User> comparator = new Comparator<User>() {

            @Override
            public int compare(User user1, User user2) {

                int userId1 = Integer.parseInt(user1.getUserId());
                int userId2 = Integer.parseInt(user2.getUserId());
                if (userId1 == userId2) {
                    return 1;
                } else if (userId1 > userId2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        User user1 = new User().setUserId("1002");
        User user2 = new User().setUserId("1002");
        int result = comparator.compare(user1, user2);
        System.out.println(result);
    }

    /**
     * 使用entrySet遍历Map类集合KV，而不是keySet方式进行遍历。
     * 只要是集合想在遍历中进行修改就必须使用iterator进行遍历
     */
    @Test
    public void testMapEntrySet(){

        // 初始化一个集合
        Map<String,String> map = new HashMap<>(16);
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");

        // 使用entrySet+增强for循环进行遍历（1）
        Set<Map.Entry<String, String>> entries = map.entrySet();
        try {
            for (Map.Entry<String, String> entry : entries) {

                String key = entry.getKey();
                if (Objects.equals(key,"1")) {
                    map.remove(key);
                }
            }
            System.out.println(map);
        } catch (Exception e) {

            e.printStackTrace();// must happen
        }

        // 使用entrySet+iterator进行遍历（2）
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {

            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            if (Objects.equals(key,"1")) {

                iterator.remove();
            }
        }
        System.out.println(map);
    }
    /**
     * 如果是JDK8，使用Map.foreach方法。
     * 这个方法本质上封装了 entrySet+增强for循环进行遍历
     */
    @Test
    public void testMapForeach() {

        // 初始化一个集合
        Map<String,String> map = new HashMap<>(16);
        map.put("1","1");
        map.put("2","2");
        map.put("3","3");

        // 使用Map.foreach遍历内部类的方式（普通）
        map.forEach(new BiConsumer<String, String>() {

            @Override
            public void accept(String key, String value) {

                System.out.println("key:"+key+"--->value:"+value);
            }
        });

        // 使用Map.foreach遍历内部类的方式（lambdas表达式）
        map.forEach((key, value) -> {

            System.out.println("key:"+key+"--->value:"+value);
        });
    }
}
