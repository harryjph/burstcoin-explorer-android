package com.harrysoft.burstcoinexplorer.test;

import android.support.test.runner.AndroidJUnit4;

import com.harrysoft.burstcoinexplorer.burst.utils.TransactionTypeUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

@RunWith(AndroidJUnit4.class)
public class TransactionTypeUtilsTest {
    @Test
    public void testTransactionTypes() {
        Map<Byte, Integer> transactionTypes = TransactionTypeUtils.getTransactionTypes();

        assertNotNull(transactionTypes);
        assertNotSame(0, transactionTypes.size());

        for (Map.Entry<Byte, Integer> type : transactionTypes.entrySet()) {
            assertNotNull(type.getKey());
            assertNotNull(type.getValue());
        }
    }

    @Test
    public void testTransactionSubTypes() {
        Map<Byte, Map<Byte, Integer>> transactionTypes = TransactionTypeUtils.getTransactionSubTypes();

        assertNotNull(transactionTypes);

        assertNotSame(0, transactionTypes.size());

        for (Map.Entry<Byte, Map<Byte, Integer>> type : transactionTypes.entrySet()) {
            Map<Byte, Integer> transactionSubTypes = type.getValue();

            assertNotNull(type.getKey());
            assertNotNull(transactionSubTypes);
            assertNotSame(0, transactionSubTypes.size());

            for (Map.Entry<Byte, Integer> subType : transactionSubTypes.entrySet()) {
                assertNotNull(subType.getKey());
                assertNotNull(subType.getValue());
            }
        }
    }
}
