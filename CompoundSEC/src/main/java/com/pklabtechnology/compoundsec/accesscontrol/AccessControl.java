package com.pklabtechnology.compoundsec.accesscontrol;

interface AccessControl {
    void grantAccess();
    void denyAccess();
    void requestAccess();
    void checkAccess();
}