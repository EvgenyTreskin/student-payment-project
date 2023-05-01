package edu.studentorder.DAO;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.stream.Collector;

public class SimpleRunner {
    public static void main(String[] args) {
        SimpleRunner simpleRunner = new SimpleRunner();
        simpleRunner.runTests();
        
    }

    private void runTests() {
        try {
            Class<?> cl = Class.forName("edu.studentorder.DAO.DictionaryDaoImplTest");

            Constructor<?> cst = cl.getDeclaredConstructor();
            Object entity = cst.newInstance();

            Method[] methods = cl.getMethods();
            for (Method m: methods) {
                Test ann = m.getAnnotation(Test.class);
                if (ann!=null){
                    m.invoke(entity);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
