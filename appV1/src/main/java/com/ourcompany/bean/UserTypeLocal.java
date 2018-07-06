package com.ourcompany.bean;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2018/4/2 21:04
 * Des    :{
     "userType": [
         {
             "typeName": "暂无",
             "type": 0
         },
         {
             "typeName": "业主",
             "type": 1
         },
         {
             "typeName": "设计师",
             "type": 2
         },
         {
             "typeName": "装修技工",
             "type": 3
         },
         {
             "typeName": "装修公司",
             "type": 4
         },
         {
             "typeName": "商家",
             "type": 5
         }
     ]
 }
 */

public enum  UserTypeLocal {
    None(0),Owner(1),Designer(2),Worker(3),Company(4),Business(5);
    private int value; //编码标识
    private UserTypeLocal(int value){
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
