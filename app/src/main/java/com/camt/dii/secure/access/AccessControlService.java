// com/camt/dii/secure/access/AccessControlService.java
package com.camt.dii.secure.access;

import com.camt.dii.secure.common.Floor;

public interface AccessControlService {
    boolean grantAccess(String facadeId, Floor floor, String room, String token);
}