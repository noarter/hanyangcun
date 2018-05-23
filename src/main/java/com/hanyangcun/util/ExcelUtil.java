package com.hanyangcun.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelUtil<T> {
    // 表格标题
    private String sheetTitle = "Sheet1";
    // 列标题及列宽
    private String[][] headers = {};
    // 数据集
    private Collection<T> dataSets;
    // 日期输出格式
    private String dateFormat = "yyyy-MM-dd";
    // 输出流
    private OutputStream out = null;
    // 图片行行高
    public static int PIC_LINE_HEIGHT = 60;

    public ExcelUtil() {
        super();
    }

    public ExcelUtil(Collection<T> dataSets, OutputStream out) {
        super();
        this.dataSets = dataSets;
        this.out = out;
    }

    public ExcelUtil(String sheetTitle, Collection<T> dataSets, OutputStream out) {
        this(dataSets, out);
        this.sheetTitle = sheetTitle;
    }

    public ExcelUtil(String[][] headers, Collection<T> dataSets, OutputStream out) {
        this(dataSets, out);
        this.headers = headers;
    }

    public ExcelUtil(String sheetTitle, String[][] headers, Collection<T> dataSets, OutputStream out) {
        this(sheetTitle, dataSets, out);
        this.headers = headers;
    }

    public ExcelUtil(String sheetTitle, String[][] headers, Collection<T> dataSets, String dateFormat, OutputStream out) {
        this(sheetTitle, headers, dataSets, out);
        this.dateFormat = dateFormat;
    }

    /**
     * 利用JAVA的反射机制，将集合中的数据输出到指定IO流中
     * <p>
     * 如有图片,需将图片字段（byte）的顺序与表格中的图片列顺序对应
     *
     * @throws Exception 异常
     */
    public void ExportExcel() throws Exception {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 标题样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        // 设置水平居中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 标题字体
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontName("微软雅黑");
        titleFont.setColor(IndexedColors.BLACK.getIndex());
        titleFont.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        titleStyle.setFont(titleFont);
        // 正文样式
        HSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.cloneStyleFrom(titleStyle);
        // 正文字体
        HSSFFont bodyFont = workbook.createFont();
        bodyFont.setFontName("宋体");
        bodyFont.setColor(IndexedColors.BLACK.getIndex());
        bodyFont.setFontHeightInPoints((short) 12);
        bodyStyle.setFont(bodyFont);
        int index = 0;
        HSSFRow row = null;
        if (headers.length > 0) {
            // 产生表格标题行
            row = sheet.createRow(index++);
            // 设置行高
            row.setHeightInPoints(30f);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString(headers[i][0]);
                cell.setCellValue(text);
                cell.setCellStyle(titleStyle);
                // 设置列宽
                sheet.setColumnWidth(i, Integer.parseInt(headers[i][1]) * 256);
            }
        }
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataSets.iterator();
        while (it.hasNext()) {
            row = sheet.createRow(index);
            // 设置行高
            row.setHeightInPoints(25f);
            T t = it.next();
            // 利用反射，得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(bodyStyle);
                Field field = fields[i];
                field.setAccessible(true);
                Object value = field.get(t);
                // 判断值的类型后进行强制类型转换
                String textValue = null;
                if (value instanceof Date) {
                    Date date = (Date) value;
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                    textValue = sdf.format(date);
                } else if (value instanceof byte[]) {
                    // 设置图片行行高
                    row.setHeightInPoints(PIC_LINE_HEIGHT);
                    byte[] bsValue = (byte[]) value;
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) i, index, (short) i, index);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
                    patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                } else {
                    textValue = value.toString();
                }
                // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                if (textValue != null) {
                    Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                    Matcher matcher = p.matcher(textValue);
                    if (matcher.matches()) {
                        // 是数字当作double处理
                        cell.setCellValue(Double.parseDouble(textValue));
                    } else {
                        cell.setCellValue(textValue);
                    }
                }
            }
            index++;
        }
        workbook.write(out);
    }

    public static void main(String[] args) throws Exception {
        String[][] headers = {{"序号", "8"}, {"姓名", "20"}, {"年龄", "10"}, {"日期", "15"}};
        List<Student> dataSet = new ArrayList<Student>();
        dataSet.add(new Student(1l, "张三", 21, new Date()));
        dataSet.add(new Student(2l, "李四", 22, new Date()));
        dataSet.add(new Student(3l, "王五", 23, new Date()));

        // Excel输出地址
        OutputStream out = new FileOutputStream(new File("/Users/yyq/Downloads/test.xls"));
        new ExcelUtil<Student>("班级人员", headers, dataSet, out).ExportExcel();
        System.out.println("excel导出成功！");
    }
}

class Student {
    public Long id;
    public String name;
    public int age;
    public Date date;

    public Student() {
        super();
    }

    public Student(Long id, String name, int age) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Student(Long id, String name, int age, Date date) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.date = date;
    }
}
