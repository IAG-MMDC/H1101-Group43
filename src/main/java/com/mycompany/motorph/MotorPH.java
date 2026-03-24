/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.motorph;

/**
 * Isabelle Angeli Gallardo
 * Jam Rosales
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class MotorPH {
    
    static String empCsv = "resources/MotorPH_Employee Data - Employee Details.csv";
    static String attCsv = "resources/MotorPH_Employee Data - Attendance Record.csv";
    static String[] empNos = new String[34];
    static String[] empFirstNames = new String[34];
    static String[] empLastNames = new String[34];
    static String[] empBirthdays = new String[34];
    static double[] hourlyRates = new double[34];
    static int count = 0;
    static String option = "";  
    
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        
        System.out.print("Enter username: ");
        String inputUser = scan.nextLine();
        
        System.out.print("Enter password: ");
        String inputPass = scan.nextLine();
       
        // Access verification (improved logic: username checked first, then password)
        if (inputUser.equals("employee")) {

            if (!inputPass.equals("12345")) {
                System.out.println("Incorrect password.");
                return;
            }

            System.out.println("\n1. Enter employee number.");
            System.out.println("2. Exit the program.");
            System.out.print("Choose an option: ");
            option = scan.nextLine();

            if (option.equals("1")) {

                System.out.print("\nEnter employee no.: ");
                String inputEmpNo = scan.nextLine();                   
                
                readEmpData();
                for (int i = 0; i < count; i++) {
                if(inputEmpNo.equals(empNos[i])) {
                    printEmpDetails(i);
                    return;
                } else {
                    System.out.println("Employee number does not exist.");
                    return;
                }
                } 
            } else {
                System.out.println("Exiting program...");
            }

        } else if (inputUser.equals("payroll_staff")) {

            if (!inputPass.equals("12345")) {
                System.out.println("Incorrect password.");
                return;
            }
            
            System.out.println("\n1. Process Payroll."); 
            System.out.println("2. Exit the program.");
            System.out.print("Choose an option: ");
            option = scan.nextLine();
            
            if (option.equals("1")){
                
            System.out.println("\n1. One employee.");
            System.out.println("2. All employees.");
            System.out.println("3. Exit the program.");
            System.out.print("Choose an option: ");
            option = scan.nextLine();

                switch (option) {
                    case "1":
                            System.out.print("\nEnter employee no.: ");
                            String inputEmpNo = scan.nextLine();
                            
                            readEmpData();
                            for (int i = 0; i < count; i++) {
                                if(inputEmpNo.equals(empNos[i])) {
                                printEmpDetails(i);
                                readAttData(empNos[i], hourlyRates[i]);
                                return;
                                } else {
                                    System.out.println("Employee number does not exist.");
                                    return;
                                }
                            }
                        break;
                    case "2":
                        readEmpData();
                        for (int i = 0; i < count; i++) {
                            printEmpDetails(i);
                            readAttData(empNos[i], hourlyRates[i]);
                        }
                        break;
                    default: System.out.println("Exiting program...");
                }
            } else {
                System.out.println("Exiting program...");
            }

        } else {
            System.out.println("Incorrect username.");
        }
    }

    // Reads the employee details file and stores each value in the matching array.
    // This allows the program to access employee information later without reopening the file.
    public static void readEmpData() {
        
       try (BufferedReader br = new BufferedReader(new FileReader(empCsv))) { 

            br.readLine(); 
            String currentLine;

            while ((currentLine = br.readLine()) != null) {
                if (currentLine.trim().isEmpty()) continue;

                String[] data = currentLine.split(",");
                
                    empNos[count] = data[0];
                    empLastNames[count] = data[1];
                    empFirstNames[count] = data[2];
                    empBirthdays[count] = data[3];
                    hourlyRates[count] = Double.parseDouble(data[data.length-1]);
                    count++;
                }
            }
            catch (Exception e) {
            System.out.println("An error occurred while reading employee file.");
            e.printStackTrace();
            } 
       
        }

    // Prints the selected employee's basic information.
    public static void printEmpDetails(int i){ 
        System.out.println("\n===================================");
        System.out.println("Employee # : " + empNos[i]);
        System.out.println("Employee Name : " + empLastNames[i] + ", " + empFirstNames[i]);
        System.out.println("Birthday : " + empBirthdays[i]);
        System.out.println("===================================");
   } 

   // Reads the attendance file and computes payroll for each cut-off period.
   // The first cut-off covers days 1 to 15, while the second cut-off covers days 16 to the end of the month.
   // It also calculates salary deductions and net pay based on the monthly gross salary.
   public static void readAttData(String empNo, double hourlyRate) {
   
   for (int month = 6; month <= 12; month++) { 
            double firstCutoff = 0;
            double secondCutoff = 0;
            String monthName = getMonthName(month);
            String daysInMonth = getDaysInMonth(month);

            try (BufferedReader br = new BufferedReader(new FileReader(attCsv))) {

                br.readLine(); 
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;

                    String[] data = line.split(",");
                    
                    if (!data[0].equals(empNo)) continue;

                    String[] dateParts = data[3].split("/");
                    int attMonth = Integer.parseInt(dateParts[0]);
                    int attDay = Integer.parseInt(dateParts[1]);
                    int attYear = Integer.parseInt(dateParts[2]);
                    
                    if (attYear != 2024) continue; 
                    if (attMonth != month) continue;

                    String[] loginParts = data[4].trim().split(":");
                    int loginHour = Integer.parseInt(loginParts[0]);
                    int loginMin = Integer.parseInt(loginParts[1]);
                     
                    String[] logoutParts = data[5].trim().split(":");
                    int logoutHour = Integer.parseInt(logoutParts[0]);
                    int logoutMin = Integer.parseInt(logoutParts[1]);

                    // This converts the login and logout time into total hours worked.
                    // It applies the program's attendance rules, including the grace period,
                    // the fixed lunch deduction, and the maximum allowable working time.
                    double hours = computeHours(loginHour, loginMin, logoutHour,logoutMin);

                    // Attendance is split by cut-off period so payroll can be displayed per half-month.
                    if (attDay <= 15) {
                        firstCutoff += hours;
                    } else {
                        secondCutoff += hours;
                    }
                }

            } catch (Exception e) {
                System.out.println("An error occurred while reading attendance file.");
                e.printStackTrace(); 
            }      
            double firstGrossSalary = firstCutoff * hourlyRate;
            double grossMonthlySalary = (firstCutoff + secondCutoff) * hourlyRate;
            
            // These formulas compute mandatory employee deductions based on gross monthly salary.
            double sss = computeSSS(grossMonthlySalary);
            double pagIbig = computePagIbig(grossMonthlySalary);
            double philHealth = computePhilHealth(grossMonthlySalary);
            double totalDeductions = computeTotalDeductions(sss, pagIbig, philHealth);
            double taxableIncome = computeTaxableIncome(grossMonthlySalary, totalDeductions);
            double witholdingTax = computeWithholdingTax(taxableIncome);
            double netSalary = taxableIncome - witholdingTax;
            
            // This prints the payroll summary for the selected employee and month.
            printPayrollDetails(monthName, firstCutoff, secondCutoff, daysInMonth, firstGrossSalary,
            grossMonthlySalary, totalDeductions, sss, philHealth, pagIbig,witholdingTax, netSalary);    
  
        }
   }

   // Returns the month name based on the numeric month value.
    public static String getMonthName(int month) {
       
            switch (month) {
                case 6: return "June"; 
                case 7: return "July"; 
                case 8: return "August"; 
                case 9: return "September"; 
                case 10: return  "October"; 
                case 11: return "November";
                case 12: return "December"; 
                default: return  "";
            }
    }

    // Returns the number of days in the month as a string for display purposes.
    public static String getDaysInMonth(int month) {
        
            switch (month) {
                case 6: return "30";
                case 7: return "31"; 
                case 8: return "31"; 
                case 9: return "30"; 
                case 10: return "31"; 
                case 11: return "30";
                case 12: return "31"; 
                default: return ""; 
            }
    }
    
    // Prints the payroll breakdown, including hours worked, gross pay, deductions, and net salary.
    public static void printPayrollDetails(String monthName, double firstCutoff, double secondCutoff, String daysInMonth, double firstGrossSalary,
    double grossMonthlySalary, double totalDeductions, double sss, double philHealth, double pagIbig, double withholdingTax, double netSalary) {     
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstCutoff);
            System.out.println("Gross Salary: " + firstGrossSalary); 
            System.out.println("Net Salary: " + firstGrossSalary); 

            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
            System.out.println("Total Hours Worked : " + secondCutoff);
            System.out.println("Gross Salary: " + grossMonthlySalary); 
            System.out.println("Deductions: " + totalDeductions);
            System.out.println("    SSS: " + sss);
            System.out.println("    PhilHealth: " + philHealth);
            System.out.println("    Pag-IBIG: " + pagIbig);
            System.out.println("    Tax: " + withholdingTax);
            System.out.println("Net Salary: "+ netSalary);
    }
    
    // Calculates the total hours worked from the login and logout time.
    // It applies the 8:00 AM start time, the 8:10 AM grace period, and the 1-hour lunch deduction.
    // If the employee logs out after 5:00 PM, the time is capped at 5:00 PM.
    public static double computeHours(int loginHour, int loginMin, int logoutHour, int logoutMin) {
        int graceLimit = 8 * 60 + 10;
        int startTime = 8 * 60;
        int login = loginHour * 60 + loginMin;
        int logout = logoutHour * 60 + logoutMin;

        if (logout > 17 * 60) {
            logout = 17 * 60;
        }
        if (login <= graceLimit) {
            login = startTime;
        }
        
        int lateMinutes = Math.max(0, login - startTime);
        int minutesWorked = Math.min(logout - login, 540 - lateMinutes);
        
        minutesWorked -= 60;
        
        if (minutesWorked < 0) minutesWorked = 0;
        
        return minutesWorked / 60.0;
    }
    
    // Calculates the SSS contribution based on the employee's gross monthly salary.
    public static double computeSSS(double grossMonthlySalary) {

        if (grossMonthlySalary < 3250) {
            return 135.00;
        }
        if (grossMonthlySalary >= 24750) {
            return 1125.00;
        }

        double salary = 3250;
        double contribution = 135;

        for (salary = 3250; grossMonthlySalary >= salary; salary += 500) {
            contribution += 22.50;
        }
        return contribution;
    }
    
    // Calculates the Pag-IBIG contribution using the salary range and contribution cap.
    public static double computePagIbig(double grossMonthlySalary) { 
        if (grossMonthlySalary >= 1000 && grossMonthlySalary <= 1500) {
            return Math.min(grossMonthlySalary * .03, 100);
        } else if (grossMonthlySalary > 1500) {
            return Math.min(grossMonthlySalary * .04, 100);
        } else {
            return 0;
        }
    }

    // Calculates the PhilHealth contribution based on the employee's gross monthly salary.
    public static double computePhilHealth(double grossMonthlySalary) { 
        if (grossMonthlySalary <= 10_000) {
            return (150.00);
        } else if (grossMonthlySalary >= 60_000) {
            return (900.00);
        } else {
            return (grossMonthlySalary * 0.03) / 2;    
       }      
    }

    // Adds all mandatory contributions to get the total deductions.
    public static double computeTotalDeductions(double sss, double philhealth, double pagibig) { 
        return sss + philhealth + pagibig;        
    }

    // Subtracts deductions from gross salary to get taxable income.
    public static double computeTaxableIncome(double grossMonthlySalary, double totalDeductions) { 
        return grossMonthlySalary - totalDeductions;
    }

    // Calculates withholding tax using the tax brackets for taxable income.
    public static double computeWithholdingTax(double taxableIncome) { 
        if (taxableIncome < 20_833) {
            return 0;
        } else if (taxableIncome < 33_333) {
            return (taxableIncome - 20_833) * .20;
        } else if (taxableIncome < 66_667) {
            return (((taxableIncome - 33_333) * .25) + 2_500);
        } else if (taxableIncome < 166_667) {
            return (((taxableIncome - 66_667) * .30) + 10_833);
        } else if (taxableIncome < 666_667) {
            return (((taxableIncome - 166_667) * .32) + 40_833.33);
        } else {
            return (((taxableIncome - 666_667) * .35) + 200_833.33);
        }
    }
}
