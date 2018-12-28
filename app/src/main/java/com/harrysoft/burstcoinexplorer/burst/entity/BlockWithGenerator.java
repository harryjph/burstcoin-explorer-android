package com.harrysoft.burstcoinexplorer.burst.entity;

import burst.kit.entity.response.BlockResponse;

public class BlockWithGenerator {
    private final BlockResponse block;
    private final AccountWithRewardRecipient generator;

    public BlockWithGenerator(BlockResponse block, AccountWithRewardRecipient generator) {
        this.block = block;
        this.generator = generator;
    }

    public BlockResponse getBlock() {
        return block;
    }

    public AccountWithRewardRecipient getGenerator() {
        return generator;
    }
}
