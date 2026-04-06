package chanjava;

import java.util.*;
import java.time.LocalDate;

public class LibraryBookTrackingSystem {

    static class Book {
        String title;
        int quantity;

        Book(String title, int quantity) {
            this.title = title;
            this.quantity = quantity;
        }
    }

    static ArrayList<Book> libraryBooks = new ArrayList<>();
    static Map<String, List<String>> borrowedBooks = new LinkedHashMap<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loginMenu();
    }
  
    static String capitalize(String text) {
        if (text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    static String getNameInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("[a-zA-Z\\s'-]+")) return capitalize(input);
            System.out.println("Letters only. Try again.");
        }
    }

    static String getCourseInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("[a-zA-Z\\s]+")) return capitalize(input);
            System.out.println("Invalid input. Try again");
        }
    }

    static String getIDInput(String prompt) {	
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("[0-9\\-]+")) return input;
            System.out.println("Invalid input. Try again");
        }
    }

    static String getContactInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.matches("\\d+")) return input;
            System.out.println("Invalid input. Try again");
        }
    }

    static int getNumber(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int x = Integer.parseInt(sc.nextLine());
                if (x >= min && x <= max) return x;
                System.out.println("Invalid input. Try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    static void loginMenu() {
        while (true) {
            System.out.println("\n=== Welcome to School Library Book Tracker ===");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter an option: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) login();
            else if (choice.equals("2")) System.exit(0);
            else System.out.println("Invalid option. Try again.");
        }
    }

    static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (username.equals("librarian") && password.equals("1234")) {
            System.out.println("Login successful!");
            mainMenu();
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    static void mainMenu() {
        while (true) {
            System.out.println("\n=== Library Menu ===");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. Edit Borrower Info");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. View Available Books");
            System.out.println("7. View Borrowed Books");
            System.out.println("8. Logout");
            System.out.print("Enter option: ");
            String option = sc.nextLine();

            switch (option) {
                case "1": addBook(); break;
                case "2": removeBook(); break;
                case "3": editBorrowerInfo(); break;
                case "4": borrowBook(); break;
                case "5": returnBook(); break;
                case "6": viewAvailableBooks(); break;
                case "7": viewBorrowedBooks(); break;
                case "8": return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void addBook() {
        int n = getNumber("Enter how many books to add? ", 1, 100);
        for (int i = 1; i <= n; i++) {
            while (true) {
                System.out.print(i + ". Enter Book Title: ");
                String title = sc.nextLine().trim();
                int qty = getNumber("Enter quantity: ", 1, 100);

                if (!title.isEmpty()) {
                    boolean found = false;
                    for (Book b : libraryBooks) {
                        if (b.title.equalsIgnoreCase(title)) {
                            b.quantity += qty;
                            found = true;
                            break;
                        }
                    }
                    if (!found) libraryBooks.add(new Book(title, qty));
                    System.out.println("Added: " + title + " (Qty: " + qty + ")");
                    break;
                } else {
                    System.out.println("Book title cannot be empty.");
                }
            }
        }
        System.out.println("Book added successfully!");
    }

    static void removeBook() {
        if (libraryBooks.isEmpty()) {
            System.out.println("No books to remove.");
            return;
        }
        viewAvailableBooks();
        int n = getNumber("Enter how many books to remove? ", 1, libraryBooks.size());
        for (int i = 1; i <= n; i++) {
            int idx = getNumber(i + ". Enter book number: ", 1, libraryBooks.size()) - 1;
            Book book = libraryBooks.get(idx);
            int qty = getNumber("Enter quantity to remove (Available: " + book.quantity + "): ", 1, book.quantity);
            if (qty < book.quantity) book.quantity -= qty;
            else libraryBooks.remove(idx);
            System.out.println("Book removed successfully!");
        }
    }

    static void editBorrowerInfo() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books.");
            return;
        }

        List<String> keys = new ArrayList<>(borrowedBooks.keySet());
        for (int i = 0; i < keys.size(); i++) System.out.println((i + 1) + ". " + keys.get(i));

        int choice = getNumber("Select borrower number to edit: ", 1, keys.size()) - 1;
        String oldKey = keys.get(choice);
        List<String> books = borrowedBooks.get(oldKey);

        String first = getNameInput("New First Name: ");
        String last = getNameInput("New Last Name: ");
        String course = getCourseInput("New Course: ");
        String studentID = getIDInput("New Student ID: ");
        String contact = getContactInput("New Contact Number: ");
        String date = oldKey.split("\\|")[4].trim();

        String newKey = first + " " + last + " | " + course + " | " + studentID + " | " + contact + " | " + date;
        borrowedBooks.remove(oldKey);
        borrowedBooks.put(newKey, books);

        System.out.println("Borrower info updated successfully!");
    }

    static void borrowBook() {
        if (libraryBooks.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        viewAvailableBooks();
        int n = getNumber("How many books to borrow? ", 1, libraryBooks.size());
        List<Integer> selected = new ArrayList<>();
        List<Integer> qtyToBorrow = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            while (true) {
                int idx = getNumber(i + ". Enter book number: ", 1, libraryBooks.size()) - 1;
                Book book = libraryBooks.get(idx);
                if (!selected.contains(idx)) {
                    int borrowQty = getNumber("Enter quantity to borrow (Available: " + book.quantity + "): ", 1, book.quantity);
                    selected.add(idx);
                    qtyToBorrow.add(borrowQty);
                    break;
                } else System.out.println("Already selected.");
            }
        }

        String first = getNameInput("First Name: ");
        String last = getNameInput("Last Name: ");
        String course = getCourseInput("Course: ");
        String studentID = getIDInput("Student ID: ");
        String contact = getContactInput("Contact Number: ");
        String key = first + " " + last + " | " + course + " | " + studentID + " | " + contact + " | " + LocalDate.now();

        borrowedBooks.putIfAbsent(key, new ArrayList<>());
        for (int i = 0; i < selected.size(); i++) {
            int idx = selected.get(i);
            int borrowQty = qtyToBorrow.get(i);
            Book book = libraryBooks.get(idx);
            for (int j = 0; j < borrowQty; j++) borrowedBooks.get(key).add(book.title);
            book.quantity -= borrowQty;
        }

        libraryBooks.removeIf(b -> b.quantity == 0);
        System.out.println("Borrowed successfully!");
    }

    static void returnBook() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No borrowed books.");
            return;
        }

        List<String> keys = new ArrayList<>(borrowedBooks.keySet());
        for (int i = 0; i < keys.size(); i++)
            System.out.println((i + 1) + ". " + keys.get(i) + " | Books: " + borrowedBooks.get(keys.get(i)).size());

        int borrower = getNumber("Select borrower: ", 1, keys.size()) - 1;
        List<String> books = borrowedBooks.get(keys.get(borrower));

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String b : books) counts.put(b, counts.getOrDefault(b, 0) + 1);

        List<String> uniqueBooks = new ArrayList<>(counts.keySet());

        System.out.println("\nBorrowed Books:");
        for (int i = 0; i < uniqueBooks.size(); i++) {
            String title = uniqueBooks.get(i);
            int qty = counts.get(title);
            System.out.println((i + 1) + ". " + title + " (Quantity: " + qty + ")");
        }

        int bookChoice = getNumber("Select book to return: ", 1, uniqueBooks.size()) - 1;
        String selectedBook = uniqueBooks.get(bookChoice);
        int maxQty = counts.get(selectedBook);

        int returnQty = getNumber("Enter quantity to return: ", 1, maxQty);

        boolean found = false;
        for (Book b : libraryBooks) {
            if (b.title.equals(selectedBook)) {
                b.quantity += returnQty;
                found = true;
                break;
            }
        }
        if (!found) libraryBooks.add(new Book(selectedBook, returnQty));

        int removed = 0;
        for (int j = 0; j < books.size(); j++) {
            if (books.get(j).equals(selectedBook)) {
                books.remove(j);
                j--;
                removed++;
                if (removed == returnQty) break;
            }
        }

        if (books.isEmpty()) borrowedBooks.remove(keys.get(borrower));

        System.out.println("\nBooks returned successfully!");
    }

    static void viewAvailableBooks() {
        System.out.println("\n=== Available Books ===");
        if (libraryBooks.isEmpty()) System.out.println("No books available.");
        else {
            for (int i = 0; i < libraryBooks.size(); i++) {
                Book b = libraryBooks.get(i);
                System.out.println((i + 1) + ". " + b.title + " (Qty: " + b.quantity + ")");
            }
        }
    }

    static void viewBorrowedBooks() {
        System.out.println("\n=== Borrowed Books ===");
        if (borrowedBooks.isEmpty()) System.out.println("No borrowed books.");
        else {
            int count = 1;
            for (String key : borrowedBooks.keySet()) {
                List<String> books = borrowedBooks.get(key);
                Map<String, Integer> borrowedCount = new LinkedHashMap<>();
                for (String b : books) borrowedCount.put(b, borrowedCount.getOrDefault(b, 0) + 1);

                String[] info = key.split("\\|");
                System.out.println(count + ". Name: " + info[0].trim() +
                        " | Course: " + info[1].trim() +
                        " | ID: " + info[2].trim() +
                        " | Contact: " + info[3].trim() +
                        " | Date: " + info[4].trim());
                System.out.println("   Books: " + borrowedCount);
                count++;
            }
        }
    }
}
