package resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Calculator resource exposed at '/calculator' path
 */
@Path("/calculator")
public class CalculatorResource {

    /**
     * Method handling HTTP POST requests. The calculated answer to the expression will be sent to the client as
     * plain/text.
     * @param expression the expression to be solved as plain/text.
     * @return solution to the expression as plain/text or -1 on error
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public int calculate(String expression){
        // Removes all whitespaces
        String expressionTrimmed = expression.replaceAll("\\s+","");

        int result = -1;

        /*
         * .matches use regex expression to decide if a String matches a given pattern.
         * [0-9]+[+][0-9]+ explained:
         * [0-9]+: a number from 0-9 one or more times. For example 1, 12 and 123.
         * [+]: the operator + one time.
         * The total regex expression accepts for example:
         * 12+34,
         * 1+2,
         * 10000+1000
         */
        if(expressionTrimmed.matches("[0-9]+[+][0-9]+")) result = sum(expressionTrimmed);
        else if(expressionTrimmed.matches("[0-9]+[-][0-9]+")) result = subtraction(expressionTrimmed);

        return result;
    }

    /**
     * Method used to calculate a sum expression.
     * @param expression the equation to be calculated as a String
     * @return the answer as an int
     */
    public int sum(String expression){
        String[] split = expression.split("[+]");

        int number1 = Integer.parseInt(split[0]);
        int number2 = Integer.parseInt(split[1]);

        return number1 + number2;
    }

    /**
     * Method used to calculate a subtraction expression.
     * @param expression the expression to be calculated as a String
     * @return the answer as an int
     */
    public int subtraction(String expression){
        String[] split = expression.split("[-]");

        int number1 = Integer.parseInt(split[0]);
        int number2 = Integer.parseInt(split[1]);

        return number1 - number2;
    }
}
