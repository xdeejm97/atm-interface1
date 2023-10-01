package org.example.repository;

import org.example.model.DepositTransactionEntity;
import org.example.model.UserEntity;
import org.example.model.WithdrawTransactionEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.Scanner;

public class AtmRepository {

    Scanner scanner = new Scanner(System.in);


    private EntityManager entityManager;

    public AtmRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UserEntity saveToDatabaseUser(String userIdScanner, String userPinScanner, UserEntity userEntity) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            userEntity.setUserId(userIdScanner);
            userEntity.setUserPin(userPinScanner);
            entityManager.persist(userEntity);
            transaction.commit();
            return userEntity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public DepositTransactionEntity saveToDatabaseDeposit(BigDecimal depositScanner, DepositTransactionEntity depositTransaction) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            depositTransaction.setDeposit(depositScanner);
            entityManager.persist(depositTransaction);
            transaction.commit();
            return depositTransaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public WithdrawTransactionEntity saveToDatabaseWithdraw(BigDecimal withdrawScanner, WithdrawTransactionEntity withdrawTransaction) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            withdrawTransaction.setWithdraw(withdrawScanner);
            entityManager.persist(withdrawTransaction);
            transaction.commit();
            return withdrawTransaction;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public  listAllTransactions(){
//
//        return entityManager.createQuery("from ", .class).getResultList();
//    }

}