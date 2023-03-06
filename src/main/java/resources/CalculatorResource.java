package resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Calculator resource exposed at '/calculator' path
 */
@Path("/calculator")
public class CalculatorResource {

    /**
     * Method handling HTTP POST requests. The calculated answer to the expression will be sent to the client as
     * plain/text.
     *
     * @param expression the expression to be solved as plain/text.
     * @return solution to the expression as plain/text or -1 on error
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String calculate (String expression){
    // Removes all whitespaces
    String expressionTrimmed = expression.replaceAll("\\s+", "");
    
    int result = -1;
    
    /*
     * .matches use regex expression to decide if a String matches a given pattern.
     * [0-9]+[+][0-9]+ explained:
     * [0-9]+: a number from 0-9 one or more times. For example 1, 12 and 123.
     * [+]: the operator + one time.
     * The total regex expression accepts for example:
     * 12+34,
     * 1+2
     * 10000+1000
     *
     *  ^\\d+(?:\\s*[+]\\s*\\d+)*$
     */
    if (expressionTrimmed.matches("^\\d+(?:\\s*[+]\\s*\\d+)*$")) result = sum(expressionTrimmed);
    else if (expressionTrimmed.matches("^\\d+(?:\\s*[-]\\s*\\d+)*$")) result = subtraction(expressionTrimmed);
    else if (expressionTrimmed.matches("^\\d+(?:\\s*[*]\\s*\\d+)*$")) result = multiplication(expressionTrimmed);
    else if (expressionTrimmed.matches("^\\d+(?:\\s*[/]\\s*\\d+)*$")) result = division(expressionTrimmed);
    else return "Error: Invalid expression.";
    return Integer.toString(result);
    }
    
    /**
     * Method used to calculate a sum expression.
     * @param expression the equation to be calculated as a String
     * @return the answer as an int
     */
    public int sum (String expression){
    String[] split = expression.split("[+]");
    int result = 0;
    for (int i = 0; i < split.length; i++) {
        split[i] = split[i].trim();
        result += Integer.parseInt(split[i]);
    
    }
    return result;
    }
    
    /**
     * Method used to calculate a subtraction expression.
     * @param expression the expression to be calculated as a String
     * @return the answer as an int
     */
    public int subtraction (String expression){
    String[] split = expression.split("[-]");
    int result = Integer.parseInt(split[0]);
    for (int i = 1; i < split.length; i++) {
        split[i] = split[i].trim();
        result -= Integer.parseInt(split[i]);
    }
    return result;
    }
    
    /**
     * Method used to calculate a multiplication expression.
     *
     * @param expression the expression to be calculated as a String
     * @return the answer as an int
     */
    public int multiplication (String expression){
    String[] split = expression.split("[*]");
      int result = Integer.parseInt(split[0]);
      for (int i = 1; i < split.length; i++) {
        split[i] = split[i].trim();
        result *= Integer.parseInt(split[i]);
      }
    return result;
    
    }
    
    /**
     * Method used to calculate a division expression.
     * @param expression the expression to be calculated as a String
     * @return the answer as an int
     */
    public int division (String expression){
        String[] split = expression.split("[/]");
        int result = Integer.parseInt(split[0]);
        for (int i = 1; i < split.length; i++) {
            split[i] = split[i].trim();
            result /= Integer.parseInt(split[i]);
        }
        return result;
        }
    }


