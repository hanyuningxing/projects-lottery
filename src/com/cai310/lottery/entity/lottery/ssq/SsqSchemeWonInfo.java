package com.cai310.lottery.entity.lottery.ssq;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cai310.lottery.entity.lottery.SchemeWonInfo;

@Table(name = com.cai310.lottery.Constant.LOTTERY_TABLE_PREFIX + "SSQ_SCHEME_WON_INFO")
@Entity
public class SsqSchemeWonInfo extends SchemeWonInfo {

	private static final long serialVersionUID = 4981165004505541073L;

	private SsqWinUnit winUnit = new SsqWinUnit();

	@Embedded
	public SsqWinUnit getWinUnit() {
		return winUnit;
	}

	public void setWinUnit(SsqWinUnit winUnit) {
		if (winUnit != null) {
			this.winUnit = winUnit;
		} else {
			this.winUnit = new SsqWinUnit();
		}
	}

}