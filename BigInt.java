import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Used to do simple calculations with very large non-negative integers.
 * 
 * @author Michael Higgins
 */
public class BigInt
{
   private static final String USAGE_MESSAGE = "Usage: java BigInt " + "num "
                                                      + "opp" + " num\n"
                                                      + "Enter 'DONE' to exit.";
   private static final String ADD_OP        = "+";
   private static final String MULT_OP       = "*";

   /**
    * Constructor that takes a string representation of the number. Note that
    * the string may contain leading zeros that will be discarded; however,
    * remember that 0 by itself is a number as well.
    * 
    * @param value
    *           string to convert to a BigInt.
    */
   public BigInt(String value)
   {
      String validBigInt = "";
      int temp = 0;
      boolean beginCounting = false;

      // validate the characters in the String and then store them as a BigInt
      // note: the characters that are in validBigInt must all be ints and the
      // leading zeros will be discarded
      for (int i = 0; i < value.length(); i++)
      {

         // first validate that the character is an integer and don't count
         // the length of the BigInt until all front zeros are removed
         temp = Integer.valueOf(value.substring(i, i + 1));

         // removing the zeros
         if (temp != 0)
         {
            beginCounting = true;
         }

         if (beginCounting)
         {
            validBigInt += temp;
         }
      }

      // initialize the byte array to hold the bytes of the number and then
      // convert all of the bytes in the number to byte ints
      _bigInt = validBigInt.getBytes();

      for (int i = 0; i < validBigInt.length(); i++)
      {
         _bigInt[i] = intValueOf(_bigInt[i]);
      }
   } // BigInt(String value)

   /**
    * Constructor that takes a long representation of the number.
    * 
    * @param value
    *           the value to create a BigInt with.
    */
   public BigInt(Long value)
   {
      // calling another constructor to do the work, because lazy is better
      this(value.toString());
   }

   /**
    * Zero arg constructor to prevent an empty instance of BigInt from being
    * created.
    */
   private BigInt()
   {
   }

   /**
    * A method that returns the sum of the receiver of the message and the
    * parameter.
    * 
    * @param number
    *           the BigInt to add to the caller.
    * 
    * @return A BigInt representing the sum of the caller and the number
    *         parameter.
    */
   public BigInt add(BigInt number)
   {
      // remember that we are using the numbers for the BigInt that are stored
      // IN ORDER like they were passed into the constructor
      String result = "";
      String longerBigInt = "";
      String shorterBigInt = "";
      int tempSum = 0;
      int tempSumMod10 = 0;
      int carry = 0;
      int modNum = 10;
      int divNum = 10;

      // determining the shorter and longer BigInts by byte count and storing
      // the BigInts as Strings temporarily to allow the appending of zeros to
      // the front of them more easily than if they remained in a byte array
      if (this._bigInt.length > number._bigInt.length)
      {
         longerBigInt = this.toString();
         shorterBigInt = number.toString();
      }
      else
      {
         longerBigInt = number.toString();
         shorterBigInt = this.toString();
      }

      // adding zeros to the front of the shorterBigInt to greatly simplify our
      // addition
      while (longerBigInt.length() != shorterBigInt.length())
      {
         shorterBigInt = "0" + shorterBigInt;
      }

      // loop through the indexes backwards
      for (int i = longerBigInt.length(); i > 0; i--)
      {
         // save the sum of the shorterBigIntNum + longerBigIntNum +
         // tempSumMod10
         // save the tempSum modded by 10
         // save the tempSum int div by 10
         // save the result as ModNum + result
         tempSum = Integer.parseInt(shorterBigInt.substring(i - 1, i))
                  + Integer.parseInt(longerBigInt.substring(i - 1, i)) + carry;
         tempSumMod10 = tempSum % modNum;
         carry = tempSum / divNum;
         result = tempSumMod10 + result;
      }

      // if there is a leftover carry, add it to the front of the result
      if (carry > 0)
      {
         result = carry + result;
      }

      return new BigInt(result);
   } // add(BigInt number)

   /**
    * A method that returns the product of the receiver of the message and the
    * parameter.
    * 
    * @param number
    *           the BigInt to multiply by the caller.
    * 
    * @return A BigInt representing the product of the caller and the number
    *         parameter.
    */
   public BigInt multiply(BigInt number)
   {
      String extraZeros = "";
      String resultJ = "";
      String runningTotal = "";
      int tempMult = 0;
      int tempMultMod10 = 0;
      int carry = 0;
      int modNum = 10;
      int divNum = 10;

      // loop through the BigInt parameter number from the back to the front
      for (int i = number._bigInt.length; i > 0; i--)
      {
         // for each iteration beyond the first, add another 0 to be appended to
         // tempMult. If i is less than the length of the BigInt,
         // then it has iterated
         if (number._bigInt.length > i)
         {
            extraZeros += "0";
         }

         // loop through the BigInt caller from the back to the front
         for (int j = this._bigInt.length; j >= 0; j--)
         {
            // set tempMult = the int value of the string representation of the
            // BigInt number * the int value of the string representation of the
            // BigInt caller + the carry number from the last loop + the
            // extraZeros from the depth of the multiplication
            // set tempMultMod10 = tempMult % modNum which is the next value to
            // add to the front of resultJ
            // set tempMultDiv10 = tempMult / divNum which is the carry value to
            // add to the next tempMult, or the front of resultI if we have
            // completed looping through all indexes of I and J
            // set resultJ = tempMod10 + resultJ which is our running total for
            // the
            // number at index i in the BigInt number * all numbers in the
            // BigInt caller
            if (j > 0 && i > 0)
            {
               tempMult = Integer.valueOf((((Integer.valueOf(number.toString()
                        .substring(i - 1, i)) * Integer.valueOf(this.toString()
                        .substring(j - 1, j))) + carry)));
            }
            else
            {
               tempMult = carry;
            }

            tempMultMod10 = tempMult % modNum;
            carry = tempMult / divNum;
            resultJ = tempMultMod10 + resultJ;
         }

         // set runningTotal = runningTotal + resultJ which is the running total
         // for the BigInt number * the BigInt caller
         runningTotal = (new BigInt(runningTotal).add(new BigInt(resultJ
                  + extraZeros))).toString();

         // reset all inner loop variables except for carry because it
         // is what carries for us
         resultJ = "";
         tempMult = 0;
         tempMultMod10 = 0;
      }

      return (new BigInt(runningTotal));
   } // multiply

