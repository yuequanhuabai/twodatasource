package com.he.multi;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test1 {
    private  static Logger logger = LoggerFactory.getLogger(Test1.class);

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
        // 月全 话白

        // 青山之云
        // 我不再是我，好像被自己迂回了。 你不再是风，风

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if(CollectionUtils.isEmpty(list)){
            System.out.println("true");
        }else {
            System.out.println("false");
        }


    }

    @Test
    public void test2() throws ParseException {

        String  basePackage="datasource.proxy.basePackage";

        String[] strings = StringUtils.tokenizeToStringArray(basePackage, ",; \t\n");

        System.out.println(strings);
    }
}
