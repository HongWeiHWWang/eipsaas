package poi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import poi.Excel;
import poi.editor.IFontEditor;
import poi.style.Align;
import poi.style.BorderStyle;
import poi.style.Color;
import poi.style.font.BoldWeight;
import poi.style.font.Font;

import com.hotent.base.util.FileUtil;

/**
 * Excel工具方法
 *
 * @company 广州宏天软件股份有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年7月5日
 */
public class ExcelUtil {

	/**
	 * 获取工作表的行数
	 *
	 * @param sheet
	 *            HSSFSheet表对象
	 * @return 表行数
	 */
	public static int getLastRowNum(HSSFSheet sheet) {
		int lastRowNum = sheet.getLastRowNum();
		if (lastRowNum == 0) {
			lastRowNum = sheet.getPhysicalNumberOfRows() - 1;
		}
		return lastRowNum;
	}

	/**
	 * 获取该行第一个单元格的下标
	 *
	 * @param row
	 *            行对象
	 * @return 第一个单元格下标，从0开始
	 */
	public static int getFirstCellNum(HSSFRow row) {
		return row.getFirstCellNum();
	}

	/**
	 * 获取该行最后一个单元格的下标
	 *
	 * @param row
	 *            行对象
	 * @return 最后一个单元格下标，从0开始
	 */
	public static int getLastCellNum(HSSFRow row) {
		return row.getLastCellNum();
	}

	/**
	 * 获取POI的行对象
	 *
	 * @param sheet
	 *            表对象
	 * @param row
	 *            行号，从0开始
	 * @return
	 */
	public static HSSFRow getHSSFRow(HSSFSheet sheet, int row) {
		if (row < 0) {
			row = 0;
		}
		HSSFRow r = sheet.getRow(row);
		if (r == null) {
			r = sheet.createRow(row);
		}
		return r;
	}

	/**
	 * 获取单元格对象
	 *
	 * @param sheet
	 *            表对象
	 * @param row
	 *            行，从0开始
	 * @param col
	 *            列，从0开始
	 * @return row行col列的单元格对象
	 */
	public static HSSFCell getHSSFCell(HSSFSheet sheet, int row, int col) {
		HSSFRow r = getHSSFRow(sheet, row);
		return getHSSFCell(r, col);
	}

	/**
	 * 获取单元格对象
	 *
	 * @param row
	 *            行，从0开始
	 * @param col
	 *            列，从0开始
	 * @return 指定行对象上第col行的单元格
	 */
	public static HSSFCell getHSSFCell(HSSFRow row, int col) {
		if (col < 0) {
			col = 0;
		}
		HSSFCell c = row.getCell(col);
		c = c == null ? row.createCell(col) : c;
		return c;
	}

	/**
	 * 获取工作表对象
	 *
	 * @param workbook
	 *            工作簿对象
	 * @param index
	 *            表下标，从0开始
	 * @return
	 */
	public static HSSFSheet getHSSFSheet(HSSFWorkbook workbook, int index) {
		if (index < 0) {
			index = 0;
		}
		if (index > workbook.getNumberOfSheets() - 1) {
			workbook.createSheet();
			return workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
		} else {
			return workbook.getSheetAt(index);
		}
	}

	/**
	 * 下载文件
	 *
	 * @param workBook
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */

