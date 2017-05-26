package com.example.treemapp;

/**
 * Created by 5dv173 on 5/24/17.
 */

public class CheckboxModel {
    String name;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

    CheckboxModel(String name, int value){
        this.name = name;
        this.value = value;
    }
    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }

}
