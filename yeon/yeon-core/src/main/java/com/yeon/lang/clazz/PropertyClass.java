package com.yeon.lang.clazz;

import com.yeon.lang.*;
import com.yeon.lang.Class;

import java.util.Arrays;
import java.util.List;

public class PropertyClass implements com.yeon.lang.Class {
    public static final String ID = "r2";

    @Override
    public Property getPropertyByName(String name) {
        if("domain".equalsIgnoreCase(name)) {

        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Property getPropertyById(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Property> getProperties() {
        // domain
        // range
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getId() {
        return ID;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getTypeClassId() {
        return ClassClass.ID;
    }

    @Override
    public Class getTypeClass() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getPropertyObjectByName(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getPropertyObjectById(String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<LocalValue> getNames() {
        return Arrays.asList(new LocalValue("en", "class"));
    }

    @Override
    public List<LocalValue> getDescriptions() {
        return Arrays.asList(new LocalValue("en", "class"));
    }

    @Override
    public ResourceList getPrototypes() {
        // TODO ClassClass를 돌려주는 로직 추가.
        return ResourceList.EMPTY;
    }
}
