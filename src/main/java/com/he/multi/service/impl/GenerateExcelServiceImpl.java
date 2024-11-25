package com.he.multi.service.impl;

import com.he.multi.dao.GradesDao;
import com.he.multi.service.GenerateExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GenerateExcelServiceImpl implements GenerateExcelService {

    private static Logger logger = LoggerFactory.getLogger(GenerateExcelServiceImpl.class);

    @Autowired
    private GradesDao gradesDao;

    @Override
    public void generateExcel() {

        List<Map<String, Object>> maps = gradesDao.selectMaps(null);

        Workbook workbook = null;

        workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet("sheet1");

        Row row = null;
        Cell cell = null;

        int rows = -1;
        int cells = 0;
//        HSSFRow row =null;
//        HSSFCell cell=null;

        CellRangeAddress range = null;
        Integer cellIndex = null;

        List<String> stringList = new ArrayList<>();
        stringList.add("id");
        stringList.add("stu_no");
        stringList.add("grade");

      String[] dataField=  stringList.toArray(new String[stringList.size()]);

        List<String> titleList = new ArrayList<>();
        titleList.add("ID");
        titleList.add("学号");
        titleList.add("成绩");

        List<String> ss = new ArrayList<>();
        ss.add("aa");

        String[] title = titleList.toArray(new String[titleList.size()]);

// 标题
        for (int i = 0; i < ss.size(); i++) {
            if (rows < i) {
                rows = i;
                row = sheet.createRow(i);
                if (rows == 0) {
                    cellIndex = stringList.size() - 1;
                }
            }

            row.setHeight((short) 610);
            cell = row.createCell(0);
            cell.setCellValue("阅读报表3333!");
            range = new CellRangeAddress(0, 0, 0, stringList.size() - 1);
            sheet.addMergedRegion(range);


        }

        rows++;
        cells = 0;
        // 注释行
        for (int i = 0; i <  ss.size(); i++) {
//            if (rows < i) {
//                rows = i;
//                row = sheet.createRow(i);
//                if (rows == 0) {
//                    cellIndex = stringList.size() - 1;
//                }
//            }
            row = sheet.createRow(1);

//            row.setHeight((short) 610);
            cell = row.createCell(0);
            cell.setCellValue("注释说明!");
            range = new CellRangeAddress(1, 1, 0, stringList.size() - 1);
            sheet.addMergedRegion(range);


        }



        // 表头
        rows++;
        cells = 0;
        logger.info("表头rows:" + rows);
        if (title != null && title.length > 0) {
           row = sheet.createRow(rows);
            for (int i = 0; i < title.length; i++) {
                 cell = row.createCell(cells);
                cell.setCellValue(title[i]);
                logger.info(":"+i+"getRowNum()"+row.getRowNum()+":"+cells);
//                range =new CellRangeAddress(row.getRowNum(),row.getRowNum(),cells,cells);
//                sheet.addMergedRegion(range);
                cells++;
            }

        }


        // 数据
        rows++;
        cells = 0;

        for(Map<String,Object> map : maps ){

            row =sheet.createRow(rows);

            for(int i=0;i<dataField.length;i++){
                cell=row.createCell(cells);
                cell.setCellValue(String.valueOf(map.get(dataField[i])));
                cells++;
            }

           cells=0;
            rows++;

        }






        // 文件生成
        String path = "D:\\resources\\temp\\a.xls";

        File file = new File(path);
        try {

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
