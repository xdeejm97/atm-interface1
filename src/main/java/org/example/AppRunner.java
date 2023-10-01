package org.example;

import org.example.atmApp.AtmApp;
import org.example.model.DepositTransactionEntity;
import org.example.model.WithdrawTransactionEntity;
import org.example.model.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class AppRunner {
    public static void main(String[] args) {

        AtmApp atmApp = new AtmApp();
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(UserEntity.class)
                .addAnnotatedClass(DepositTransactionEntity.class)
                .addAnnotatedClass(WithdrawTransactionEntity.class)
                .buildSessionFactory();

        atmApp.run(sessionFactory);


//        StandardServiceRegistry ssr =  new StandardServiceRegistryBuilder()
//                .configure("hibernate.cfg.xml").build();
//        Metadata metadata = new MetadataSources(ssr).getMetadataBuilder().build();
//        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        UserEntity user = new UserEntity();
//        System.out.println("Create new user");
//        String userIdScanner = scanner.nextLine();
//        int userPinScanner = scanner.nextInt();
//        user.setUserId(userIdScanner);
//        user.setUserPin(userPinScanner);
//
//        session.save(user);
//        transaction.commit();

    }
}
