package com.he.multi;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.*;

public class Test1 {
    private static Logger logger = LoggerFactory.getLogger(Test1.class);

    @Test
    public void test1() throws ParseException {
//        System.out.println("hello");
//
//        String month ="202308";
//        Date yyyyMM = new SimpleDateFormat("yyyyMM").parse(month);
//        String askTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(yyyyMM);
//
//        logger.info("askTime1: "+askTime);
//
//        Calendar calendar = Calendar.getInstance();
//         calendar.setTime(new SimpleDateFormat("yyyyMM").parse(month));
//         calendar.add(Calendar.MONTH,1);
//         askTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
//
//         logger.info("askTime2: "+askTime);
//
//         new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String format = simpleDateFormat.format(new Date());
//        logger.info("format: "+format);

        // 青山之塚

        // 我不再是我，好像被自己迂回了。 你不再是风，风

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isEmpty(list)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }


//        List<Map<String,Object>> list


    }

    @Test
    public void test2() throws ParseException {

        String basePackage = "datasource.proxy.basePackage";

        String[] strings = StringUtils.tokenizeToStringArray(basePackage, ",; \t\n");

        System.out.println(strings);
    }


    @Test
    public void test3() throws ParseException {

        List<Map<String, Object>> list1 = new ArrayList<>();

        List<Map<String, Object>> list2 = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("majorNo", "13");
        map.put("minorNo", "12");
        map.put("UniqueCode", "Code123");
        list2.add(map);


    }

    @Test
    public void test4(){
        String string = UUID.randomUUID().toString();
        System.out.println(string);
    }
}
