package com.lemon.api.auto10.Util;


import org.testng.Assert;

public class AssertUtil {
    /**断言实际结果与预期结果是否一致
     * @param expectedResponseData
     * @param actualResponseData
     */
    public static String assertEquals(String expectedResponseData, String actualResponseData) {

        String result="通过";
        try {
            expectedResponseData= VariableUtil.replaceVariable(expectedResponseData);
            Assert.assertEquals(actualResponseData,expectedResponseData);

        }catch (Throwable e){
            result=actualResponseData;

        }

        return result;
    }
}
