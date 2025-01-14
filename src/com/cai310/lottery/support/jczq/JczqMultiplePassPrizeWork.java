package com.cai310.lottery.support.jczq;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.cai310.lottery.Constant;
import com.cai310.lottery.JczqConstant;
import com.cai310.lottery.entity.lottery.jczq.JczqMatch;
import com.cai310.lottery.service.lottery.dczc.MatchNotEndException;
import com.cai310.lottery.support.Item;
import com.cai310.lottery.support.JcWonMatchItem;
import com.cai310.lottery.support.PrintWonItem;
import com.cai310.lottery.support.PrizeWork;
import com.cai310.lottery.utils.CommonUtil;
import com.google.common.collect.Lists;

public class JczqMultiplePassPrizeWork implements PrizeWork {
	/** 各个中奖组合中奖信息的分隔符 */
	public static final String lineSeq = "<br/>";
	/** 乘号字符 */
	public static final char MUL_CHAR = '×';

	public static final NumberFormat MONEY_FORMAT = new DecimalFormat("0.00");
	public static final NumberFormat AWARD_FORMAT = new DecimalFormat("0.00");

	// ===============================================================

	protected final PlayType playType;
	protected final PassMode passMode;
	protected final Map<String, JczqMatch> drawdMatchMap;
	protected final List<JczqMatchItem> matchItemList;
	protected final List<TicketItem> ticketList;
	protected final List<Map<String, Map<String, Double>>> printAwardList;

	// ===============================================================

	protected Map<Integer, List<WonObject>> wonMap = new TreeMap<Integer, List<WonObject>>(new Comparator<Integer>() {

		public int compare(Integer o1, Integer o2) {
			if (o1 > o2)
				return 1;
			if (o1 < o2)
				return -1;
			else
				return 0;
		}
	});
	
	protected Map<Integer, List<WonObject>> ticketWonMap = new TreeMap<Integer, List<WonObject>>(new Comparator<Integer>() {

		public int compare(Integer o1, Integer o2) {
			if (o1 > o2)
				return 1;
			if (o1 < o2)
				return -1;
			else
				return 0;
		}
	});
	protected List<RefundmentObject> refundmentList = new ArrayList<RefundmentObject>();

	// ===============================================================

	/** 中奖详情 */
	protected StringBuilder wonDetail;

	/** 奖金详情 */
	protected StringBuilder prizeDetail;
	
	/** 出票回查 */
	protected List<PrintWonItem> printWonItemList=Lists.newArrayList();
	
	/** 税前总奖金 */
	protected double totalPrize = 0.0d;

	/** 税后总奖金 */
	protected double totalPrizeAfterTax = 0.0d;

	/** 是否中奖 */
	protected boolean won;

	// ===============================================================

	public JczqMultiplePassPrizeWork(PlayType playType, PassMode passMode, List<JczqMatchItem> matchItemList,
			List<TicketItem> ticketList, List<Map<String, Map<String, Double>>> printAwardList,
			Map<String, JczqMatch> drawdMatchMap) {
		super();
		this.playType = playType;///玩法
		this.passMode = passMode;///过关
		this.drawdMatchMap = drawdMatchMap;//比赛结果
		this.matchItemList = matchItemList;///选择的场次
		this.ticketList = ticketList;///出票
		this.printAwardList = printAwardList;

		init();
	}