	public static void downloadExcel(HSSFWorkbook workBook, String fileName, HttpServletResponse response) throws IOException {

		String filedisplay = URLEncoder.encode(fileName+ ".xls", "utf-8");
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
		response.addHeader("filename", filedisplay);
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				os.close();
		}
	}

	/**
	 * 导出excel文件。

	 *
	 * @param title
	 *            excel表格名称
	 * @param rowHeight
	 *            行高
	 * @param fieldMap
	 *            字段名映射 为一个LinkedHashMap
	 * @param data
	 *            行数据
	 *<pre>
	 * fieldMap的键要与data集合里map的键对应
	 * Map<String, String> fieldMap = new LinkedHashMap<String, String>();
	 *	fieldMap.put("A1", "标题1");
	 *	fieldMap.put("A2", "标题2");
	 *	fieldMap.put("A3", "标题3");
	 *	fieldMap.put("A4", "标题4");
	 *	fieldMap.put("A5", "标题5");
	 *	fieldMap.put("A6", "标题6");
	 *	List<Map<String, String>> data=new ArrayList<Map<String, String>>();
	 *	Map<String, String> m1=new HashMap<String, String>();
	 *	m1.put("A1", "LJ1");
	 *	m1.put("A2", "LJ2");
	 *	m1.put("A3", "LJ3");
	 *	m1.put("A4", "LJ4");
	 *	m1.put("A5", "LJ5");
	 *	m1.put("A6", "LJ6");
	 *	data.add(m1);
	 * </pre>
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static HSSFWorkbook exportExcel(String title, int rowHeight, Map<String, String> fieldMap, List data) throws Exception {

		int size = fieldMap.size();
		Excel excel = new Excel();

		int titleCols = size; // 列数

		if (titleCols == 0) {
			throw new Exception("请设置列！");
		}

		// 设置页名
		excel.sheet().sheetName(title);// 重命名当前处于工作状态的表的名称

		int i = 0;
		// 设置表头，第二行开始
		for (String name : fieldMap.values()) { // 表头已经排序过了

			excel.cell(0, i).value(name).align(Align.CENTER) // 设置水平对齐方式
			.bgColor(Color.GREY_25_PERCENT) // 设置背景色
			.fontHeightInPoint(14).width(256 * 50)// 增加宽度
			.border(BorderStyle.THIN, Color.BLACK) // 设置外边框样式
			.font(new IFontEditor() { // 设置字体
				@Override
				public void updateFont(Font font) {
					font.boldweight(BoldWeight.BOLD);// 粗体
					font.color(Color.BLACK);// 字体颜色
				}
			});
			i++;
		}

		// 插入数据，第三行开始
		int rows = 1;
		for (Object obj : data) {
			Map rowObj = (Map) obj;
			int col = 0;
			for (String key : fieldMap.keySet()) {
				String val = rowObj.get(key) == null ? "" : rowObj.get(key).toString();
				excel.cell(rows, col).value(val).border(BorderStyle.MEDIUM, Color.BLACK) // 设置外边框样式
				.fontHeightInPoint(14).warpText(true).align(Align.LEFT); // 设置水平对齐方式
				col++;
			}
			rows++;
		}

		return excel.getWorkBook();
	}

	/**
	 * 导入excel文件。
	 * @param firstFile 文件流对象
	 * @return
	 */
	public static List<Map<String, String>> ImportDate(MultipartFile firstFile) {
		Workbook wb =null;
		Sheet sheet = null;
		Row row = null;
		List<Map<String,String>> list = null;
		String cellData = null;
		List<String> columns=new ArrayList<String>();
		wb = readExcel(firstFile);
		if(wb != null){
			//用来存放表中数据
			list = new ArrayList<Map<String,String>>();
			//获取第一个sheet
			sheet = wb.getSheetAt(0);
			//获取最大行数
			int rownum = sheet.getPhysicalNumberOfRows();
			//获取第一行
			row = sheet.getRow(0);
			//获取最大列数
			int colnum = row.getPhysicalNumberOfCells();
			for (int i = 0; i<rownum; i++) {
				Map<String,String> map = new LinkedHashMap<String,String>();
				row = sheet.getRow(i);
				if(i==0){
					for (int j=0;j<colnum;j++){
						cellData = (String) getCellFormatValue(row.getCell(j));
						columns.add(cellData);
					}
				}else{
					if(row !=null){
						for (int j=0;j<colnum;j++){
							cellData = (String) getCellFormatValue(row.getCell(j));
							map.put(columns.get(j), cellData);
						}
					}else{
						break;
					}
					list.add(map);
				}
			}
		}
		return list;
	}


	//读取excel
	public static Workbook readExcel(MultipartFile multipartFile){
		String filePath=multipartFile.getOriginalFilename();
		Workbook wb = null;
		if(filePath==null){
			return null;
		}
		String extString = filePath.substring(filePath.lastIndexOf("."));
		InputStream is = null;
		try {
			is = multipartFile.getInputStream();
			if(".xls".equals(extString)){
				return wb = new HSSFWorkbook(is);
			}else if(".xlsx".equals(extString)){
				return wb = new XSSFWorkbook(is);
			}else{
				return wb = null;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}

	public static Object getCellFormatValue(Cell cell){
		Object cellValue = null;
		if(cell!=null){
			//判断cell类型
			switch(cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:{
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			}
			case Cell.CELL_TYPE_FORMULA:{
				//判断cell是否为日期格式
				if(DateUtil.isCellDateFormatted(cell)){
					//转换为日期格式YYYY-mm-dd
					cellValue = cell.getDateCellValue();
				}else{
					//数字
					cellValue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
			case Cell.CELL_TYPE_STRING:{
				cellValue = cell.getRichStringCellValue().getString();
				break;
			}
			default:
				cellValue = "";
			}
		}else{
			cellValue = "";
		}
		return cellValue;
	}

	/**
	 * 下载文件
	 *
	 * @param workBook
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */

	public static void saveExcel(HSSFWorkbook workBook, String fileName,String path) throws IOException {
		FileUtil.createFolder(path, true);
		String excelName = fileName + ".xls";
		String filePath = path + File.separator + excelName;
		FileOutputStream fout = new FileOutputStream(filePath);
		workBook.write(fout);
		fout.flush();
		fout.close();
	}

}
