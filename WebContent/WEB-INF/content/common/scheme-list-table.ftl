<#assign currUser=loggedUser!'' />
<script type="text/javascript">
	function schemeListOrder(type){
		var scheme_list_form = document.getElementById("scheme_list_form");
		var orderType =scheme_list_form["queryForm.orderType"];
		if(0==type){
			///方案金额
			if(orderType.value=="SCHEME_COST_DESC"){
				orderType.value="SCHEME_COST_ASC";
			}else{
				orderType.value="SCHEME_COST_DESC";
			}
		}else if(1==type){
		    if(orderType.value=="PROCESS_RATE_DESC"){
				orderType.value="PROCESS_RATE_ASC";
			}else{
				orderType.value="PROCESS_RATE_DESC";
			}
			///进度
		}else if(2==type){
			///发起时间
			if(orderType.value=="CREATE_TIME_DESC"){
				orderType.value="CREATE_TIME_ASC";
			}else{
				orderType.value="CREATE_TIME_DESC";
			}
		}
		scheme_list_form.submit();
		return false;
	}
	
	var subscribe_current = null;
	function submitSubscribeReq(schemeId) {
		if(subscribe_current != null){
			return;
		}
		var el = document.getElementById('subscribe_'+schemeId);
		if(!/\d+/.test(el.value)){
			alert('认购金额必须是整数');
			return;
		}
		var cost = parseInt(el.value,10);
		if(cost <= 0){
			alert('认购金额不能小于零');
			return;
		}
		
		$SSO.login_auth(function(){
			if (confirm('您确认要购买？')) {
				subscribe_current = schemeId;
				$.ajax({
					url : '<@c.url value="/${lotteryType.key}/scheme!subscribe.action" />',
					type : 'POST',
					cache : false,
					data : {
						"id" : schemeId,
						"subscribeForm.subscriptionCost" : cost,
						"ajax" : true
					},
					success : function(data, textStatus) {
						var jsonObj = toJsonObject(data);
						var msg = getCommonMsg(jsonObj);
						if (jsonObj.success == true) {
							alert(msg);
							if (jsonObj.redirectURL != null)
								window.location.href = jsonObj.redirectURL;
							else
								window.location.reload();
						} else {
							alert(msg);
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert('提交请求失败.');
						subscribe_current = null;
					},
					complete : function(XMLHttpRequest, textStatus) {
						subscribe_current = null;
					}
				});
			}
		});
	};
</script>
 <table width="100%" border="0" cellspacing="0" cellpadding="0" class="hmTable">
	<thead>
		<tr align="center">
		  <td width="15"  style="border-left: 0pt none;">序</td>
		  <td width="60"><a href="#" onclick="schemeListOrder(2);return false">方案编号</a></td>
		  <td width="80" align="left">发起人</td>
		  <td width="100">投注内容</td>
		  <#if lotteryType??&&lotteryType=='DLT'><td width="40"><a href="#">追加</a></td></#if>
		  <td width="40"><a href="#" onclick="schemeListOrder(0);return false">金额</a></td>
		  <td width="40">最低</br>认购</td>
		  <td width="55"><a href="#" onclick="schemeListOrder(1);return false">进度</a></td>
		  <td width="40">剩余</td>
		  <td width="100">购买</td>
		  <td width="65" style="border-right: 0pt none;">详细</td>
		</tr>
	</thead>
	<tbody>
      <#if pagination.result?? && pagination.result?size gt 0>
		<#list pagination.result as data>
		<#if data.keepTop!false><#assign styleStr='style="color:red"' /><#else><#assign styleStr='' /></#if>
		<tr class="row${(data_index+1)%2}" ${styleStr}  onmouseout="this.className=''" onmouseover="this.className='trhover'">
			<td align="center" style="border-left: 0pt none;"><#if data.keepTop!false>顶<#else>${data_index+1}</#if></td>
            <td  align="center"><a href="<@c.url value="/${data.lotteryType.key}/scheme-${data.schemeNumber}.html" />" <#if data.keepTop!false>style="color:red"</#if>>${data.schemeNumber}</a></td>
            <td align="left">${data.sponsorName}</td>
            <td align="left"> 
				<#if data.secretType=="FULL_PUBLIC">
					<#if data.mode=="SINGLE">
				      	<#if data.uploaded>
		                 	<a target="_blank" href="<@c.url value="/${data.lotteryType.key}/scheme!download.action?schemeNumber=${data.schemeNumber}"/>">下载</a>
		  				<#elseif currUser!='' && currUser.id == data.sponsorId>
		  					<a href="<@c.url value="/${scheme.lotteryType.key}/scheme!editUpload.action?schemeNumber=${data.schemeNumber}" />">上传方案内容</a>
		  				<#else>
		  					未上传
		  				</#if>         
					<#else>
					<#assign schemeContent = data.compoundContentTextForList!>
		               <#if schemeContent?length lt 18>   
                  		${schemeContent!}
	                  	<#else> 
	                  		 <a target="_blank" href="<@c.url value="/${data.lotteryType.key}/scheme-${data.schemeNumber}.html"/>">查看投注详细</a>
	                  </#if>
					</#if>
				<#else>
		        	${data.secretType.secretName}
				</#if>
            </td>
            <#if lotteryType??&&lotteryType=='DLT'><td align="center"><#if data.playType??&&data.playType=='GeneralAdditional'><img src="<@c.url value="/pages/images/addicon.gif"/>" /></#if></td></#if>
            <td align="center">${data.schemeCost}</td>
            <td align="center"><#if data.minSubscriptionCost??>#{data.minSubscriptionCost;M2}<#else>不限</#if></td>
            <td align="right">
				<#assign baodiProgressRate=data.baodiProgressRate!0 />
            	<span class="jindu">#{data.progressRate!0;M2}%</span>
            	<#if baodiProgressRate gt 0><br /><span class="greenchar">+#{baodiProgressRate;M2}%</span><span style="color:red">(保)</span></#if>
            </td>
            <td>#{(data.remainingCost!0);M2}</td>
		  	<td><#if data.state=='UNFULL'><input id="subscribe_${data.id}" type="text" maxlength="6" style="ime-mode: disabled; width: 30px;" onkeydown="number_check(this,event,0)" oncontextmenu="return false;" autocomplete="off" /><span style="margin-left:5px;;height: 100%; writing-mode: tb-rl;vertical-align: middle;"><img onclick="submitSubscribeReq(${data.id});" style="cursor:pointer;" src="<@c.url value="/pages/images/btck.gif" />"/><span><#else>${data.state.stateName}</#if></td>
            <td align="center"><a href="<@c.url value="/${data.lotteryType.key}/scheme-${data.schemeNumber}.html" />" target="_blank">查看</a></td>
		</tr>
		</#list>
	  <#else>
		<tr  onmouseout="this.className=''" onmouseover="this.className='trhover'">
			<td colspan="<#if lotteryType??&&lotteryType=='DLT'>11<#else>10</#if>">暂无记录.</td>
		</tr>
	  </#if>
	</tbody>
</table>
 <div class="kong5"></div>
	        <!-- 翻页开始 -->
		            <#import "../../macro/pagination.ftl" as b />
			        <@b.page />