	protected void init() {
		for (int i = 0; i < ticketList.size(); i++) {
			TicketItem ticketItem = ticketList.get(i);

			boolean isAll = ticketItem.getMatchFlag() == 0;
			int[] vals = ticketItem.getItemValues();
			List<JczqMatchItem> itemList = JczqUtil.getSelectByTicketItem(ticketItem,matchItemList);

			Map<String, Map<String, Double>> matchPrintAwardMap = printAwardList.get(i);
			if (matchPrintAwardMap == null){
				///退款+退出判断下一张票
				Integer ticketUnits=1;
				for (JczqMatchItem jczqMatchItem :itemList) {
					//计算注数
					ticketUnits*=jczqMatchItem.selectedCount();
					
				}
				refundmentList.add(new RefundmentObject(ticketUnits, ticketItem.getMultiple()));
				continue;
			    
				//throw new MatchNotEndException("出票SP值未更新.");
	       	}
			List<JcWonMatchItem> correctList = new ArrayList<JcWonMatchItem>();
			for (int k = 0; k < itemList.size(); k++) {
				JczqMatchItem matchItem = itemList.get(k);
				String matchKey = matchItem.getMatchKey();

				JcWonMatchItem wonItem = new JcWonMatchItem();
				wonItem.setMatchKey(matchKey);
				wonItem.setSelectCount(matchItem.selectedCount());

				JczqMatch match = drawdMatchMap.get(matchKey);
				if (match.isCancel()) {
					wonItem.setCancel(true);
					wonItem.setAward(1d);
					correctList.add(wonItem);
				} else if (match.isEnded()) {
					Map<String, Double> printAwardMap = matchPrintAwardMap.get(matchKey);
					Item rs = null;
					if(PlayType.MIX.equals(playType)){
						 rs = match.getResult(matchItem.getPlayType());	
					}else{
						 rs = match.getResult(playType);
					}
					Object printAwardObj = null;
					if (matchItem.hasSelect(rs)) {
						printAwardObj = printAwardMap.get(rs.getValue());
						if (printAwardObj == null )
							throw new MatchNotEndException("出票SP值未更新.");
						Double printAward = Double.valueOf(""+printAwardObj);
						if (printAward == null || printAward <= 0)
							throw new MatchNotEndException("出票SP值未更新.");

						wonItem.setAward(printAward);
						wonItem.setResultItem(rs);

						correctList.add(wonItem);
					}
				}
			}

			JczqSimplePrizeWork prizeWork = new JczqSimplePrizeWork(passMode, ticketItem.getMultiple(),
					ticketItem.getPassType(), correctList, false);

			if (prizeWork.isWon()) {
				won = true;

				if (prizeWork.getRefundmentUnits() > 0) {
					refundmentList.add(new RefundmentObject(prizeWork.getRefundmentUnits(), prizeWork.getMultiple()));
				}

				if (prizeWork.getWonMap() != null && !prizeWork.getWonMap().isEmpty()) {
					for (Map.Entry<Integer, List<List<Double>>> entry : prizeWork.getWonMap().entrySet()) {
						Integer key = entry.getKey();
						List<WonObject> helpList = wonMap.get(key);
						if (helpList == null)
							helpList = new ArrayList<WonObject>();

						WonObject obj = new WonObject(entry.getValue(), prizeWork.getMultiple());
						helpList.add(obj);

						wonMap.put(key, helpList);
					}
					//增加出票中奖统计
					ticketWonMap.clear();
					for (Map.Entry<Integer, List<List<Double>>> entry : prizeWork.getWonMap().entrySet()) {
						Integer key = entry.getKey();
						List<WonObject> helpList = ticketWonMap.get(key);
						if (helpList == null)
							helpList = new ArrayList<WonObject>();

						WonObject obj = new WonObject(entry.getValue(), prizeWork.getMultiple());
						helpList.add(obj);
						ticketWonMap.put(key, helpList);
					}
					buildTicketWon(ticketWonMap,i);
				}
			}
		}
		if (won) {
			build();
		}
	}

	protected void build() {
		buildInit();
		buildRefundment();
		buildWon();
		buildSum();
	}

	protected void buildInit() {
		totalPrize = 0.0d;
		totalPrizeAfterTax = 0.0d;
		wonDetail = new StringBuilder();
		prizeDetail = new StringBuilder();
	}

	protected void buildRefundment() {
		if (!refundmentList.isEmpty()) {
			for (RefundmentObject obj : refundmentList) {
				wonDetail.append("退款:").append(obj.getRefundmentUnits()).append("注").append(obj.getMultiple())
						.append("倍").append(lineSeq);

				double prize = obj.getRefundmentUnits() * 2 * obj.getMultiple();
				totalPrize += prize;
				totalPrizeAfterTax += prize;
				prizeDetail.append(obj.getRefundmentUnits()).append(MUL_CHAR).append(2).append(MUL_CHAR)
						.append(obj.getMultiple()).append("(退款)=").append(MONEY_FORMAT.format(prize)).append("元")
						.append(lineSeq);
			}
		}
	}

