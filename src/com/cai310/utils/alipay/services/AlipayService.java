package com.cai310.utils.alipay.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.cai310.utils.alipay.config.AlipayConfig;
import com.cai310.utils.alipay.util.AlipaySubmit;

/* *
 *类名：AlipayService
 *功能：支付宝各接口构造类
 *详细：构造支付宝各接口请求参数
 *版本：3.2
 *修改日期：2011-03-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayService {
    
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

    /**
     * 构造支付宝单笔交易查询接口
     * @param sParaTemp 请求参数集合
     * @return 表单提交HTML信息
     * @throws Exception 
     */
    public static String single_trade_query(Map<String, String> sParaTemp) throws Exception {

    	//增加基本配置
        sParaTemp.put("service", "single_trade_query");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);      

        return AlipaySubmit.sendPostInfo(sParaTemp,ALIPAY_GATEWAY_NEW);
    }
    public static Map<String, String> single_trade_query_my(Map<String, String> sParaTemp) throws Exception{
    	String sHtmlText = AlipayService.single_trade_query(sParaTemp);
    	Document doc = DocumentHelper.parseText(sHtmlText);
    	List<Node> list = doc.selectNodes("//response/trade/*");
		Map<String, String> response = new HashMap<String, String>();
		Map<String, String> returnMap = new HashMap<String, String>();
		for (Node node : list) {
			response.put(node.getName(), node.getText());
		}
		String out_trade_no = response.get("out_trade_no");
		String total_fee = response.get("total_fee");
		String trade_no = response.get("trade_no");
		String trade_status = response.get("trade_status");
		returnMap.put("out_trade_no", out_trade_no);
		returnMap.put("total_fee", total_fee);
		returnMap.put("trade_no", trade_no);
		returnMap.put("trade_status", trade_status);
		return returnMap;
    }

    /**
     * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
     * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
     * @return 时间戳字符串
     * @throws IOException
     * @throws DocumentException
     * @throws MalformedURLException
     */
    public static String query_timestamp() throws MalformedURLException,
                                                        DocumentException, IOException {

        //构造访问query_timestamp接口的URL串
        String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + AlipayConfig.partner;
        StringBuffer result = new StringBuffer();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new URL(strUrl).openStream());

        List<Node> nodeList = doc.selectNodes("//alipay/*");

        for (Node node : nodeList) {
            // 截取部分不需要解析的信息
            if (node.getName().equals("is_success") && node.getText().equals("T")) {
                // 判断是否有成功标示
                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
                for (Node node1 : nodeList1) {
                    result.append(node1.getText());
                }
            }
        }

        return result.toString();
    }
}
