<script type="text/javascript" >
	function chg(value){
		if(value==0){
			$("#forecast").css("display","block");
			$("#skill").css("display","none");
		}else if(value==1){
			$("#skill").css("display","block");
			$("#forecast").css("display","none");
		}else if(value==2){
			$("#result").css("display","block");
			$("#info").css("display","none");
		}else if(value==3){
			$("#info").css("display","block");
			$("#result").css("display","none");
		}
				
	} 

</script>

<!-- left开始 -->
  <!-- left结束 -->
  <!-- 右边开始 -->
<div class="main_cz_right">
    <div class="rightkuangb1">
      <div class="rigtopbg">
        <div class="lefttc">快乐十分开奖号码</div>
        <div class="righttc"></div> 
      </div> 
      <div class="all5px">
          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tbbge5">
            <tr class="trgrayf0f0f0 boldchar" >
              <td width="65" height="20">期号 </td>
              <td width="50">时间 </td>
              <td>号码 </td>
            </tr> 
			<tr class="trw" id="resultList_0"></tr>
			<tr class="trw" id="resultList_1"></tr>
			<tr class="trw" id="resultList_2"></tr>
			<tr class="trw" id="resultList_3"></tr>
			<tr class="trw" id="resultList_4"></tr>
			<tr class="trw" id="resultList_5"></tr>
			<tr class="trw" id="resultList_6"></tr>
			<tr class="trw" id="resultList_7"></tr>
          </table>
      </div>
	  <div class="floatrig rig10" style="margin-bottom:8px;"></div>
    </div>
    
    
    <div class="kong10"></div>
    <div class="rightkuangb1">
      <div class="kaijtishi" style="padding-bottom:4px;">
        <div class="yelbgbt"><a href="/ch/klsf-analyse!klsfzs-qyzs.htm" target="_blank" ><font color="#c61d01">${lotteryType.lotteryName!}数据分析图表</font></a></div>
        <div class="kong10"></div>
        <ul class="graybt_menu">
          <li><a href="/ch/klsf-analyse!klsfzs-qyzs.htm" style="margin-right:6px;" target="_blank">综合走势图</a></li>
          <li><a href="/ch/klsf-analyse!klsfzs-lhzs.htm" target="_blank">连号走势图</a></li>
        </ul>
        <ul class="graybt_menu">
          <li><a href="/ch/klsf-analyse!klsfzs-chzs.htm" target="_blank" style="margin-right:6px;">重号走势</a></li>
          <li><a href="/ch/klsf-analyse!klsfzs-xmzs.htm" target="_blank">新码走势</a></li>
        </ul>
        <ul class="graybt_menu">
          <li><a href="/ch/klsf-analyse!yilou-RAND1.htm" target="_blank" style="margin-right:6px;">选一遗漏</a></li>
          <li><a href="/ch/klsf-analyse!yilou-RAND2.htm" target="_blank">任二遗漏</a></li>
        </ul>
        <ul class="graybt_menu">
          <li><a href="/ch/klsf-analyse!yilou-QSDX.htm" target="_blank">选三前直遗漏</a></li>
          <li><a href="/ch/klsf-analyse!yilou-QEZX.htm" target="_blank" style="margin-right:6px;">选二连组遗漏</a></li>
        </ul>
        </div> 
    </div>
    
    <script type="text/javascript" language="javascript">
	           function ShowWon(name,obj){
					var BaseObj=document.getElementById(name+"WonDiv");
				    var wonContent=$('#wonContentdiv div[_name="won"]');
		            var wonTitle=$('#wonTitlediv a');
					for(var i=0;i<wonTitle.length;i++){
						wonTitle[i].className='';
					}
					for(var i=0;i<wonContent.length;i++){
						wonContent[i].style.display='none';
					}
					BaseObj.style.display='block';
					obj.className='now';
				}
	</script>
    <div class="kong10"></div>
    <div class="rightkuangb1">
      <div class="chuhaobg" id="wonTitlediv">
        <ul class="kgnav" style="padding-left:8px;">
     	  <li><a href="#" class="now" onclick="ShowWon('new',this);return false;">最新</a></li>
          <li><a href="#"  onclick="ShowWon('today',this);return false;">今日</a></li>
          <li><a href="#"  onclick="ShowWon('week',this);return false;">周榜</a></li>
          <li><a href="#"  onclick="ShowWon('month',this);return false;">月榜</a></li>
          <li><a href="#"  onclick="ShowWon('sum',this);return false;">总榜</a></li>
 
        </ul>
      </div>
	  <div class="all5px" id="wonContentdiv">
	        <div id="newWonDiv" _name="won">
	          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tbbge5">
	            <tr class="trgrayf0f0f0 boldchar" >
	              <td width="105" height="20">用户名 </td>
	              <td width="55">玩法 </td>
	              <td>中奖金额 </td>
	            </tr>
	            <tr class="trw" id="newWon_0"></tr>
				<tr class="trw" id="newWon_1"></tr>
				<tr class="trw" id="newWon_2"></tr>
				<tr class="trw" id="newWon_3"></tr>
				<tr class="trw" id="newWon_4"></tr>
				<tr class="trw" id="newWon_5"></tr>
	          </table>
	        </div>
	        
	         <div id="todayWonDiv" _name="won" style="display:none">
		          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tbbge5">
		            <tr class="trgrayf0f0f0 boldchar" >
		              <td width="55" height="20">排行</td>
		              <td width="105">用户名</td>
		              <td>累计奖金</td>
		            </tr>
		            <tr class="trw" id="todayWon_0"></tr>
					<tr class="trw" id="todayWon_1"></tr>
					<tr class="trw" id="todayWon_2"></tr>
					<tr class="trw" id="todayWon_3"></tr>
					<tr class="trw" id="todayWon_4"></tr>
					<tr class="trw" id="todayWon_5"></tr>
		          </table>
            </div>
            <div id="weekWonDiv" _name="won" style="display:none">
		          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tbbge5">
		            <tr class="trgrayf0f0f0 boldchar" >
		              <td width="55" height="20">排行</td>
		              <td width="105">用户名</td>
		              <td>累计奖金</td>
		            </tr>
		            <tr class="trw" id="weekWon_0"></tr>
					<tr class="trw" id="weekWon_1"></tr>
					<tr class="trw" id="weekWon_2"></tr>
					<tr class="trw" id="weekWon_3"></tr>
					<tr class="trw" id="weekWon_4"></tr>
					<tr class="trw" id="weekWon_5"></tr>
		          </table>
            </div>
            <div id="monthWonDiv" _name="won" style="display:none">
		          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tbbge5">
		            <tr class="trgrayf0f0f0 boldchar" >
		              <td width="55" height="20">排行</td>
		              <td width="105">用户名</td>
		              <td>累计奖金</td>
		            </tr>
		            <tr class="trw" id="monthWon_0"></tr>
					<tr class="trw" id="monthWon_1"></tr>
					<tr class="trw" id="monthWon_2"></tr>
					<tr class="trw" id="monthWon_3"></tr>
					<tr class="trw" id="monthWon_4"></tr>
					<tr class="trw" id="monthWon_5"></tr>
		          </table>
            </div>
	        
	        <div id="sumWonDiv" _name="won" style="display:none">
		          <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tbbge5">
		            <tr class="trgrayf0f0f0 boldchar" >
		              <td width="55" height="20">排行</td>
		              <td width="105">用户名</td>
		              <td>累计奖金</td>
		            </tr>
		            <tr class="trw" id="sumWon_0"></tr>
					<tr class="trw" id="sumWon_1"></tr>
					<tr class="trw" id="sumWon_2"></tr>
					<tr class="trw" id="sumWon_3"></tr>
					<tr class="trw" id="sumWon_4"></tr>
					<tr class="trw" id="sumWon_5"></tr>
		          </table>
            </div>
	  </div>  
    </div>
    
	  <div class="kong10"></div>
	  <div class="rightkuangb1">
      <div class="kaijtishi" style="padding-bottom:4px;">
        <div class="yelbgbt"><a href="/ch/ZPD_KLSF_INFO.htm" target="_blank" ><font color="#c61d01">广东福彩${lotteryType.lotteryName!}新闻资讯</font></a></div>
        <div class="kong10"></div>    
        <ul class="graybt_menu">
          <li><a href="/ch/ZPD_KLSF_FORECAST.htm" target="_blank" onmouseover="chg(0);"} style="margin-right:6px;"><h2>${lotteryType.lotteryName!}预测分析</h2></a></li>
          <li><a href="/ch/ZPD_KLSF_SKILLS.htm" target="_blank" onmouseover="chg(1);"><h2>${lotteryType.lotteryName!}投注技巧</h2></a></li>	 
        </ul>
        <div style="display:block;" id="forecast">
        <div class="graybt_menu1">
        	<#if forecastList?? && forecastList?size gt 0>
				<#list forecastList as data> 
				        <a href="/info/${data.id!}.html" target="_blank">
				    	<span <#if data??&&data.titleColor??> class="${data.titleColor.colorStyle}"</#if>> 
								${data.shortTitle!} 
						</span>
				    	</a></br>
				</#list>
			  </#if>
			  </div>
		</div>	
		<div style="display:none;" id="skill">
        <div class="graybt_menu1">
        	<#if skillsList?? && skillsList?size gt 0>
				<#list skillsList as data>
				        <a href="/info/${data.id!}.html" target="_blank">
				    	<span <#if data??&&data.titleColor??> class="${data.titleColor.colorStyle}"</#if>> 
								${data.shortTitle!} 
						</span>
				    	</a></br>
				</#list>
			  </#if>
			  </div>
		</div>	  
        <ul class="graybt_menu">
          <li><a href="/ch/ZPD_KLSF_RESULTNOTICE.htm" target="_blank" style="margin-right:6px;" onmouseover="chg(2);"><h2>${lotteryType.lotteryName!}开奖号码</h2></a></li>
          <li><a href="/ch/ZPD_KLSF_INFO.htm" target="_blank" onmouseover="chg(3);"><h2>${lotteryType.lotteryName!}热点新闻</h2></a></li>
        </ul>
		   <div class="graybt_menu1" style="display:block;" id="result">  
		       <#if resultList?? && resultList?size gt 0>
						<#list resultList as data>
						    	<a href="/info/${data.id!}.html" target="_blank">
						    	<span <#if data??&&data.titleColor??> class="${data.titleColor.colorStyle}"</#if>> 
										${data.shortTitle!} 
								</span>
						    	</a></br>
						</#list>
				</#if>
		    </div>
    
		    <div class="graybt_menu1" style="display:none;" id="info">  
		       <#if infoList?? && infoList?size gt 0>
						<#list infoList as data>
						    	<a href="/info/${data.id!}.html" target="_blank">
						    	<span <#if data??&&data.titleColor??> class="${data.titleColor.colorStyle}"</#if>> 
										${data.shortTitle!} 
								</span>
						    	</a></br>
						</#list>
				</#if>
		    </div>
    
        </div> 
    </div>       
    
      <div class="kong10"></div>
      <div class="rightkuangb1">
		      <div class="chuhaobg">
		        <div class="chuhaobglef">${lotteryType.lotteryName!}热门搜索</div>
		      </div> 
      <div class="right_neik">
      
	 <div class="cp_clear1"></div>	 
	  </div>
	 </div>
      
  </div>