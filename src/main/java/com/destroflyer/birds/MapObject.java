/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.destroflyer.birds;

/**
 *
 * @author Carl
 */
public class MapObject {

    public MapObject() {
        
    }
    protected Map map;
    protected float x;
    protected float y;
    protected float radius;
    protected float angle;
    protected Sprite sprite;
    protected float speed;

    public Map getMap(){
        return map;
    }

    public void setMap(Map map){
        this.map = map;
    }

    public float getX(){
        return x;
    }

    public void setX(float x){
        this.x = x;
    }

    public float getY(){
        return y;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getRadius(){
        return radius;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public float getAngle(){
        return angle;
    }

    public Sprite getSprite(){
        return sprite;
    }

    public float getSpeed(){
        return speed;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }
}
