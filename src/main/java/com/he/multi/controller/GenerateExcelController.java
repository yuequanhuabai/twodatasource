package com.he.multi.controller;


import com.he.multi.service.GenerateExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/b/")
public class GenerateExcelController {

    @Autowired
    private GenerateExcelService generateExcelService;

    @RequestMapping("generateExcel")
    public String generateExcel(){

        generateExcelService.generateExcel();

        return "生成excelok!";
    }


}
