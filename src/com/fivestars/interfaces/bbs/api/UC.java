package com.fivestars.interfaces.bbs.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.cai310.lottery.entity.user.User;
import com.cai310.lottery.entity.user.UserLogin;
import com.cai310.lottery.service.user.UserEntityManager;
import com.cai310.lottery.web.sso.SsoLoginHolder;
import com.cai310.spring.SpringContextHolder;
import com.cai310.utils.Struts2Utils;
import com.fivestars.interfaces.bbs.client.Client;

/**
 * 此类用来同步UC Server发出的操作指令
 * 可以根据业务需要添加相应的执行代码
 * 
 */
public class UC extends HttpServlet{

	private static final long serialVersionUID = -7377364931916922413L;
	
	public static boolean IN_DISCUZ= true;
	public static String UC_CLIENT_VERSION="1.5.0";	//note UCenter 版本标识
	public static String UC_CLIENT_RELEASE="20081031";

	public static boolean API_DELETEUSER=true;		//note 用户删除 API 接口开关
	public static boolean API_RENAMEUSER=true;		//note 用户改名 API 接口开关
	public static boolean API_GETTAG=true;		//note 获取标签 API 接口开关
	public static boolean API_SYNLOGIN=true;		//note 同步登录 API 接口开关
	public static boolean API_SYNLOGOUT=true;		//note 同步登出 API 接口开关
	public static boolean API_UPDATEPW=true;		//note 更改用户密码 开关
	public static boolean API_UPDATEBADWORDS=true;	//note 更新关键字列表 开关
	public static boolean API_UPDATEHOSTS=true;		//note 更新域名解析缓存 开关
	public static boolean API_UPDATEAPPS=true;		//note 更新应用列表 开关
	public static boolean API_UPDATECLIENT=true;		//note 更新客户端缓存 开关
	public static boolean API_UPDATECREDIT=true;		//note 更新用户积分 开关
	public static boolean API_GETCREDITSETTINGS=true;	//note 向 UCenter 提供积分设置 开关
	public static boolean API_GETCREDIT=true;		//note 获取用户的某项积分 开关
	public static boolean API_UPDATECREDITSETTINGS=true;	//note 更新应用积分设置 开关

	public static String API_RETURN_SUCCEED   =    "1";
	public static String API_RETURN_FAILED    =   "-1";
	public static String API_RETURN_FORBIDDEN =   "-2";
	
