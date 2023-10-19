package com.junglesoftware.marketplace.common.response;

public enum ServiceStatusEnumeration {
    OK (0),
    BUSINESS_ERROR(1),
    TECHNICAL_ERROR(2);

    private int value;

    ServiceStatusEnumeration(int value) {this.value = value;}

    int getValue() {return this.value;}
}
