package org.mapdb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOError;

/*
 * https://github.com/jankotek/MapDB/issues/78
 *
 * @author Nandor Kracser
 */
public class Issue78Test {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IOError.class, timeout = 10000)
    public void testIssue() {
        DB db = DBMaker.tempFileDB().make();
        HTreeMap<String, NotSerializable> usersMap = db.hashMap("values");
        usersMap.put("thisKillsTheAsyncWriteThread", new NotSerializable());
        db.commit();
    }

    class NotSerializable {
    }
}
