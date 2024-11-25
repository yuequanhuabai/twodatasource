package com.he.multi.controller;

import com.he.multi.dao.GradesDao;
import com.he.multi.pojo.Grades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/a/")
public class AController {

    private static Logger logger = LoggerFactory.getLogger(AController.class);

    @Autowired
    private GradesDao gradesDao;
//@Resource
//      private  ClassPathXmlApplicationContext classPathXmlApplicationContext;






    @RequestMapping("getString")
    public Object getString(){

        AtomicInteger a = new AtomicInteger(0);

        List<Grades> grades = gradesDao.selectList(null);
        List<Map<String, Object>> maps = gradesDao.selectMaps(null);


        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
       int year= instance.get(Calendar.YEAR);
        int month=  instance.get(Calendar.MONTH)+1;
        int day = instance.get(Calendar.DAY_OF_MONTH);
        logger.info("time"+year+":"+month+":"+day);

        return maps;
    }
}
