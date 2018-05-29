package com.harrysoft.burstcoinexplorer.test;

import com.harrysoft.burstcoinexplorer.util.FileSizeUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FileSizeUtilsTest {
    @Test
    public void testFileSizeFormatting() {
        assertEquals("123 Bytes", FileSizeUtils.formatFileSize(new BigInteger("123")));
        assertEquals("1.205 KB", FileSizeUtils.formatFileSize(new BigInteger("1234")));
        assertEquals("12.056 KB", FileSizeUtils.formatFileSize(new BigInteger("12345")));
        assertEquals("120.562 KB", FileSizeUtils.formatFileSize(new BigInteger("123456")));
        assertEquals("1.177 MB", FileSizeUtils.formatFileSize(new BigInteger("1234567")));
        assertEquals("11.774 MB", FileSizeUtils.formatFileSize(new BigInteger("12345678")));
        assertEquals("117.738 MB", FileSizeUtils.formatFileSize(new BigInteger("123456789")));
        assertEquals("1.150 GB", FileSizeUtils.formatFileSize(new BigInteger("1234567891")));
        assertEquals("11.498 GB", FileSizeUtils.formatFileSize(new BigInteger("12345678912")));
        assertEquals("114.978 GB", FileSizeUtils.formatFileSize(new BigInteger("123456789123")));
        assertEquals("1.123 TB", FileSizeUtils.formatFileSize(new BigInteger("1234567891234")));
        assertEquals("11.228 TB", FileSizeUtils.formatFileSize(new BigInteger("12345678912345")));
        assertEquals("112.283 TB", FileSizeUtils.formatFileSize(new BigInteger("123456789123456")));
        assertEquals("1122.833 TB", FileSizeUtils.formatFileSize(new BigInteger("1234567891234567")));
        assertEquals("11228.330 TB", FileSizeUtils.formatFileSize(new BigInteger("12345678912345678")));
        assertEquals("112283.296 TB", FileSizeUtils.formatFileSize(new BigInteger("123456789123456789")));
    }
}
