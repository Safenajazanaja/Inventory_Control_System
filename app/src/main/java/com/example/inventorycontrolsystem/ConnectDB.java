package com.example.inventorycontrolsystem;

import com.adedom.library.Dru;

import java.sql.Connection;

public class ConnectDB {

    public  static  String BASE_URL="172.20.10.3";
    public  static  String BASE_IMAGE="http://"+BASE_URL+"/ics/images/";

    public static Connection getconnection() {
        return Dru.connection(BASE_URL, "drusp", "drusp", "ics");

    }

}

