package com.co.financialservice.model.transactionmodel;
import com.co.financialservice.model.accountmodel.AccountModel;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionModel {

    private Long transaccionId;
    private LocalDate fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldo;
    private Long cuentaId;
}
