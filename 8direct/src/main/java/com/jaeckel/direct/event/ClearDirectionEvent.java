package com.jaeckel.direct.event;

/**
 * @author flashmop
 * @date 27.06.13 23:15
 */
public class ClearDirectionEvent {
    int direction;


    public ClearDirectionEvent(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ClearDirectionEvent{");
        sb.append("direction=").append(direction);
        sb.append('}');
        return sb.toString();
    }
}
