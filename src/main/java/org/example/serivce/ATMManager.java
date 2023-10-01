package org.example.serivce;

import org.example.model.DepositTransactionEntity;
import org.example.model.UserEntity;
import org.example.model.WithdrawTransactionEntity;
import org.example.repository.AtmRepository;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;

public class ATMManager {
    public void savingUser(SessionFactory sessionFactory, String scannerUserId, String scannerUserPin,UserEntity userEntity) {
        AtmRepository atmRepository = new AtmRepository(sessionFactory.createEntityManager());
        atmRepository.saveToDatabaseUser(scannerUserId, scannerUserPin,userEntity);
    }

    public void savingDeposit(SessionFactory sessionFactory, BigDecimal scannerDeposit, DepositTransactionEntity deposit) {
        AtmRepository atmRepository = new AtmRepository(sessionFactory.createEntityManager());
        atmRepository.saveToDatabaseDeposit(scannerDeposit, deposit);
    }

    public void savingWithdraw(SessionFactory sessionFactory, BigDecimal scannerWithdraw, WithdrawTransactionEntity withdraw) {
        AtmRepository atmRepository = new AtmRepository(sessionFactory.createEntityManager());
        atmRepository.saveToDatabaseWithdraw(scannerWithdraw, withdraw);
    }

//    public void showBalance(SessionFactory sessionFactory){
//        AtmRepository atmRepository = new AtmRepository(sessionFactory.createEntityManager());
//        atmRepository.showBalance();
//    }


}
