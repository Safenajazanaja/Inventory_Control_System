package com.example.inventorycontrolsystem;

import com.adedom.library.Dru;

import java.sql.Connection;

public class ConnectDB {

    public  static  String BASE_URL="192.168.1.7";
    public  static  String BASE_IMAGE="http://"+BASE_URL+"/ics/images/";

    public static Connection getconnection() {
        return Dru.connection(BASE_URL, "drusp", "drusp", "ics");

    }

}

