package com.harry1453.burst.explorer.service;

import com.harry1453.burst.explorer.entity.Account;
import com.harry1453.burst.explorer.entity.Block;
import com.harry1453.burst.explorer.entity.SearchResult;
import com.harry1453.burst.explorer.entity.Transaction;

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