   /**
    * Method to convert a byte to an integer stored in a byte.
    * 
    * @param value
    *           the value to convert to an int and store as a byte.
    * 
    * @return byte literal for an integer.
    */
   private static byte intValueOf(byte value)
   {
      byte asciiAdjustValue = 48;

      return (byte) (value - asciiAdjustValue);
   }

   /**
    * A method to give the caller a String representation of the number.
    * 
    * @return A String representation of the caller.
    */
   public String toString()
   {
      // loop through the array to reassemble the number as a String
      StringBuilder result = new StringBuilder();

      for (int i = 0; i < this._bigInt.length; i++)
      {
         result.append(this._bigInt[i]);
      }

      return result.toString();
   }

   /**
    * A method to compute the equation given by the parameters.
    * 
    * @param bigInt1
    *           the first BigInt.
    * @param operator
    *           the operation to be performed.
    * @param bigInt2
    *           the second BigInt.
    * @return the result of the equation as a BigInt.
    */
   private static BigInt computeEquation(BigInt bigInt1, String operator,
            BigInt bigInt2)
   {
      BigInt result;

      switch (operator)
      {
         case ADD_OP:
            result = bigInt1.add(bigInt2);
            break;

         case MULT_OP:
            result = bigInt1.multiply(bigInt2);
            break;

         default:
            throw new IllegalArgumentException();
      }

      return result;
   }

   /**
    * A method to display the results of a given expression to the console.
    * 
    * @param bigInt1
    *           the first BigInt.
    * @param operator
    *           the operation to be performed.
    * @param bigInt2
    *           the second BigInt.
    * @param result
    *           the result of the operation being performed on the two BigInts.
    */
   private static void displayResultsOf(BigInt bigInt1, String operator,
            BigInt bigInt2, BigInt result)
   {
      int maxWidth;

      // determine max width
      if (bigInt1._bigInt.length > bigInt2._bigInt.length
               && bigInt1._bigInt.length > result._bigInt.length)
      {
         maxWidth = bigInt1._bigInt.length;
      }
      else if (bigInt1._bigInt.length < bigInt2._bigInt.length
               && result._bigInt.length < bigInt2._bigInt.length)
      {

         maxWidth = bigInt2._bigInt.length;
      }
      else
      {
         maxWidth = result._bigInt.length;
      }

      // display the results
      System.out.printf("  %" + maxWidth + "s \n", bigInt1.toString()
               .equals("") ? 0 : bigInt1.toString());
      System.out.printf("%s%" + (maxWidth + 1) + "s\n", operator,
               bigInt2.toString());

      for (int i = 0; i <= maxWidth + 1; i++)
      {
         System.out.print("-");
      }

      System.out.printf("\n= %" + maxWidth + "s \n", result.toString());

   }

   // store the BigInt number as a byte array
   private byte[] _bigInt;

   // main program
   public static void main(String[] args)
   {
      String theEquation;
      Scanner inputScanner = new Scanner(System.in);
      Scanner equationScanner;
      BigInt bigInt1;
      BigInt bigInt2;
      BigInt result;
      boolean finished = false;
      String operator;
      final String QUIT = "DONE";

      // while the user doesn't want to quit
      while (!finished)
      {
         // prompt the user for an equation
         System.out.print("\nCompute: ");

         // get two operands and an operator, as a string
         // see if we are finished, if not we'll break this
         // up into an operator and operands
         theEquation = inputScanner.nextLine();

         // if the user wants to quit, then we'll quit, else compute
         finished = theEquation.equals(QUIT);

         if (!finished) // perform the operation
         {
            equationScanner = new Scanner(theEquation);

            try
            {
               // create a scanner to parse the equation
               // separate the operands and operator
               // get the second BigInt
               // do the computation
               // display the result
               bigInt1 = new BigInt(equationScanner.next());
               operator = equationScanner.next();
               bigInt2 = new BigInt(equationScanner.next());
               result = computeEquation(bigInt1, operator, bigInt2);
               BigInt.displayResultsOf(bigInt1, operator, bigInt2, result);
            }
            catch (NumberFormatException e)
            {
               System.err.println(USAGE_MESSAGE);
               finished = true;
            }
            catch (IllegalArgumentException e)
            {
               System.err.println(USAGE_MESSAGE);
               finished = true;
            }
            catch (NoSuchElementException e)
            {
               System.err.println(USAGE_MESSAGE);
               finished = true;
            }
         }
         else
         {
            System.out.println("Thank you for using "
                     + "this program, have a nice day!");
         }
      }
      inputScanner.close();

      // get the numbers via chars, not strings
      // string builder for toString()
   } // main
}
