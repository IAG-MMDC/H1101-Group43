/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Isabelle Angeli Gallardo
 * Jam Rosales
 */
public class MotorPH {

    //CSV Filepath
    static String empCsv = "resources/MotorPH_Employee Data - Employee Details.csv";
    static String attCsv = "resources/MotorPH_Employee Data - Attendance Record.csv";

    //Process Payroll Constants
    public static final int MAX_WORKING_MINUTES = 540;
    public static final int LUNCH_BREAK_MINS = 60;
    public static final int MINUTES_PER_HOUR = 60;

    //Compute SSS Contribution Constants
    public static final int SSS_MIN_CONTRIBUTION = 135;
    public static final int SSS_MAX_CONTRIBUTION = 1125;
    public static final int SSS_LOWEST_SALARY_BRACKET = 3250;
    public static final int SSS_HIGHEST_SALARY_BRACKET = 24750;
    public static final int SSS_SALARY_INCREMENT = 500;
    public static final double SSS_CONTRIBUTION_INCREMENT = 22.50;

    //Compute PagIBIG Contribution Constants
    public static final int PAGIBIG_MAX_CONTRIBUTION = 100;
    public static final double PAGIBIG_EMPLOYEE_MIN_CONTRIBUTION_RATE = .01;
    public static final double PAGIBIG_EMPLOYEE_MAX_CONTRIBUTION_RATE = .02;
    public static final int PAGIBIG_MIN_SALARY_THRESHOLD = 1000;
    public static final int PAGIBIG_MAX_SALARY_THRESHOLD = 1500;

    //Compute PhilHealth Contribution Constants 
    public static final double PHILHEALTH_PREMIUM_RATE = 0.03;
    public static final int PHILHEALTH_MIN_MONTHLY_BASIC_SALARY = 10_000;
    public static final int PHILHEALTH_MAX_MONTHLY_BASIC_SALARY = 60_000;
    public static final double PHILHEALTH_MIN_EMPLOYEE_SHARE = 150.00;
    public static final double PHILHEALTH_MAX_EMPLOYEE_SHARE = 900.00;
    public static final int PHILHEALTH_EMPLOYEE_SHARE = 2;

    //Compute Withholding Tax Contribution Constants
    public static final int MONTHLY_RATE_20832_AND_BELOW = 20_833;
    public static final int MONTHLY_RATE_20833_AND_BELOW_33_333 = 33_333;
    public static final int MONTHLY_RATE_33_333_TO_BELOW_66_667 = 66_667;
    public static final int MONTHLY_RATE_66_667_TO_BELOW_166667 = 166_667;
    public static final int MONTHLY_RATE_166667_TO_BELOW_666_667 = 666_667;

    public static final int MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_20_PERCENT_TAX_RATE = 20_833;
    public static final int MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_25_PERCENT_TAX_RATE = 33_333;
    public static final int MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_30_PERCENT_TAX_RATE = 66_667;
    public static final int MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_32_PERCENT_TAX_RATE = 166_667;
    public static final int MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_35_PERCENT_TAX_RATE = 666_667;

    public static final double TAX_RATE_20_PERCENT = .20;
    public static final double TAX_RATE_25_PERCENT = .25;
    public static final double TAX_RATE_30_PERCENT = .30;
    public static final double TAX_RATE_32_PERCENT = .32;
    public static final double TAX_RATE_35_PERCENT = .35;

    public static final int PLUS_2500 = 2500;
    public static final int PLUS_10833 = 10833;
    public static final double PLUS_40833_33 = 40833.33;
    public static final double PLUS_200833_33 = 200833.33;

    //Employee Details List
    static List<String> empNo = new ArrayList<>();
    static List<String> empLastName = new ArrayList<>();
    static List<String> empFirstName = new ArrayList<>();
    static List<String> empBirthday = new ArrayList<>();
    static List<Double> hourlyRate = new ArrayList<>();

    //Attendance Record List
    static List<String> attEmpNo = new ArrayList<>();
    static List<Integer> attMonth = new ArrayList<>();
    static List<Integer> attDay = new ArrayList<>();
    static List<Integer> attYear = new ArrayList<>();
    static List<LocalTime> attLogin = new ArrayList<>();
    static List<LocalTime> attLogout = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter username: ");
        String inputUser = scan.nextLine();

