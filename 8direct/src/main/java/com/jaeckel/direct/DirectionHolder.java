package com.jaeckel.direct;

public interface DirectionHolder
{
   boolean isActivated(String direction);

   boolean isActivated(int direction);

   void setActivated(int direction, boolean state);

   void setActivated(String direction, boolean activated);

   void setActivated(boolean[] activated);

   boolean[] getActivated();

}
