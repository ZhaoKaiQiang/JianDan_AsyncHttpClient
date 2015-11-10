package com.socks.jiandan;

import android.test.InstrumentationTestCase;

import com.socks.jiandan.base.JDApplication;

/**
 * Created by zhaokaiqiang on 15/4/27.
 */
public class TestClass extends InstrumentationTestCase {

    public TestClass() {

    }

    public void testGetCommentators() throws Exception {
        JDApplication.getContext();
    }

}
