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
        assertEquals("123 Bytes (100%)", FileSizeUtils.formatBlockSize(new BigInteger("123"), 1d));
        assertEquals("1.205 KB (50%)", FileSizeUtils.formatBlockSize(new BigInteger("1234"), 0.5d));
        assertEquals("12.056 KB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("12345"), 0d));
        assertEquals("120.562 KB (1%)", FileSizeUtils.formatBlockSize(new BigInteger("123456"), 0.01d));
        assertEquals("1.177 MB (0.1%)", FileSizeUtils.formatBlockSize(new BigInteger("1234567"), 0.001d));
        assertEquals("11.774 MB (0.01%)", FileSizeUtils.formatBlockSize(new BigInteger("12345678"), 0.0001d));
        assertEquals("117.738 MB (0.001%)", FileSizeUtils.formatBlockSize(new BigInteger("123456789"), 0.00001d));
        assertEquals("1.150 GB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("1234567891"), 0.000005d));
        assertEquals("11.498 GB (1.111%)", FileSizeUtils.formatBlockSize(new BigInteger("12345678912"), 0.01111d));
        assertEquals("114.978 GB (1.111%)", FileSizeUtils.formatBlockSize(new BigInteger("123456789123"), 0.011115d));
        assertEquals("1.123 TB (1.123%)", FileSizeUtils.formatBlockSize(new BigInteger("1234567891234"), 0.01123d));
        assertEquals("11.228 TB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("12345678912345"), 0));
        assertEquals("112.283 TB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("123456789123456"), 0));
        assertEquals("1122.833 TB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("1234567891234567"), 0));
        assertEquals("11228.330 TB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("12345678912345678"), 0));
        assertEquals("112283.296 TB (0%)", FileSizeUtils.formatBlockSize(new BigInteger("123456789123456789"), 0));
    }
}
