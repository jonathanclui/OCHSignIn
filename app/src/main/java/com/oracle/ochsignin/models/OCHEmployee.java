package com.oracle.ochsignin.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by jonathanlui on 7/5/15.
 */
@ParseClassName("OCHEmployee")
public class OCHEmployee extends ParseObject {

    public int getEmployeeId() {
        return getInt("employeeId");
    }

    public void setEmployeeId(int value) {
        put("employeeId", value);
    }

    public String getEmployeeName() {
        return getString("employeeName");
    }

    public void setEmployeeName(String value) {
        put("employeeName", value);
    }

    public boolean getAttended() {
        return getBoolean("attended");
    }

    public void setAttended(boolean value) {
        put("attended", value);
    }
}
