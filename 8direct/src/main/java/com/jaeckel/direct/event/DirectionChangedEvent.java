package com.jaeckel.direct.event;

/**
 * @author flashmop
 * @date 27.06.13 23:15
 */
public class DirectionChangedEvent
{
   private int direction;
   private boolean activated;

   public DirectionChangedEvent(int direction, boolean value)
   {
      this.direction = direction;
      this.setActivated(value);
   }

   public int getDirection()
   {
      return direction;
   }

   public void setDirection(int direction)
   {
      this.direction = direction;
   }

   @Override
   public String toString()
   {
      final StringBuffer sb = new StringBuffer("ClearDirectionEvent{");
      sb.append("direction=").append(direction);
      sb.append('}');
      return sb.toString();
   }

   public boolean isActivated()
   {
      return activated;
   }

   public void setActivated(boolean value)
   {
      this.activated = value;
   }
}
