 <div class="yhzxzh">
	  	  <span class="yhzxtx"></span>
	    <span class="yhzxzhwz black_666_14">欢迎您：<span class="black_333_14"><#if loginUser??>${loginUser.userName!""}</#if></span><br /><span class="black_666_12">账户余额：<span class="rc1"><strong><#if loginUser??>${loginUser.remainMoney?string.currency}</#if></strong> 元</span>&nbsp;&nbsp;&nbsp;冻结资金：<span class="blue_0091d4"><strong>0</strong> 元</span></span><br /><span class="black_666_12">上次登录：<#if userLogin??&&userLogin.lastLoginTime??>${userLogin.lastLoginTime?string("yyyy-MM-dd HH:mm")}</#if>&nbsp;&nbsp;&nbsp;IP ：<#if userLogin??&&userLogin.lastLoginIp??>${userLogin.lastLoginIp}</#if></span></span>
</div>