	private UserEntityManager userEntityManager;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result = doAnswer(request, response);
		response.getWriter().print(result);
	}
	
	/**
	 * 执行具体的Action
	 * 所有服务器发出的参数均可通过$get来获得。
	 * 注意： request本身是不能得到参数值的。
	 * 
	 * @param request
	 * @param response
	 * @return 操作状态或操作结果
	 * @throws UnsupportedEncodingException 
	 */
	private String doAnswer(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		//处理
		String $code = request.getParameter("code");
		if($code==null) return API_RETURN_FAILED;				
		
		Map<String,String> $get = new HashMap<String, String>();
		$code = new Client().uc_authcode($code, "DECODE");
		$code = new String($code.getBytes("GBK"),"UTF-8");
		parse_str($code, $get);

		if($get.isEmpty()) {
			return "Invalid Request";
		}
		if(time() - tolong($get.get("time")) > 3600) {
			return "Authracation has expiried";
		}

		String $action = $get.get("action");
		if($action==null) return API_RETURN_FAILED;
		
		if($action.equals("test")) {

			return API_RETURN_SUCCEED;

		} else if($action.equals("deleteuser")) {


			return API_RETURN_SUCCEED;

		} else if($action.equals("renameuser")) {

			return API_RETURN_SUCCEED;

		} else if($action.equals("gettag")) {

			if(!API_GETTAG ) return API_RETURN_FORBIDDEN;
			
			//同步代码
			
			return API_RETURN_SUCCEED;
			

		} else if($action.equals("synlogin")) {
			if(!API_SYNLOGIN ) return (API_RETURN_FORBIDDEN);
			//实现网站同步登录
			userEntityManager = SpringContextHolder.getBean("userEntityManager");
			User loginUser = userEntityManager.getUserBy($get.get("username"));
//			SsoLoginHolder.login(loginUser.getId(), request, response);
			Cookie c = SsoLoginHolder.getLoginCookie(loginUser);
			System.out.println(c.getName());
			//记录登录信息
			UserLogin userLogin = userEntityManager.getUserLoginBy(loginUser.getId());		
			userLogin.setLastLoginIp(userLogin.getTryIp());
			userLogin.setLastLoginTime(userLogin.getTryTime());
			userLogin.setTryIp(Struts2Utils.getRemoteAddr(request));
			userLogin.setTryTime(new Date());
			userEntityManager.saveUserLogin(userLogin);
			
			response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
		//	int $cookietime = 31536000;
		//	Cookie user = new Cookie("loginuser",$get.get("username"));
		//	user.setMaxAge($cookietime);
		//	response.addCookie(user);

		} else if($action.equals("synlogout")) {
//			SsoLoginHolder.logout(request, response);
			if(!API_SYNLOGOUT ) return (API_RETURN_FORBIDDEN);
			
			
			//note 同步登出 API 接口
			//obclean();
			response.addHeader("P3P"," CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

			//clearcookie();
			Cookie user = new Cookie("loginuser","");
			user.setMaxAge(0);
			response.addCookie(user);

		} else if($action.equals("updateclient")) {
			if(!API_UPDATECLIENT ) return API_RETURN_FORBIDDEN;

			
			//同步代码
			
			return API_RETURN_SUCCEED;

		} else if($action.equals("updatepw")) {
			if(!API_UPDATEPW) return API_RETURN_FORBIDDEN;
			
			//同步代码
			
			return API_RETURN_SUCCEED;

		} else if($action.equals("updatebadwords")) {

			if(!API_UPDATEBADWORDS) return API_RETURN_FORBIDDEN;
			
			//同步代码
			
			return API_RETURN_SUCCEED;

		} else if($action.equals("updatehosts")) {

			if(!API_UPDATEHOSTS ) return API_RETURN_FORBIDDEN;


			return API_RETURN_SUCCEED;

		} else if($action.equals("updateapps")) {

			if(!API_UPDATEAPPS ) return API_RETURN_FORBIDDEN;


			return API_RETURN_SUCCEED;

		} else if($action.equals("updatecredit")) {

			//if(!UPDATECREDIT ) return API_RETURN_FORBIDDEN;

			return API_RETURN_SUCCEED;

		} else if($action.equals("getcreditsettings")) {

			//if(!GETCREDITSETTINGS ) return API_RETURN_FORBIDDEN;

			return "";//积分值

		} else if($action.equals("updatecreditsettings")) {

			if(!API_UPDATECREDITSETTINGS) return API_RETURN_FORBIDDEN;

			
			//同步代码
			
			return API_RETURN_SUCCEED;

		} else {

			return (API_RETURN_FORBIDDEN);

		}		
		return "";
	}

	private void parse_str(String str, Map<String,String> sets){
		if(str==null||str.length()<1) 
			return;
		String[] ps = str.split("&");
		for(int i=0;i<ps.length;i++){
			String[] items = ps[i].split("=");
			if(items.length==2){
				sets.put(items[0], items[1]);
			}else if(items.length ==1){
				sets.put(items[0], "");
			}
		}
	}
	
	protected long time(){
		return System.currentTimeMillis()/1000;
	}
	
    private static long tolong(Object s){
        if(s!=null){
            String ss = s.toString().trim();
            if(ss.length()==0){
                return 0L;
            }else{
                return Long.parseLong(ss);
            }
        }else{
            return 0L;
        }
    }

}
