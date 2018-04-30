package com.harrysoft.burstcoinexplorer.burst.api;

import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.burst.entity.AccountTransactions;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.BlockExtra;
import com.harrysoft.burstcoinexplorer.burst.entity.NetworkStatus;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;

import java.math.BigInteger;

import io.reactivex.Single;

public interface BurstAPIService {
    Single<Block[]> fetchRecentBlocks();
    Single<NetworkStatus> fetchNetworkStatus();
    Single<Block> fetchBlockByHeight(BigInteger blockHeight);
    Single<Block> fetchBlockByID(BigInteger blockID);
    Single<BlockExtra> fetchBlockExtra(BigInteger blockID);
    Single<Account> fetchAccount(BigInteger accountID);
    Single<AccountTransactions> fetchAccountTransactions(BigInteger accountID);
    Single<Transaction> fetchTransaction(BigInteger transactionID);
}