	protected void buildTicketWon(Map<Integer, List<WonObject>> wonMap,Integer index) {
		if (!wonMap.isEmpty()) {
			
			/** 税前总奖金 */
			double totalPrize = 0.0d;
			/** 税后总奖金 */
			double totalPrizeAfterTax = 0.0d;
			/** 奖金详情 */
			StringBuilder prizeDetail = new StringBuilder();
			StringBuilder wonDetail = new StringBuilder();
			StringBuilder line = new StringBuilder();
			for (Entry<Integer, List<WonObject>> entry : wonMap.entrySet()) {
				int pass = entry.getKey();
				String passText = getPassText(pass);
				wonDetail.append(passText).append(":").append(entry.getValue().size()).append("注").append(lineSeq);

				for (WonObject wonObj : entry.getValue()) {
					for (List<Double> list : wonObj.getList()) {
						line.setLength(0);
						line.append(passText).append(":");
						double prize = 1d;
						for (double award : list) {
							if (award < 0)
								throw new RuntimeException("数据异常：奖金值小于0.");

							prize *= award;
							line.append(AWARD_FORMAT.format(award)).append(MUL_CHAR);
						}

						StringBuilder tempSb = new StringBuilder();
						prize *= 2d;
						prize = CommonUtil.bankRoundPrize(prize);
						line.append(2);
						line.append('=').append(MONEY_FORMAT.format(prize)).append("元");
						if (pass >= 2) {
							Integer maxPrize = JczqConstant.getMaxPrize(pass);
							if (maxPrize != null && prize > maxPrize) {
								prize = maxPrize;
								tempSb.append(pass).append("场过关投注，单注最高奖金限额").append(maxPrize).append("元");
							}
						}

						if (prize < 2.0d) {// 补足2元
							prize = 2.0d;
							line.append("(补足2元)");
						}

						double prizeAfterTax = CommonUtil.roundPrize(Constant.countPrizeAfterTax(prize));
						if (prizeAfterTax < prize) {
							if (tempSb.length() > 0)
								tempSb.append("，");
							tempSb.append("税后").append(MONEY_FORMAT.format(prizeAfterTax)).append("元");
						}
						if (tempSb.length() > 0)
							line.append('(').append(tempSb).append(')');

						double prizeAfterTaxOfPerMul = prizeAfterTax;

						prize = BigDecimal.valueOf(prize).multiply(BigDecimal.valueOf(wonObj.getMultiple())).doubleValue();
						prizeAfterTax = BigDecimal.valueOf(prizeAfterTax).multiply(BigDecimal.valueOf(wonObj.getMultiple())).doubleValue();

						line.append('，').append(MONEY_FORMAT.format(prizeAfterTaxOfPerMul)).append('元')
								.append(MUL_CHAR).append(wonObj.getMultiple()).append("倍=")
								.append(MONEY_FORMAT.format(prizeAfterTax)).append('元');

						totalPrize += prize;
						totalPrizeAfterTax += prizeAfterTax;
						prizeDetail.append(line).append(lineSeq);
					}
				}
			}
			PrintWonItem printWonItem = new PrintWonItem();
			printWonItem.setIndex(index);
			printWonItem.setTotalPrize(totalPrize);
			printWonItem.setTotalPrizeAfterTax(totalPrizeAfterTax);
			printWonItem.setWonDetail(wonDetail.toString());
			printWonItemList.add(printWonItem);
		}
	}
	protected void buildWon() {
		if (!wonMap.isEmpty()) {
			StringBuilder line = new StringBuilder();
			for (Entry<Integer, List<WonObject>> entry : wonMap.entrySet()) {
				int pass = entry.getKey();
				String passText = getPassText(pass);
				wonDetail.append(passText).append(":").append(entry.getValue().size()).append("注").append(lineSeq);

				for (WonObject wonObj : entry.getValue()) {
					for (List<Double> list : wonObj.getList()) {
						line.setLength(0);
						line.append(passText).append(":");
						double prize = 1d;
						for (double award : list) {
							if (award < 0)
								throw new RuntimeException("数据异常：奖金值小于0.");

							prize *= award;
							line.append(AWARD_FORMAT.format(award)).append(MUL_CHAR);
						}

						StringBuilder tempSb = new StringBuilder();
						prize *= 2d;
						prize = CommonUtil.bankRoundPrize(prize);
						line.append(2);
						line.append('=').append(MONEY_FORMAT.format(prize)).append("元");
						if (pass >= 2) {
							Integer maxPrize = JczqConstant.getMaxPrize(pass);
							if (maxPrize != null && prize > maxPrize) {
								prize = maxPrize;
								tempSb.append(pass).append("场过关投注，单注最高奖金限额").append(maxPrize).append("元");
							}
						}

						if (prize < 2.0d) {// 补足2元
							prize = 2.0d;
							line.append("(补足2元)");
						}

						double prizeAfterTax = CommonUtil.roundPrize(Constant.countPrizeAfterTax(prize));
						if (prizeAfterTax < prize) {
							if (tempSb.length() > 0)
								tempSb.append("，");
							tempSb.append("税后").append(MONEY_FORMAT.format(prizeAfterTax)).append("元");
						}
						if (tempSb.length() > 0)
							line.append('(').append(tempSb).append(')');

						double prizeAfterTaxOfPerMul = prizeAfterTax;

						prize = BigDecimal.valueOf(prize).multiply(BigDecimal.valueOf(wonObj.getMultiple())).doubleValue();
						prizeAfterTax = BigDecimal.valueOf(prizeAfterTax).multiply(BigDecimal.valueOf(wonObj.getMultiple())).doubleValue();

						line.append('，').append(MONEY_FORMAT.format(prizeAfterTaxOfPerMul)).append('元')
								.append(MUL_CHAR).append(wonObj.getMultiple()).append("倍=")
								.append(MONEY_FORMAT.format(prizeAfterTax)).append('元');

						totalPrize += prize;
						totalPrizeAfterTax += prizeAfterTax;
						prizeDetail.append(line).append(lineSeq);
					}
				}
			}
		}
	}

