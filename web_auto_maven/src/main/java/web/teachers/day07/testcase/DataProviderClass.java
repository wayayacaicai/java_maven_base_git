package web.teachers.day07.testcase;

import org.testng.annotations.DataProvider;

public class DataProviderClass {
	/**
	 * 	 
	 手机号	         密码		重复密码	验证码		期望值
											用户名不能为空
	lemon									非法的手机号
	13888888888					LM19		密码不能为空
	13888888888	12345			LM19		密码长度至少为6位
	13888888888	123456			LM19		重复密码不能为空
	13888888888	123456	1234567	LM19    	密码不一致
	 * @return
	 */
	@DataProvider(name="data")
	public static Object[][] registerData(){
		return new Object[][]{
			{"","","","","用户名不能为空"},
			{"lemon","","","","非法的手机号"},
			{"柠檬班","","","","非法的手机号"},
			{"13888888888","","","LM19","密码不能为空"},
			{"13888888888","12345","","LM19","密码长度至少为6位"},
			{"13888888888","123456","","LM19","重复密码不能为空"},
			{"13888888888","123456","1234567","LM19","密码不一致"},
			{"13888888888","123456","123456","","验证码不能为空"},
		};
	}
}