        System.out.print("Enter password: ");
        String inputPass = scan.nextLine();

        if (!inputUser.equals("employee") && !inputUser.equals("payroll_staff") && (!inputPass.equals("12345"))) {
            System.out.print("Incorrect username and password.");
            return;
        } else if (!inputUser.equals("employee") && !inputUser.equals("payroll_staff") || (!inputPass.equals("12345"))) {
            System.out.println("Incorrect username or password.");
            return;
        }

        readEmployeeDetails();
        readAttendanceRecord();

        if (inputUser.equals("employee")) {
            employeeMenu(scan);
        } else {
            payrollStaffMenu(scan);
        }
    }

    /*.
    *Menu for employee.
    *Allows the user to look up employee details by employee number.
     */
    public static void employeeMenu(Scanner scan) {
        System.out.println("\n1. Enter employee number.");
        System.out.println("2. Exit the program.");
        System.out.print("Choose an option: ");
        String option = scan.nextLine();

        if (option.equals("1")) {
            int index = findEmployeeNo(scan); //Searches for the employee and display their details if found.
            if (index >= 0) {
                printEmployeeDetails(index);
            }
        } else {
            System.out.println("Exiting program...");
        }
    }

    /* 
    *Menu for payroll staff.
    *Allows payroll staff to process payroll for one employee or for all employees.
     */
    public static void payrollStaffMenu(Scanner scan) {
        System.out.println("\n1. Process Payroll.");
        System.out.println("2. Exit the program.");
        System.out.print("Choose an option: ");
        String option = scan.nextLine();

        if (option.equals("1")) {
            System.out.println("\n1. One employee.");
            System.out.println("2. All employees.");
            System.out.println("3. Exit the program.");
            System.out.print("Choose an option: ");
            option = scan.nextLine();

            switch (option) {
                case "1" -> {
                    int index = findEmployeeNo(scan); //Find and process payroll for one employee.
                    if (index >= 0) {
                        printEmployeeDetails(index);
                        processPayroll(empNo.get(index), hourlyRate.get(index));
                    }
                }
                case "2" -> {
                    for (int i = 0; i < empNo.size(); i++) { //Iterate through every employee and process their payroll.
                        printEmployeeDetails(i);
                        processPayroll(empNo.get(i), hourlyRate.get(i));
                    }
                }
                default ->
                    System.out.println("Exiting program...");
            }
        } else {
            System.out.println("Exiting program...");
        }
    }

    /*
    *Asks the user to enter the employee number, the program then searches the empNo list.
    *The method returns the index of the matching employee, and returns -1 if employee number is not found.
     */
    public static int findEmployeeNo(Scanner scan) {
        System.out.print("\nEnter Employee No.: ");
        String inputEmpNo = scan.nextLine();

        int index = -1; //

        for (int i = 0; i < empNo.size(); i++) {
            if (empNo.get(i).equals(inputEmpNo)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("Employee doesn't exist.");
        }
        return index;
    }

    //Prints employee details depending on the index being called.
    public static void printEmployeeDetails(int index) {
        System.out.println("\n======================================");
        System.out.println("Employee # : " + empNo.get(index));
        System.out.println("Employee Name : " + empLastName.get(index) + ", " + empFirstName.get(index));
        System.out.println("Birthday : " + empBirthday.get(index));
        System.out.println("======================================");
    }

    /* 
    *Reads employee details csv file using bufferedreader and filereader and stores it to its assigned lists.
    *Employee # --> added and stored to empNo list.
    *Last Name --> added and stored to empLastName list.
    *First Name --> added and stored to empFirstName list.
    *Birthday --> added and stored to empBirthday list.
    *Hourly rate --> added and stored to hourlyRate list.
     */
    public static void readEmployeeDetails() {
        try (BufferedReader br = new BufferedReader(new FileReader(empCsv))) {

            br.readLine();
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                if (currentLine.trim().isEmpty()) continue;

                String[] data = currentLine.split(",");

                empNo.add(data[0]);
                empLastName.add(data[1]);
                empFirstName.add(data[2]);
                empBirthday.add(data[3]);
                hourlyRate.add(Double.valueOf(data[data.length - 1]));
            }
        } catch (Exception e) {
            System.out.println("An error occurred while reading employee file.");
            e.printStackTrace();
        }
    }

    /*
    *Reads attendance record csv file using bufferedreader and filereader and stores it to its assigned lists.
    *Employee # --> added and stored to attEmpNo list.
    *Month --> added and stored to attMonth list.
    *Day --> added and stored to attDay list.
    *Year --> added and stored to attYear list.
    *Login --> added and stored to attLogin list.
    *Logout --> added and stored to attLogout list.
     */
    public static void readAttendanceRecord() {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(attCsv))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                attEmpNo.add(data[0]);

                String[] dateParts = data[3].split("/");
                attMonth.add(Integer.valueOf(dateParts[0]));
                attDay.add(Integer.valueOf(dateParts[1]));
                attYear.add(Integer.valueOf(dateParts[2]));

                attLogin.add(LocalTime.parse(data[4].trim(), timeFormat));
                attLogout.add(LocalTime.parse(data[5].trim(), timeFormat));
            }

        } catch (Exception e) {
            System.out.println("An error occurred while reading attendance file.");
            e.printStackTrace();
        }
    }
    
    //Iterates every month from June to December 2024 and computes the first cut-off payroll and second cut-off payroll for the given employee. 
    public static void processPayroll(String empNo, double hourlyRate) {
        for (int month = 6; month <= 12; month++) {
            double firstCutOffHours = 0;
            double secondCutOffHours = 0;

            //Scans the attendance records and skips attendance records that isn't matched with the employee being called.
            for (int i = 0; i < attEmpNo.size(); i++) {
                if (!attEmpNo.get(i).equals(empNo)) {
                    continue;
                }
                if (!attYear.get(i).equals(2024)) {
                    continue;
                }
                if (!attMonth.get(i).equals(month)) {
                    continue;
                }

                double hoursWorked = computeHoursWorked(attLogin.get(i), attLogout.get(i));

                //Adds hours into the correct pay period. 
                if (attDay.get(i) <= 15) {
                    firstCutOffHours += hoursWorked;
                } else {
                    secondCutOffHours += hoursWorked;
                }
            }
            //Prints payslip for each cut-off period.
            computeFirstCutOff(month, firstCutOffHours, hourlyRate);
            computeSecondCutOff(month, firstCutOffHours, secondCutOffHours, hourlyRate);
        }
    }

    /*
    *Displays the first cut-off (1st - 15th) payslip.
    *No deductions are applied, hence gross salary and net salary are the same amount.
    *Built-in month method is used to display the correct month name.
     */
    public static void computeFirstCutOff(int month, double firstCutOffHours, double hourlyRate) {
        Month monthName = Month.of(month);

        double firstCutOffGrossSalary = firstCutOffHours * hourlyRate;

        System.out.println("\nCutoff Date: " + monthName + " 1 to " + monthName + " 15");
        System.out.println("Total Hours Worked : " + firstCutOffHours);
        System.out.println("Gross Salary: " + firstCutOffGrossSalary);
        System.out.println("Net Salary: " + firstCutOffGrossSalary);
    }

    /*
    *Displays the second cut-off (16th- end of month) payslip.
    *Deductions are computed(SSS, Pag-IBIG, PhilHealth) against the combined gross salary of both cut-offs of the month.
    *Built-in month method is used to display the correct month name.
    *Built-in daysInMonth method is used to display the correct end of month.
     */
    public static void computeSecondCutOff(int month, double firstCutOffHours, double secondCutOffHours, double hourlyRate) {
        int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();
        Month monthName = Month.of(month);

        double firstCutOffGrossSalary = firstCutOffHours * hourlyRate;
        double secondCutOffGrossSalary = secondCutOffHours * hourlyRate;
        double grossMonthlySalary = firstCutOffGrossSalary + secondCutOffGrossSalary;

        double sss = computeSSSContribution(grossMonthlySalary);
        double pagIbig = computePagIBIGContribution(grossMonthlySalary);
        double philHealth = computePhilHealthContribution(grossMonthlySalary);
        double totalDeductions = computeTotalDeductions(sss, pagIbig, philHealth);
        double taxableIncome = computeTaxableIncome(grossMonthlySalary, totalDeductions);
        double withholdingTax = computeWithholdingTax(taxableIncome);
        double netSalary = taxableIncome - withholdingTax;

        System.out.println("\nCutoff Date: " + monthName + " 16 to " + monthName + " " + daysInMonth);
        System.out.println("Total Hours Worked : " + secondCutOffHours);
        System.out.println("Gross Salary: " + grossMonthlySalary);
        System.out.println("Deductions: " + totalDeductions);
        System.out.println("    SSS: " + sss);
        System.out.println("    PhilHealth: " + philHealth);
        System.out.println("    Pag-IBIG: " + pagIbig);
        System.out.println("    Tax: " + withholdingTax);
        System.out.println("Net Salary: " + netSalary);
        System.out.println("\n======================================");
    }

    /*
    *Computes the hours worked in a single work day.
    *Grace limit: logins at or before 8:10 AM is treated as 8:00 AM (no late deduction).
    *Overtime cap: logouts after 5:00 PM is not counted; end time capped at 5:00 PM (no overtime pay).
    *Lunch deduction: 60 minutes is deducted if the employee worked more than an hour.
    *Late minutes deduction: late minutes is deducted from maximum working minutes. 
    *Return hours worked as decimal. 
     */
    public static double computeHoursWorked(LocalTime login, LocalTime logout) {
        LocalTime graceLimit = LocalTime.of(8, 10);
        LocalTime startTime = LocalTime.of(8, 00);
        LocalTime endTime = LocalTime.of(17, 00);

        //Cap logout at end time (overtime not counted).
        if (logout.isAfter(endTime)) {
            logout = endTime;
        }

        //Login within grace limit time is treated as on time.
        if (!login.isAfter(graceLimit)) {
            login = startTime;
        }

        //Calculate how many minutes late the employee is, 0 if the employee is on time.
        long lateMinutes = Math.max(0, Duration.between(startTime, login).toMinutes());

        //Limit worked minutes based on late minutes.
        long minutesWorked = Math.min(Duration.between(login, logout).toMinutes(), MAX_WORKING_MINUTES - lateMinutes);

        //Deduct 1 hour unpaid lunch break if employee worked more than 60 minutes.
        //Return 0 if employee worked less than 60 minutes.
        if (minutesWorked > 60) {
            minutesWorked -= LUNCH_BREAK_MINS;
        } else {
            minutesWorked = 0;
        }
        return (double) minutesWorked / MINUTES_PER_HOUR;
    }

    /*
    *Computes the employee's SSS Contribution. 
    *Compensation range below PHP 3250 --> PHP 135 contribution.
    *Compensation range from PHP 3250 to 24749 --> PHP 135 + PHP 22.50 for each PHP 500 increment above PHP 3250.
    *Compensation range PHP 24750 and above --> PHP 1,125 contribution. 
     */
    public static double computeSSSContribution(double grossMonthlySalary) {

        if (grossMonthlySalary < SSS_LOWEST_SALARY_BRACKET) {
            return SSS_MIN_CONTRIBUTION; //
        }
        if (grossMonthlySalary >= SSS_HIGHEST_SALARY_BRACKET) {
            return SSS_MAX_CONTRIBUTION;
        }

        //Start at minimum contribution and add PHP 22.50 for each PHP 500 increment. 
        double contribution = SSS_MIN_CONTRIBUTION;
        for (double salary = SSS_LOWEST_SALARY_BRACKET; salary <= grossMonthlySalary; salary += SSS_SALARY_INCREMENT) {
            contribution += SSS_CONTRIBUTION_INCREMENT;
        }
        return contribution;
    }

    /*
    *Computes the employee's monthly PagIBIG Contribution. 
    *Monthly basic salary below PHP 1,000 --> PHP 0, no contribution.
    *Monthly basic salary at least PHP 1,000 to PHP 1,500 --> 1% of salary, capped at PHP 100.
    *Monthly basic salary over PHP 1,500 --> 2% of  salary, capped at PHP 100. 
     */
    public static double computePagIBIGContribution(double grossMonthlySalary) {
        if (grossMonthlySalary >= PAGIBIG_MIN_SALARY_THRESHOLD && grossMonthlySalary <= PAGIBIG_MAX_SALARY_THRESHOLD) {
            return Math.min(grossMonthlySalary * PAGIBIG_EMPLOYEE_MIN_CONTRIBUTION_RATE, PAGIBIG_MAX_CONTRIBUTION);
        } else if (grossMonthlySalary > PAGIBIG_MAX_SALARY_THRESHOLD) {
            return Math.min(grossMonthlySalary * PAGIBIG_EMPLOYEE_MAX_CONTRIBUTION_RATE, PAGIBIG_MAX_CONTRIBUTION);
        } else {
            return 0;
        }
    }

    /*
    *Computes the employee's monthly PhilHealth Contribution (employee share). 
    *The total premium rate is divided equally between employee and employer.
    *Monthly basic salary at or below PHP 10000 --> PHP 150 (employee share).
    *Monthly basic salary between PHP 10000 and PHP 60000 --> (Gross monthly salary * .03) / 2.
    *Monthly basic salary at or above PHP 60000 --> PHP 900 (employee share).
     */
    public static double computePhilHealthContribution(double grossMonthlySalary) {
        if (grossMonthlySalary <= PHILHEALTH_MIN_MONTHLY_BASIC_SALARY) {
            return (PHILHEALTH_MIN_EMPLOYEE_SHARE);
        } else if (grossMonthlySalary >= PHILHEALTH_MAX_MONTHLY_BASIC_SALARY) {
            return (PHILHEALTH_MAX_EMPLOYEE_SHARE);
        } else {
            return (grossMonthlySalary * PHILHEALTH_PREMIUM_RATE) / PHILHEALTH_EMPLOYEE_SHARE;
        }
    }

    //Adds the government-mandated contributions(SSS, Philhealth, and Pag-IBIG) to get total deductions. 
    public static double computeTotalDeductions(double sss, double philhealth, double pagibig) {
        return sss + philhealth + pagibig;
    }

    //Subtracts total deductions from gross monthly salary to get taxable income.
    public static double computeTaxableIncome(double grossMonthlySalary, double totalDeductions) {
        return grossMonthlySalary - totalDeductions;
    }

    //Computes the withholding tax using the provided matrix for government-mandated deductions/contributions for withholding tax.
    public static double computeWithholdingTax(double taxableIncome) {
        if (taxableIncome < MONTHLY_RATE_20832_AND_BELOW) {
            return 0; //Tax-exempt/ No Withholding Tax
        } else if (taxableIncome < MONTHLY_RATE_20833_AND_BELOW_33_333) {
            //20% in excess of PHP 20,833
            return (taxableIncome - MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_20_PERCENT_TAX_RATE) * TAX_RATE_20_PERCENT;
        } else if (taxableIncome < MONTHLY_RATE_33_333_TO_BELOW_66_667) {
            //PHP 2,500 + 25% in excess of PHP 33,333
            return (((taxableIncome - MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_25_PERCENT_TAX_RATE) * TAX_RATE_25_PERCENT) + PLUS_2500);
        } else if (taxableIncome < MONTHLY_RATE_66_667_TO_BELOW_166667) {
            //PHP 10,833 + 30% in excess of PHP 66,667
            return (((taxableIncome - MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_30_PERCENT_TAX_RATE) * TAX_RATE_30_PERCENT) + PLUS_10833);
        } else if (taxableIncome < MONTHLY_RATE_166667_TO_BELOW_666_667) {
            //PHP 40,833.33 + 32% in excess over PHP 166,667
            return (((taxableIncome - MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_32_PERCENT_TAX_RATE) * TAX_RATE_32_PERCENT) + PLUS_40833_33);
        } else {
            //PHP 200,833.33 + 35% in excess of 666,667
            return (((taxableIncome - MONTHLY_RATE_TO_BE_SUBTRACTED_FOR_35_PERCENT_TAX_RATE) * TAX_RATE_35_PERCENT) + PLUS_200833_33);
        }
    }
}
