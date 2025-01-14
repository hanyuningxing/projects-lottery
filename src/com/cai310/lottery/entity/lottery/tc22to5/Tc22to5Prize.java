package com.cai310.lottery.entity.lottery.tc22to5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import com.cai310.lottery.exception.DataException;
import com.cai310.lottery.support.PrizeItem;
import com.cai310.lottery.support.WinItem;
import com.cai310.lottery.utils.BigDecimalUtil;

@Embeddable
public class Tc22to5Prize implements Serializable {
	private static final long serialVersionUID = 4761360751476732484L;
	private Integer firstPrize;
	private Integer secondPrize;
	private Integer thirdPrize;

	
	@Column(name = "firstPrize")
	public Integer getFirstPrize() {
		return this.firstPrize;
	}

	public void setFirstPrize(Integer firstPrize) {
		this.firstPrize = firstPrize;
	}

	public Integer calcFirstPrize(Integer winUnit) {
		Integer prize = this.getFirstPrize();
		if (winUnit != null && winUnit > 0 && prize != null && prize > 0) {
			return winUnit * prize;
		}
		return null;
	}

	
	@Column(name = "secondPrize")
	public Integer getSecondPrize() {
		return this.secondPrize;
	}

	public void setSecondPrize(Integer secondPrize) {
		this.secondPrize = secondPrize;
	}

	public Integer calcSecondPrize(Integer winUnit) {
		Integer prize = this.getSecondPrize();
		if (winUnit != null && winUnit > 0 && prize != null && prize > 0) {
			return winUnit * prize;
		}
		return null;
	}

	
	@Column(name = "thirdPrize")
	public Integer getThirdPrize() {
		return this.thirdPrize;
	}

	public void setThirdPrize(Integer thirdPrize) {
		this.thirdPrize = thirdPrize;
	}

	public Integer calcThirdPrize(Integer winUnit) {
		Integer prize = this.getThirdPrize();
		if (winUnit != null && winUnit > 0 && prize != null && prize > 0) {
			return winUnit * prize;
		}
		return null;
	}


	public int calcTotalPrize(Tc22to5WinUnit winUnit) {
		int totalPrize = 0;
		Integer prize = this.calcFirstPrize(winUnit.getFirstWinUnits());
		if (prize != null) {
			totalPrize += prize;
		}
		prize = this.calcSecondPrize(winUnit.getSecondWinUnits());
		if (prize != null) {
			totalPrize += prize;
		}
		prize = this.calcThirdPrize(winUnit.getThirdWinUnits());
		if (prize != null) {
			totalPrize += prize;
		}
		
		return totalPrize;
	}

	public List<PrizeItem> getPrizeItemList(Tc22to5WinUnit winUnit) {
		List<PrizeItem> list = new ArrayList<PrizeItem>();

		Integer unit = winUnit.getFirstWinUnits();
		Integer prize = this.getFirstPrize();
		if (unit != null && unit > 0) {
			list.add(new PrizeItem(new WinItem("一等奖", unit), BigDecimalUtil.valueOf(prize)));
		}

		unit = winUnit.getSecondWinUnits();
		prize = this.getSecondPrize();
		if (unit != null && unit > 0) {
			list.add(new PrizeItem(new WinItem("二等奖", unit), BigDecimalUtil.valueOf(prize)));
		}

		unit = winUnit.getThirdWinUnits();
		prize = this.getThirdPrize();
		if (unit != null && unit > 0) {
			list.add(new PrizeItem(new WinItem("三等奖", unit), BigDecimalUtil.valueOf(prize)));
		}
		return list;
	}

	public void checkPrize() throws DataException {
		if (this.getFirstPrize() == null || this.getFirstPrize() < 0) {
			throw new DataException("一等奖奖金未设置！");
		}
		if (this.getSecondPrize() == null || this.getSecondPrize() < 0) {
			throw new DataException("二等奖奖金未设置！");
		}
		if (this.getThirdPrize() == null || this.getThirdPrize() < 0) {
			throw new DataException("三等奖奖金未设置！");
		}
	}
}
