package com.co.financialservice.jpa.repository;

import com.co.financialservice.jpa.entity.TransactionEntity;
import com.co.financialservice.jpa.helper.AdapterOperations;
import com.co.financialservice.jpa.helper.TransactionMapper;
import com.co.financialservice.model.transactionmodel.TransactionModel;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepositoryAdapter extends AdapterOperations<TransactionModel, TransactionEntity, Long, TransactionRepository>

{
    private final TransactionMapper transactionMapper;

    public TransactionRepositoryAdapter(TransactionRepository repository, ObjectMapper mapper, TransactionMapper transactionMapper) {
        super(repository, mapper, d -> mapper.map(d, TransactionModel.class));
        this.transactionMapper = transactionMapper;
    }
    @Transactional
    public TransactionModel save(TransactionModel transactionModel) {
        TransactionEntity transactionEntity = transactionMapper.mapToEntity(transactionModel);
        return mapper.map(repository.save(transactionEntity), TransactionModel.class);
    }
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public TransactionModel findByTransaccionId(Long transactionId) {
         Optional<TransactionEntity> transactionEntity =repository.findById(transactionId);
         return transactionEntity.map(transactionMapper::mapToModel).orElse(null);
    }

    @Transactional
    public TransactionModel updateByTransactionId(Long transactionId, TransactionModel transactionModel) {
        TransactionEntity existingTransaction = repository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if(!existingTransaction.getCuenta().getAccountId().equals(transactionModel.getCuentaId())) {
            throw new RuntimeException("Este dato no puede ser modificado");
        }

        updateTransactionEntity(transactionModel, existingTransaction);
        return mapper.map(repository.save(existingTransaction), TransactionModel.class);

    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public List<TransactionModel> findAllTransactionByAccountId(Long accountId) {
        List<Optional<TransactionEntity>> transactionEntityList = repository.findAllByCuenta_AccountId(accountId);
        return transactionEntityList.stream().map(transactionEntity -> transactionEntity.map(transactionMapper::mapToModel).orElse(null)).toList();
    }

    private void updateTransactionEntity(TransactionModel transactionModel, TransactionEntity existingTransaction) {
        existingTransaction.setFecha(transactionModel.getFecha());
        existingTransaction.setTipoMovimiento(transactionModel.getTipoMovimiento());
        existingTransaction.setValor(transactionModel.getValor());
        existingTransaction.setSaldo(transactionModel.getSaldo());
    }
    @Transactional
    public void deleteByTransactionId(Long transactionId) {
        repository.deleteByTransaccionId(transactionId);
    }
}
