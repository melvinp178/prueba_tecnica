package com.co.financialservice.jpa.repository;

import com.co.financialservice.jpa.entity.AccountEntity;
import com.co.financialservice.jpa.helper.AccountMapper;
import com.co.financialservice.jpa.helper.AdapterOperations;
import com.co.financialservice.model.accountmodel.AccountModel;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class AccountRepositoryAdapter extends AdapterOperations<AccountModel, AccountEntity, Long, AccountRepository>

{
    private final AccountMapper accountMapper;

    public AccountRepositoryAdapter(AccountRepository repository, ObjectMapper mapper, AccountMapper accountMapper) {

        super(repository, mapper, d -> mapper.map(d, AccountModel.class));
        this.accountMapper = accountMapper;
    }
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public AccountModel findByAccountId(Long accountId) {
        Optional<AccountEntity> accountEntity = repository.findById(accountId);
           return accountEntity.map(accountMapper::mapToModel).orElse(null);
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public AccountModel findByAccountNumber(String accountNumber) {
        AccountEntity accountEntity = repository.findByNumeroCuenta(accountNumber);
        return accountEntity==null ? null : accountMapper.mapToModel(accountEntity);
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public AccountModel updateByAccountId(Long accountId, AccountModel accountModel) {
        AccountEntity existingAccount = repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        updateAccountEntity(accountModel, existingAccount);
        return accountMapper.mapToModel(repository.save(existingAccount));

    }
    private void updateAccountEntity(AccountModel accountModel, AccountEntity existingAccount) {
        existingAccount.setEstado(accountModel.getEstado());
        existingAccount.setTipoCuenta(accountModel.getTipoCuenta());
        existingAccount.setSaldoInicial(accountModel.getSaldoInicial());
        existingAccount.setSaldoDisponible(accountModel.getSaldoDisponible());
        existingAccount.setNumeroCuenta(accountModel.getNumeroCuenta());
    }

    public void deleteByAccountId(Long accountId) {
        AccountEntity existingAccount = repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setEstado("false");
        repository.save(existingAccount);
    }
}
