package Main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("Welcome to APU Car Sales System!");
            System.out.println("[1] Managing Staff\n[2] Salesman\n[3] Customer\n[0] Exit");
            System.out.println("Enter your selection: ");
            String selection = sc.nextLine();
            
            switch(selection){
                case "1": System.out.println("Welcome to Staff Page!"); break;
                case "2": System.out.println("Welcome to Salesman Page!"); break;
                case "3": System.out.println("Welcome to Customer Page!"); break;
                case "0": System.out.println("Exiting system..."); break;
                default: System.out.println("Please enter the number 0-3."); continue;
            }
            break;
        }   
    }
}
