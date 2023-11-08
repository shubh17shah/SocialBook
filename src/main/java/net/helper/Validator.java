package net.helper;

import java.time.LocalDate;
import java.time.Period;

public class Validator {
	public static final int SUCCESS = 1;

	public static boolean checkName(String name) {
		return name.matches("[a-zA-Z\\s]{3,50}");
	}

	public static boolean checkUserName(String userName) {
		// Ensure the username consists of lowercase letters, digits, hyphens, and
		// underscores.
		// It should be between 4 and 28 characters in length.
		// Note: This regular expression allows only one hyphen or underscore at a time.
		return userName.matches("^[a-z0-9]([a-z0-9-_]{2,26}[a-z0-9])?$") && userName.length() >= 4
				&& userName.length() <= 28;
	}

	public static boolean checkEmail(String email) {
		// Define a regular expression pattern for a valid email address
		String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";

		// Check if the email matches the valid pattern
		return email.matches(emailPattern);
	}

	/**
	 * Password requirements: - At least 8 characters - Contains at least one
	 * lowercase letter - Contains at least one uppercase letter - Contains at least
	 * one digit - Contains at least one special character (e.g., !@#$%^&*) - No
	 * consecutive repeating characters (e.g., "aa" or "11")
	 */
	public static boolean checkPassword(String password) {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*!])[A-Za-z\\d@#$%^&*!]{8,}$";
		String consecutiveRepeat = "(.)\\1+";

		return password.matches(regex) && !password.matches(consecutiveRepeat);
	}

	public static boolean checkBirthday(LocalDate dob) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
     // Calculate the age by subtracting the birthdate from the current date
        int age = Period.between(dob, currentDate).getYears();

        // Check if the age is at least 13
        return age >= 13;
    }

//	public static boolean isValidPassword(String password, String confirmPassword) {
//        return password.equals(confirmPassword) && checkPassword(password);
//    }

//    public static boolean checkEmail(String email) {
//        // check email first
//        if (!email.matches("[a-z0-9-_]{3,30}@[a-z0-9-]{3,30}([.][a-z1-9]{2,5}){1,2}")) {
//            if (!email.matches("\\d{9,11}")) {
//                return false;
//            }
//        }
//        return true;
//    }

//  public static boolean checkUsername(String userName) {
//  return userName.matches("[a-z0-9]([a-z0-9-_]{1,28}[a-z0-9])?");
//}

	/**
	 * At least 4 chars, max 8 chars Contains at least one digit Contains at least
	 * one upper alpha char
	 */
//    public static boolean checkPassword(String password) {
//        return password.matches("(?=.*\\d+.*)(?=.*[A-Z]+.*)\\w{4,8}");
//    }
}