package org.javaboy.vhr.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.javaboy.vhr.model.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class POIUtils {
    public static ResponseEntity<byte[]> employee2Excel(List<Employee> list){
        //1.创建一个excel文档
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook();
        //2.创建文档摘要
        hssfWorkbook.createInformationProperties();
        //3.获取并配置文档信息
        DocumentSummaryInformation docInfo = hssfWorkbook.getDocumentSummaryInformation();
        docInfo.setCategory("员工信息");
        docInfo.setManager("xjh");
        docInfo.setCompany("XJH");
        //4.获取文档摘要信息
        SummaryInformation summInfo = hssfWorkbook.getSummaryInformation();
        //文档标题
        summInfo.setTitle("员工信息表");
        //文档作者
        summInfo.setAuthor("徐俊辉");
        //文档备注
        summInfo.setComments("备注");

        //创建样式
        //标题行样式
        HSSFCellStyle headerStyle=hssfWorkbook.createCellStyle();
        headerStyle.setFillBackgroundColor(IndexedColors.WHITE.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //日期样式
        HSSFCellStyle dateCellStyle=hssfWorkbook.createCellStyle();
        dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        //6.创建标题行
        //先创建表单
        HSSFSheet sheet = hssfWorkbook.createSheet("员工信息表");
        //设置列宽
        sheet.setColumnWidth(0,5*256);
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(2,10*256);
        sheet.setColumnWidth(3,5*256);
        sheet.setColumnWidth(4,12*256);
        sheet.setColumnWidth(5,20*256);
        sheet.setColumnWidth(6,10*256);
        sheet.setColumnWidth(7,10*256);
        sheet.setColumnWidth(8,16*256);
        sheet.setColumnWidth(9,12*256);
        sheet.setColumnWidth(10,15*256);
        sheet.setColumnWidth(11,20*256);
        sheet.setColumnWidth(12,16*256);
        sheet.setColumnWidth(13,14*256);
        sheet.setColumnWidth(14,14*256);
        sheet.setColumnWidth(15,12*256);
        sheet.setColumnWidth(16,8*256);
        sheet.setColumnWidth(17,20*256);
        sheet.setColumnWidth(18,20*256);
        sheet.setColumnWidth(19,15*256);
        sheet.setColumnWidth(20,8*256);
        sheet.setColumnWidth(21,25*256);
        sheet.setColumnWidth(22,14*256);
        sheet.setColumnWidth(23,15*256);
        sheet.setColumnWidth(24,15*256);
        sheet.setColumnWidth(25,15*256);
        //创建标题行
        HSSFRow r0 = sheet.createRow(0);
        //创建列1
        HSSFCell c0 = r0.createCell(0);
        c0.setCellValue("编号");
        c0.setCellStyle(headerStyle);
        //创建列2
        HSSFCell c1 = r0.createCell(1);
        c1.setCellValue("姓名");
        c1.setCellStyle(headerStyle);
        HSSFCell c2 = r0.createCell(2);
        c2.setCellValue("工号");
        c2.setCellStyle(headerStyle);
        HSSFCell c3 = r0.createCell(3);
        c3.setCellValue("性别");
        c3.setCellStyle(headerStyle);
        HSSFCell c4 = r0.createCell(4);
        c4.setCellValue("出生日期");
        c4.setCellStyle(headerStyle);
        HSSFCell c5 = r0.createCell(5);
        c5.setCellValue("身份证号码");
        c5.setCellStyle(headerStyle);
        HSSFCell c6 = r0.createCell(6);
        c6.setCellValue("婚宴状况");
        c6.setCellStyle(headerStyle);
        HSSFCell c7 = r0.createCell(7);
        c7.setCellValue("民族");
        c7.setCellStyle(headerStyle);
        HSSFCell c8 = r0.createCell(8);
        c8.setCellValue("籍贯");
        c8.setCellStyle(headerStyle);
        HSSFCell c9 = r0.createCell(9);
        c9.setCellValue("政治面貌");
        c9.setCellStyle(headerStyle);
        HSSFCell c10 = r0.createCell(10);
        c10.setCellValue("电话号码");
        c10.setCellStyle(headerStyle);
        HSSFCell c11 = r0.createCell(11);
        c11.setCellValue("联系地址");
        c11.setCellStyle(headerStyle);
        HSSFCell c12 = r0.createCell(12);
        c12.setCellValue("所属部门");
        c12.setCellStyle(headerStyle);
        HSSFCell c13 = r0.createCell(13);
        c13.setCellValue("职位");
        c13.setCellStyle(headerStyle);
        HSSFCell c14 = r0.createCell(14);
        c14.setCellValue("职称");
        c14.setCellStyle(headerStyle);
        HSSFCell c15 = r0.createCell(15);
        c15.setCellValue("聘用形式");
        c15.setCellStyle(headerStyle);
        HSSFCell c16 = r0.createCell(16);
        c16.setCellValue("最高学历");
        c16.setCellStyle(headerStyle);
        HSSFCell c17 = r0.createCell(17);
        c17.setCellValue("专业");
        c17.setCellStyle(headerStyle);
        HSSFCell c18 = r0.createCell(18);
        c18.setCellValue("毕业院校");
        c18.setCellStyle(headerStyle);
        HSSFCell c19 = r0.createCell(19);
        c19.setCellValue("入职日期");
        c19.setCellStyle(headerStyle);
        HSSFCell c20 = r0.createCell(20);
        c20.setCellValue("在职状态");
        c20.setCellStyle(headerStyle);
        HSSFCell c21 = r0.createCell(21);
        c21.setCellValue("邮箱");
        c21.setCellStyle(headerStyle);
        HSSFCell c22 = r0.createCell(22);
        c22.setCellValue("合同期限（年）");
        c22.setCellStyle(headerStyle);
        HSSFCell c23 = r0.createCell(23);
        c23.setCellValue("合同起始日期");
        c23.setCellStyle(headerStyle);
        HSSFCell c24 = r0.createCell(24);
        c24.setCellValue("合同截止日期");
        c24.setCellStyle(headerStyle);
        HSSFCell c25 = r0.createCell(25);
        c25.setCellStyle(headerStyle);
        c25.setCellValue("转正日期");

        //遍历集合放入数据，上面只是设置了标题行
        for(int i=0;i<list.size();i++){
            Employee employee = list.get(i);
            HSSFRow row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(employee.getId());
            row.createCell(1).setCellValue(employee.getName());
            row.createCell(2).setCellValue(employee.getWorkID());
            row.createCell(3).setCellValue(employee.getGender());
            //日期要设置样式，所以拆开来写了
            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(employee.getBirthday());
            cell4.setCellStyle(dateCellStyle);
            row.createCell(5).setCellValue(employee.getIdCard());
            row.createCell(6).setCellValue(employee.getWedlock());
            row.createCell(7).setCellValue(employee.getNation().getName());
            row.createCell(8).setCellValue(employee.getNativePlace());
            row.createCell(9).setCellValue(employee.getPoliticsstatus().getName());
            row.createCell(10).setCellValue(employee.getPhone());
            row.createCell(11).setCellValue(employee.getAddress());
            row.createCell(12).setCellValue(employee.getDepartment().getName());
            row.createCell(13).setCellValue(employee.getPosition().getName());
            row.createCell(14).setCellValue(employee.getJobLevel().getName());
            row.createCell(15).setCellValue(employee.getEngageForm());
            row.createCell(16).setCellValue(employee.getTiptopDegree());
            row.createCell(17).setCellValue(employee.getSpecialty());
            row.createCell(18).setCellValue(employee.getSchool());
            HSSFCell cell19 = row.createCell(19);
            cell19.setCellValue(employee.getBeginDate());
            cell19.setCellStyle(dateCellStyle);
            row.createCell(20).setCellValue(employee.getWorkState());
            row.createCell(21).setCellValue(employee.getEmail());
            row.createCell(22).setCellValue(employee.getContractTerm());
            HSSFCell cell23 = row.createCell(23);
            cell23.setCellValue(employee.getBeginContract());
            cell23.setCellStyle(dateCellStyle);
            HSSFCell cell24 = row.createCell(24);
            cell24.setCellValue(employee.getEndContract());
            cell24.setCellStyle(dateCellStyle);
            HSSFCell cell25 = row.createCell(25);
            cell25.setCellValue(employee.getConversionTime());
            cell25.setCellStyle(dateCellStyle);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //响应头
        HttpHeaders  headers=new HttpHeaders();
        try {

            //attachment表示以附件形式下载，filename为下载的文件名
            //ew String("员工表.xls".getBytes("UTF-8"),"ISO-8859-1")为了避免乱码问题，.getBytes()表示以操作系统默认方式编码，再以ISO-8859-1方式解码以适应浏览器
            headers.setContentDispositionFormData("attachment",new String("员工表.xls".getBytes("UTF-8"),"ISO-8859-1"));
            //application/octet-stream ： 二进制流数据
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //以流的形式写出
            hssfWorkbook.write(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把响应头，数据，状态作为http响应
        return new ResponseEntity<byte[]>(baos.toByteArray(),headers, HttpStatus.CREATED);

    }

    //nation要查出民族后查出id
    public static List<Employee>excel2Employee(MultipartFile file, List<Nation>allNations, List<Politicsstatus>allPoliticsstatus,
                                               List<Department>allDepartment, List<Position>allPositions,List<JobLevel>allJobLevels){
        List<Employee>list=new ArrayList<>();
        Employee employee=null;
        try {
            //1.创建一个hssfWorkbook对象
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(file.getInputStream());
            //2.获取表单数量
            int numberOfSheets = hssfWorkbook.getNumberOfSheets();
            for (int i = 0; i <numberOfSheets ; i++) {
                //3,获取表单
                HSSFSheet sheet = hssfWorkbook.getSheetAt(i);
                //4.获取表单的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j <physicalNumberOfRows ; j++) {
                    //防止标题行
                    if (j==0){
                        continue;
                    }
                    HSSFRow row = sheet.getRow(j);
                    //防止空行
                    if (row==null){
                        continue;
                    }

                    //获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    employee=new Employee();
                    for (int k = 1; k <physicalNumberOfCells ; k++) {
                        HSSFCell cell = row.getCell(k);
                        switch (cell.getCellType()){
                            case STRING:
                                String cellValue = cell.getStringCellValue();
                                switch (k){
                                    case 1:
                                        employee.setName(cellValue);
                                        break;
                                    case 2:
                                        employee.setWorkID(cellValue);
                                        break;
                                    case 3:
                                        employee.setGender(cellValue);
                                        break;
                                    case 5:
                                        employee.setIdCard(cellValue);
                                        break;
                                    case 6:
                                        employee.setWedlock(cellValue);
                                        break;
                                    case 7:
                                        //逻辑就是重写了Nation的hashcode方法，使得两个nation对象只要name相同即可，不需要id一样，这样可以得到allNations集合中nation的index，
                                        //再根据index得到nation对象，在得到nation对象的id
                                        //那为啥不直接得到nation的id，而要通过求list的index呢，因为cellValue是一个字符串不是一个对象
                                        //强调重写hashcode，要从集合中找对象，只知道name，所以需要户视id的影响
                                        int nationIndex = allNations.indexOf(new Nation(cellValue));
                                        employee.setNationId(allNations.get(nationIndex).getId());
                                        break;
                                    case 8:
                                        employee.setNativePlace(cellValue);
                                        break;
                                    case 9:
                                        int poliIndex = allPoliticsstatus.indexOf(new Politicsstatus(cellValue));
                                        employee.setPoliticId(allPoliticsstatus.get(poliIndex).getId());
                                        break;
                                    case 10:
                                        employee.setPhone(cellValue);
                                        break;
                                    case 11:
                                        employee.setAddress(cellValue);
                                        break;
                                    case 12:
                                        int departmentIndex = allDepartment.indexOf(new Department(cellValue));
                                        employee.setDepartmentId(allDepartment.get(departmentIndex).getId());
                                        break;
                                    case 13:
                                        int positionIndex = allPositions.indexOf(new Position(cellValue));
                                        employee.setPosId(allPositions.get(positionIndex).getId());
                                        break;
                                    case 14:
                                        int jobLevelIndex = allJobLevels.indexOf(new JobLevel(cellValue));
                                        employee.setJobLevelId(allJobLevels.get(jobLevelIndex).getId());
                                        break;
                                    case 15:
                                        employee.setEngageForm(cellValue);
                                        break;
                                    case 16:
                                        employee.setTiptopDegree(cellValue);
                                        break;
                                    case 17:
                                        employee.setSpecialty(cellValue);
                                        break;
                                    case 18:
                                        employee.setSchool(cellValue);
                                        break;
                                    case 20:
                                        employee.setWorkState(cellValue);
                                        break;
                                    case 21:
                                        employee.setEmail(cellValue);
                                        break;
                                }
                                break;
                            default:{
                                switch (k){
                                    case 4:
                                        employee.setBirthday(cell.getDateCellValue());
                                        break;
                                    case 19:
                                        employee.setBeginDate(cell.getDateCellValue());
                                        break;
                                    case 23:
                                        employee.setBeginContract(cell.getDateCellValue());
                                        break;
                                    case 24:
                                        employee.setEndContract(cell.getDateCellValue());
                                        break;
                                    case 22:
                                        employee.setContractTerm(cell.getNumericCellValue());
                                        break;
                                    case 25:
                                        employee.setConversionTime(cell.getDateCellValue());
                                        break;
                                }
                            }
                            break;
                        }
                    }
                    list.add(employee);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
