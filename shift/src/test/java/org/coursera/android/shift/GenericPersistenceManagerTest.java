package org.coursera.android.shift;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GenericPersistenceManagerTest {

    public class TestObject {
        private String privateField = "private";
        public String publicField = "public";

        public TestObject() {
        }

        public TestObject(String publicParameter) {
            publicField = publicParameter;
        }

        public String getPublicField() {
            return publicField;
        }

        public String getPrivateField() {
            return privateField;
        }
    }

    @Test
    public void testPutObject() throws InterruptedException {
        ShiftPersistenceManager persistenceManager = new ShiftPersistenceManager(RuntimeEnvironment.application);
        persistenceManager.putObject("test", new TestObject("testField"));

        TestObject test = persistenceManager.getObject("test", TestObject.class, new TestObject());

        Assert.assertNotNull(test);
        Assert.assertEquals("private", test.getPrivateField());
        Assert.assertEquals("testField", test.getPublicField());
    }
}

