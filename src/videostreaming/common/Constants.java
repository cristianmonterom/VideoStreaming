package videostreaming.common;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author santiago
 */
public enum Constants {
   PORT(6262),
   RATE(100),
   MAX_CLIENTS(3),
   IMAGE_DATA_SIZE(65500);
   
   private int value;
   
   private Constants(int setValue)
   {
      value = setValue;
   }
   public int getValue()
   {
      return value;
   }
}
