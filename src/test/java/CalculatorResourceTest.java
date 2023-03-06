import org.junit.jupiter.api.Test;
import resources.CalculatorResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorResourceTest{

    @Test
    public void testCalculate(){
        CalculatorResource calculatorResource = new CalculatorResource();

        String expression = "100+300";
        assertEquals("400", calculatorResource.calculate(expression));

        expression = "300-100";
        assertEquals("200", calculatorResource.calculate(expression));
        
        expression = "300*100";
        assertEquals("30000", calculatorResource.calculate(expression));
        
        expression = "300/100";
        assertEquals("3", calculatorResource.calculate(expression));
        
        expression = "100+300-100";
        assertEquals("Error: Invalid expression.", calculatorResource.calculate(expression));
    }
    

    @Test
    public void testSum(){
        CalculatorResource calculatorResource = new CalculatorResource();

        String expression = "100+300";
        assertEquals(400, calculatorResource.sum(expression));
        
        expression = "50+300+150+1000";
        assertEquals(1500, calculatorResource.sum(expression));
    }

    @Test
    public void testSubtraction(){
        CalculatorResource calculatorResource = new CalculatorResource();

        String expression = "999-100";
        assertEquals(899, calculatorResource.subtraction(expression));
    
        expression = "1000-300-150-50";
        assertEquals(500, calculatorResource.subtraction(expression));
    }
    
    @Test
    void multiplicationTest() {
        CalculatorResource calculatorResource = new CalculatorResource();
        
        String expression = "100*300";
        assertEquals(30000, calculatorResource.multiplication(expression));

        expression = "100*300*100";
        assertEquals(3000000, calculatorResource.multiplication(expression));
    }
    
    @Test
    void divisionTest() {
        CalculatorResource calculatorResource = new CalculatorResource();
        
        String expression = "1000/100";
        assertEquals(10, calculatorResource.division(expression));

        expression = "1000/100/10";
        assertEquals(1, calculatorResource.division(expression));
    }
}
