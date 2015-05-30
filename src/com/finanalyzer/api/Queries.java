package com.finanalyzer.api;

import java.util.Set;

public class Queries
{

    public static SimpleQuery create(String qCode) {
        return new SimpleQuery(qCode);
    }
    
    public static MultisetQuery create(Set<String> qCodes) {
        return new MultisetQuery(qCodes);
    }
    

}
