package com.jaeckel.direct;

public interface DirectionHolder
{

   /**
    * TODO
    * 
    * @param direction
    * @return
    */
   boolean isActivated(String direction);

   /**
    * TODO
    * 
    * @param direction
    * @param activated
    */
   void setActivated(String direction, boolean activated);

}
