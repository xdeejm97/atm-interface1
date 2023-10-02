package org.example.atmApp;

import org.example.model.DepositTransactionEntity;
import org.example.model.UserEntity;
import org.example.model.WithdrawTransactionEntity;
import org.example.serivce.ATMManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class AtmApp {

    private final Scanner scanner = new Scanner(System.in);

    private final ATMManager atmManager = new ATMManager();

    private final PrintStream printStream = new PrintStream(System.out);
    private static final String JDBC_URL = "jdbc:mysql://localhost:3307/atm_interface";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";


    public void run(SessionFactory sessionFactory) {
        printStream.println("WELCOME");
        boolean shutdownMenu = false;

        while (!shutdownMenu) {
            printStream.println("MAIN MENU" + "\n" + "Choose operation: ");
            printStream.println("r - Register");
            printStream.println("l - Login");
            printStream.println("e - Exit");

            String operationCharacter = scanner.next();
            if (operationCharacter.equals("r")) {
                tryToRegister(sessionFactory);
            }
            String loggedOperation;
            if (operationCharacter.equals("l")) {
                System.out.println("Write your ID: ");
                String scannerId = scanner.next();
                System.out.println("Write your password: ");
                String scannerPassword = scanner.next();

                boolean login = tryToLogIn(scannerId, scannerPassword);

                if (!login) {
                    continue;
                }

                boolean loggedOut = false;

                while (!loggedOut) {
                    printStream.println("d - Deposit");
                    printStream.println("w - Withdraw");
                    printStream.println("b - Balance Account");
                    printStream.println("h - History of transactions");
                    printStream.println("e - Exit");

                    loggedOperation = scanner.next();

                    switch (loggedOperation) {
                        case "d":
                            tryToDeposit(sessionFactory);
                            break;
                        case "w":
                            tryToWithdraw(sessionFactory);
                            break;
                        case "b":
                            tryToShowBalance(sessionFactory);
                            break;
                        case "h":
                            tryToListHistoryOfTransactions(sessionFactory);
                            break;
                        case "e":
                            loggedOut = true;
                            break;
                        default:
                            printStream.println("Wrong operation! Try again!");
                            break;

                    }
                }

            }

            if (operationCharacter.equals("e")) {
                shutdownMenu = true;
            }


        }
    }

    private void tryToRegister(SessionFactory sessionFactory) {
        UserEntity userEntity = new UserEntity();
        printStream.println("Write your user ID");
        String userId = scanner.next();
        printStream.println("Write your user password ");
        String passwordID = scanner.next();

        atmManager.savingUser(sessionFactory, userId, passwordID, userEntity);
        printStream.println("You has been registered with your data: " + "id:" + userId + "\t" + "password:" + passwordID +
                "\nKeep that data and don't share it with anyone!");
    }

    private boolean tryToLogIn(String userID, String password) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT user_password FROM users WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {

                String hashedPasswordFromDB = resultSet.getString("user_password");

                boolean passwordsMatch = password.equals(hashedPasswordFromDB);

                resultSet.close();
                preparedStatement.close();
                connection.close();

                if (!passwordsMatch) {
                    printStream.println("You provided wrong data!");
                } else {
                    printStream.println("You are logged in!" + "\n" + "Choose which operations do you need");
                }
                return passwordsMatch;
            } else {

                resultSet.close();
                preparedStatement.close();
                connection.close();
                printStream.println("You provided wrong data!");

                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }

    private void tryToDeposit(SessionFactory sessionFactory) {
        DepositTransactionEntity depositTransaction = new DepositTransactionEntity();
        printStream.println("How much money do you want to deposit?");
        BigDecimal deposit = scanner.nextBigDecimal();

        atmManager.savingDeposit(sessionFactory, deposit, depositTransaction);

        printStream.println("Deposit has been done in amount of: " + deposit);

    }

    private void tryToWithdraw(SessionFactory sessionFactory) {
        WithdrawTransactionEntity withdrawTransaction = new WithdrawTransactionEntity();
        printStream.println("How much money do you want to withdraw?");
        BigDecimal withdraw = scanner.nextBigDecimal();

        atmManager.savingWithdraw(sessionFactory, withdraw, withdrawTransaction);

        printStream.println("Withdraw has been done.");
    }

    private void tryToShowBalance(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();

        try {
            String hqlDeposit = "SELECT deposit FROM DepositTransactionEntity";
            Query<BigDecimal> queryDeposit = session.createQuery(hqlDeposit, BigDecimal.class);
            List<BigDecimal> listDeposit = queryDeposit.list();

            String hqlWithdraw = "SELECT withdraw FROM WithdrawTransactionEntity";
            Query<BigDecimal> queryWithdraw = session.createQuery(hqlWithdraw, BigDecimal.class);
            List<BigDecimal> listWithdraw = queryWithdraw.list();

            BigDecimal sumDeposit = BigDecimal.valueOf(0);

            for (BigDecimal numberDeposit : listDeposit) {
                sumDeposit = sumDeposit.add(numberDeposit);
            }

            BigDecimal sumWithdraw = BigDecimal.valueOf(0);

            for (BigDecimal numberWithdraw : listWithdraw) {
                sumWithdraw = sumWithdraw.add(numberWithdraw);
            }
            System.out.println("Balance: " + (sumDeposit.subtract(sumWithdraw)));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void tryToListHistoryOfTransactions(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();

        try {
            String hqlDeposit = "SELECT deposit FROM DepositTransactionEntity";
            Query<BigDecimal> queryDeposit = session.createQuery(hqlDeposit, BigDecimal.class);
            List<BigDecimal> listDeposit = queryDeposit.list();

            String hqlWithdraw = "SELECT deposit FROM DepositTransactionEntity";
            Query<BigDecimal> queryWithdraw = session.createQuery(hqlWithdraw, BigDecimal.class);
            List<BigDecimal> listWithdraw = queryWithdraw.list();

            for (BigDecimal depositsHistory : listDeposit) {
                printStream.println("+" + depositsHistory);
            }

            for (BigDecimal withdrawHistory : listWithdraw) {
                printStream.println("-" + withdrawHistory);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
