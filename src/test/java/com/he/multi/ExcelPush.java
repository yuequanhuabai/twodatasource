//package com.he.multi;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.Iterator;
//
//public class ExcelPush {
//
//
//    public static void main(String[] args) {
//        String url = "jdbc:mysql://localhost:3306/test_db";
//        String username="root";
//        String password="root";
//
//
//        String excelFilePath="D:\\Users\\YuequanHuabai\\Desktop\\student.xlsx";
//
//        int batchSize =1000;
//        int count =0;
//
//
//        try( Connection conn = DriverManager.getConnection(url,username,password)){
//
//            try {
//                FileInputStream inputStream = new FileInputStream(excelFilePath);
//               Workbook workbook = new XSSFWorkBook(inputStream);
//                Sheet sheet = workbook.getSheetAt(0);
//                Iterator<Row> iterator = sheet.iterator();
//                // 设置为自动提交
//                conn.setAutoCommit(false);
//
//                String sql = "insert into student (id,name,score) values(?,?,?)";
//
//                PreparedStatement statement = conn.prepareStatement(sql);
//
//                while (iterator.hasNext()){
//                    Row next = iterator.next();
//                    Iterator<Cell> cellIterator = next.cellIterator();
//                    while (cellIterator.hasNext()){
//                        Cell next1 = cellIterator.next();
//                        int columnIndex = next1.getColumnIndex();
//                        switch (columnIndex){
//                            case 0:
//                                int id = (int)next1.getNumericCellValue();
//                                statement.setInt(1,id);
//                                break;
//                            case 1:
//                                String name = next1.getStringCellValue();
//                                statement.setString(2,name);
//                                break;
//                            case 2:
//                                double score = next1.getNumericCellValue();
//                                statement.setDouble(3,score);
//                                break;
//                        }
//                    }
//
//                    statement.addBatch();
//                    if(count % batchSize ==0){
//                        statement.executeBatch();
//                    }
//
//                }
//
//                workbook.close();
//                statement.executeBatch();
//                conn.commit();
//
//                // 最后复原
//                conn.setAutoCommit(true);
//
//
//
//            }
//            catch (IOException e1) {
//                e1.printStackTrace();
//            }
//            catch (FileNotFoundException e2) {
//                e2.printStackTrace();
//            }
//        }catch (SQLException e3){
//            e3.printStackTrace();
//        }
//
//
//    }
//}
