package com.jaeckel.direct;

public interface DirectionHolder
{
   boolean isActivated(String direction);

   boolean isActivated(int position);

   void setActivated(String direction, boolean activated);

}
