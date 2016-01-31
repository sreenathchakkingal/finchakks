<br/>
		Net Returns: <fmt:formatNumber value="${profitAndLoss.averageReturn}"  type="percent" maxIntegerDigits="2" maxFractionDigits="2"/>
<br/>
		Net Investment: <fmt:formatNumber value="${profitAndLoss.totalInvestment}" pattern="#,###.##" />
<br/>
		Total Return: <fmt:formatNumber value="${profitAndLoss.totalReturn}" pattern="#,###.##" />
<br/>
		Total Return(If Bank): <fmt:formatNumber value="${profitAndLoss.totalReturnIfBank}" pattern="#,###.##" />
<br/>
		Net Diff(Stocks vs Bank): <fmt:formatNumber value="${profitAndLoss.totalReturnVsIfBank}" pattern="#,###.##" />
<br/>