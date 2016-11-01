package test.com.finanalyzer.endpoints;

import com.finanalyzer.domain.CompoundInterest;
import com.finanalyzer.endpoints.CalculatorEndPoint;
import com.finanalyzer.util.CalculatorUtil;

import junit.framework.TestCase;

public class CalculatorEndPointTest extends TestCase{

	private static final float TOLERANCE = 0.01F;
	public void testCalculateMissingCompoundInterestComponent()
	{
		CalculatorEndPoint calculatorEndPoint = new CalculatorEndPoint();
		
		final CompoundInterest calculateInitialAmountComponent = calculatorEndPoint.calculateMissingCompoundInterestComponent(0.0f, 10.0f, 20*365, 7209.57f);
		assertEquals(1000, calculateInitialAmountComponent.getInitialAmount(), TOLERANCE);
		
		final CompoundInterest calculateInterestRateComponent = calculatorEndPoint.calculateMissingCompoundInterestComponent(1000.0f, 0.0f, 20*365, 7209.57f);
		assertEquals(10.0f, calculateInterestRateComponent.getInterestRate(), TOLERANCE);

		final CompoundInterest calculateFinalAmountComponent = calculatorEndPoint.calculateMissingCompoundInterestComponent(1000.0f, 10.0f, 20*365, 0.0f);
		assertEquals(7209.57f, calculateFinalAmountComponent.getFinalAmount(), TOLERANCE);
	}

}