	protected void buildSum() {
		prizeDetail.append("合计：").append(MONEY_FORMAT.format(this.totalPrizeAfterTax)).append("元").append(lineSeq);
	}

	class WonObject {
		private List<List<Double>> list;
		private int multiple;

		public List<List<Double>> getList() {
			return list;
		}

		public int getMultiple() {
			return multiple;
		}

		private WonObject(List<List<Double>> list, int multiple) {
			super();
			this.list = list;
			this.multiple = multiple;
		}
	}

	class RefundmentObject {
		private int refundmentUnits;
		private int multiple;

		public int getRefundmentUnits() {
			return refundmentUnits;
		}

		public int getMultiple() {
			return multiple;
		}

		private RefundmentObject(int refundmentUnits, int multiple) {
			super();
			this.refundmentUnits = refundmentUnits;
			this.multiple = multiple;
		}
	}

	protected String getPassText(int pass) {
		if (pass == 1)
			return "单关";
		else
			return pass + "关";
	}

	public String getWonDetail() {
		return (this.wonDetail != null) ? this.wonDetail.toString() : null;
	}

	public String getPrizeDetail() {
		return (this.wonDetail != null) ? this.prizeDetail.toString() : null;
	}

	public double getTotalPrize() {
		return this.totalPrize;
	}

	public double getTotalPrizeAfterTax() {
		return this.totalPrizeAfterTax;
	}

	public boolean isWon() {
		return won;
	}

	public List<PrintWonItem> getPrintWonItemList() {
		return printWonItemList;
	}

	public void setPrintWonItemList(List<PrintWonItem> printWonItemList) {
		this.printWonItemList = printWonItemList;
	}
}
