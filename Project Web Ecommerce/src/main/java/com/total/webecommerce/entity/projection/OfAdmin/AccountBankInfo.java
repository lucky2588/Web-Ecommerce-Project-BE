package com.total.webecommerce.entity.projection.OfAdmin;

import com.total.webecommerce.entity.AccountBank;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link AccountBank}
 */
public interface AccountBankInfo {
    Integer getId();

    String getNameAccount();

    String getNumberAccount();

    String getBankBranch();


    @RequiredArgsConstructor
    class AccountBankInfoImpl implements AccountBankInfo {
        private final AccountBank acc;
        @Override
        public Integer getId() {
            return acc.getId();
        }


        @Override
        public String getNameAccount() {
            return acc.getNameAccount();
        }

        @Override
        public String getNumberAccount() {
            return acc.getNumberAccount();
        }

        @Override
        public String getBankBranch() {
            return acc.getBankBranch();
        }
    }
    static AccountBankInfo of(AccountBank acc){
        return new AccountBankInfoImpl(acc);
    }
}