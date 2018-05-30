package com.harrysoft.burstcoinexplorer.burst.explorer;

import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;

import java.math.BigInteger;

@Deprecated
public interface BurstExplorer {
    void viewBlockDetailsByBlock(Block block);
    void viewBlockDetailsByNumber(BigInteger blockNumber);
    void viewBlockDetailsByID(BigInteger blockID);
    void viewBlockExtraDetails(BigInteger blockID);
    void viewAccountDetails(BigInteger accountID);
    void viewAccountTransactions(BigInteger accountID);
    void viewTransactionDetailsByTransaction(Transaction transaction);
    void viewTransactionDetailsByID(BigInteger transactionID);
}
