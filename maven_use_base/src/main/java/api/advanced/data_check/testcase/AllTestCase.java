package api.advanced.data_check.testcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.advanced.data_check.pojo.ApiCaseDetail;
import api.advanced.data_check.pojo.CellData;
import api.advanced.data_check.utils.ApiUtils;
import api.advanced.data_check.utils.DataCheckUtils;
import api.advanced.data_check.utils.ExcelUtils;
import api.advanced.data_check.utils.HttpUtils;



/**
 * 当excel结构变化：增加、删除列、顺序调整 2：大量的冗余，不好维护--》怎么解决冗余，重复的
 * 数据库冗余：一个信息系统所有数据都放到一个表中--》通过多个表、设计关联字段解决的
 * 
 * @Desc:结合json数据和反射进行请求(执行测试用例)
 * @author:zpp
 * @time:2019年4月1日 下午9:08:57
 */
public class AllTestCase {
	private static String sourceExcelPath;
	private static String targetExcelPath;
	private static int sheetIndex1;
	private static int cellNo;
	private static Logger logger = Logger.getLogger(AllTestCase.class);

	static {
		try {
			Properties properties = new Properties();
			InputStream is = new FileInputStream(
					new File("src/main/java/api/advanced/data_check/api_info.properties"));
			properties.load(is);
			sourceExcelPath = properties.getProperty("sourceExcelPath");
			targetExcelPath = properties.getProperty("targetExcelPath");
			sheetIndex1 = Integer.parseInt(properties.getProperty("sheetIndex1"));
			cellNo = Integer.parseInt(properties.getProperty("cellNo"));
		} catch (Exception e) {
			logger.error("配置文件读取出现异常" + e.getMessage());
		}
	}

	@DataProvider
	public Object[][] getDatas() {
		return ApiUtils.getDatas();
	}

	// @Test(dataProvider = "getDatas")
	// 每一个apiCaseDetail就是一条测试用例，测试用例有一条对应得接口信息
	// 接口信息是接口测试用例的属性
	public static void get(ApiCaseDetail apiCaseDetail) {
		String entityStr = HttpUtils.request(apiCaseDetail);
		Assert.assertTrue(entityStr.contains(apiCaseDetail.getExpectedReponseData()));
	}

	
	@Test(dataProvider = "getDatas")
	public static void post(ApiCaseDetail apiCaseDetail) {
		//1.前置验证
		DataCheckUtils.beforeCheck(apiCaseDetail);
		
		String responseData = HttpUtils.request(apiCaseDetail);
		
		//2.后置验证
		DataCheckUtils.afterCheck(apiCaseDetail);
		
		//收集数据
		CellData cellData = new CellData(apiCaseDetail.getRowNo(), 6, responseData);
		ApiUtils.addCellData(cellData);
		
		// 回写数据
//		ExcelUtils.writeExcel(sourceExcelPath, targetExcelPath, sheetIndex1, apiCaseDetail.getCaseId(), cellNo,
//				entityStr);
		// Assert.assertTrue(entityStr.contains(apiCaseDetail.getExpectedReponseData()));
	}

	@AfterSuite
	public void afterSuite() {
		// 一次性数据回写--一次性把数据收集好
//		List<CellData> cellDataList = ApiUtils.getcellDataList();
//		ExcelUtils.writeExcel(sourceExcelPath, "d:/data.xlsx", sheetIndex1, cellDataList);
//		
//		System.out.println("-------");
//		
//		List<CellData> sqlCellData = ApiUtils.getSqlCellDataList();
//		ExcelUtils.writeExcel(sourceExcelPath, "d:/data_check.xlsx", 2, sqlCellData);
		
		List<CellData> cellDataList = ApiUtils.getcellDataList();
		List<CellData> sqlCellData = ApiUtils.getSqlCellDataList();
		ExcelUtils.writeAllExcel(sourceExcelPath,  "d:/data_check_all.xlsx",  cellDataList, sqlCellData);
	}
}