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
   IMAGE_DATA_SIZE(62464),
   TIME_CAPTURE(200),
   VIDEO_HEIGHT(240),
   VIDEO_WIDTH(320),
   WINDOW_HEIGHT(240),
   WINDOW_WIDTH(320)
   ;
   
   
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
