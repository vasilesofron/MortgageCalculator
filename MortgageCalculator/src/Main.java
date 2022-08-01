/*
This is a basic mortgage calculator.
The user is expected to run the calculator from the IDE and input the requested values in the console.
After the user has successfully entered the data, a .cvs file is created.
The file can be opened (ex: Excel) where all the information is provided.

The Monthly Payment Formula used in this program is the one used here:
https://en.wikipedia.org/wiki/Mortgage_calculator

-VS
 */


import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    //Static variables
    static final int MONTHS_IN_YEARS = 12;
    static final int PERCENT = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CsvWriter csvWriter;

        try {
            FileWriter writer = new FileWriter(FileProvider.getFile());
            csvWriter = new CsvWriter(writer);
            csvWriter.writeHeader();
        }catch (IOException e){
            System.out.println("Some error occurred when initializing the CsvWriter: " + e.getMessage());
            return;
        }
        int amount;
        int period;
        double interestRate;

        System.out.println("Please enter the amount: ");

        try {
            amount = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
            System.out.println("The amount is mandatory to be numeric!");
            return;
        }

        System.out.println("Please enter the loan period in years: ");

       try {
           period = scanner.nextInt();
       }catch (NumberFormatException e){
           System.out.println("The loan period is mandatory to be numeric!");
           return;
       }

        System.out.println("Please enter the annual interest rate: ");
        try {
            interestRate = scanner.nextDouble();
        }catch (NumberFormatException e){
            System.out.println("The annual interest rate is mandatory to be numeric!");
            return;
        }

        double balance = amount;
        for(int month = 1; month <= period * MONTHS_IN_YEARS; month += 1){
            double lastMonthBalance = balance;
            double monthlyMortgage = calculateMortgage(amount, period, interestRate);
            double monthlyInterest = calculateInterest(lastMonthBalance, interestRate);
            double paidAmount = monthlyMortgage - monthlyInterest;

            balance = (lastMonthBalance - paidAmount) < 0 ? 0 : lastMonthBalance - paidAmount;

            try {
                csvWriter.writeRecord(month, monthlyMortgage,balance, monthlyInterest, paidAmount);
            } catch (IOException e) {
                System.out.println("Error while writing the csv file: " + e.getMessage());
            }
        }

        try {
            csvWriter.closeFile();
            System.out.println("Task finished.");
        }catch (IOException e){
            System.out.println("Something went wrong when trying to close the csv file: " + e.getMessage());
        }

    }


    // Calculation of the monthly rate
    private static double calculateMortgage(int amount, int period, double interestRate){
        double monthlyRate = interestRate / PERCENT / MONTHS_IN_YEARS;
        return (monthlyRate * amount / (1 - Math.pow(1 + monthlyRate, -period * MONTHS_IN_YEARS)));
    }

    // Calculation of the monthly interest rate
    private static double calculateInterest(double balance, double interestRate){
        double interestPerYear = balance * interestRate / PERCENT;
        return interestPerYear / MONTHS_IN_YEARS;
    }
}