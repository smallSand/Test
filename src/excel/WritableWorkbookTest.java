package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class WritableWorkbookTest {

	public static void main(String[] args) throws Exception {
		creatEXCEL();
		//readEXCEL();
	}

	public static void readEXCEL() throws Exception{
		  InputStream instream = new FileInputStream("d:/text.xls");   
		  Workbook  readwb = Workbook.getWorkbook(instream); 
		  Sheet readsheet = readwb.getSheet(0); 
          //获取Sheet表中所包含的总列数   
          int rsColumns = readsheet.getColumns();   
          //获取Sheet表中所包含的总行数   
          int rsRows = readsheet.getRows();   
          //获取指定单元格的对象引用   
          for (int i = 0; i < rsRows; i++)   
          { 
              for (int j = 0; j < rsColumns; j++)   
              {  
                  Cell cell = readsheet.getCell(j, i);
                  System.out.print(cell.getContents() + " ");   
              }   
              System.out.println();   
          }
	}
	public static void creatEXCEL() throws Exception{
		WritableWorkbook workbook = Workbook.createWorkbook(new File("d:/text.xls"));
		//第一页
        WritableSheet sheet = workbook.createSheet("第1页", 0);
        //表头
        String[] keys = {"公司名称","产品名称","第一级分类","第二级分类"};
        for (int i = 0; i < keys.length; i++)
        {
        	for(int j=0;j<keys.length;j++){
                Label firstrow = new Label(i, j, keys[i]);
                sheet.addCell(firstrow);
        	}
        }
        workbook.write();
        workbook.close();
	}
}
