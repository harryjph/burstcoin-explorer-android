package com.harrysoft.burstcoinexplorer.burst.service;

import com.harrysoft.burstcoinexplorer.burst.entity.Account;
import com.harrysoft.burstcoinexplorer.burst.entity.Block;
import com.harrysoft.burstcoinexplorer.burst.entity.SearchResult;
import com.harrysoft.burstcoinexplorer.burst.entity.Transaction;

import java.math.BigInteger;
import java.util.List;

import io.reactivex.Single;

public interface BurstBlockchainService {
    Single<List<Block>> fetchRecentBlocks();
    Single<Block> fetchBlockByHeight(BigInteger blockHeight);
    Single<Block> fetchBlockByID(BigInteger blockID);
    Single<Account> fetchAccount(BigInteger accountID);
    Single<BigInteger> fetchAccountRewardRecipient(BigInteger accountID);
    Single<List<BigInteger>> fetchAccountTransactions(BigInteger accountID);
    Single<Transaction> fetchTransaction(BigInteger transactionID);
    Single<SearchResult> determineSearchRequestType(String searchRequest);
}
