package org.esgi.module.validator;
import java.util.regex.*;
import java.text.*;

/** 
 * FieldValidator contient les méthodes qui valident les champs (login, mdp, email, etc...)
 */
public class FieldValidator
{
	
	public boolean validateEmpty(String str)
	{
		/**
		 * valide que la chaîne de caractère n'est pas vide ou NULL.
		 */
		if(str != null && !str.isEmpty()) { 
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean validateNumbers(String str)
	{
		/**
		 * valide que la chaîne de caractère contient au moins un nombre.
		 */
		if(str.matches(".*\\d.*")) {
			return true;
		} else{
			return false;
		}
	}
	
	public boolean validateSpaces(String str)
	{
		/**
		 * valide que la chaîne de caractère ne contient aucun espace.
		 */
		if (str.matches("^\\s*$")) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public boolean validateLength(String str)
	{
		/**
		 * valide si la longueur EST EGALE au chiffre passé en param (ex: "validateLength,7").
		 */
		String[] splitArray = null;
		splitArray = str.split(",");
		
		if (splitArray[0].length() == Integer.parseInt(splitArray[1]))
			return true;
		else
			return false;
	}
	
	public boolean validateMinLength(String str)
	{
		/**
		 * valide si la longueur EST SUPERIEURE OU EGALE 
		 * au chiffre passé en param (ex: "validateMinLength,7").
		 */
		String[] splitArray = null;
		splitArray = str.split(",");
		
		if (splitArray[0].length() >= Integer.parseInt(splitArray[1]))
			return true;
		else
			return false;
	}
	
	public boolean validateMaxLength(String str)
	{
		/**
		 * valide si la longueur EST INFERIEURE OU EGALE 
		 * au chiffre passé en param (ex: "validateMaxLength,7").
		 */
		String[] splitArray = null;
		splitArray = str.split(",");
		
		if (splitArray[0].length() <= Integer.parseInt(splitArray[1]))
			return true;
		else
			return false;
	}
	/*
	
	public boolean validatePassword(String str)
	{
		** règles pour la validation des mots de passe
		 * (?=.*[0-9])     au moins un chiffre
		 * (?=.*[a-z])     au moins une lettre minuscule
		 * (?=.*[A-Z])     au moins une lettre majuscule
		 * (?=.*[@#*=])    au moins un caractère spécial
		 * (?=[\\S]+$)     aucun espace 
		 *
	    String pattern = "((?=.*[0-9])(?=.*[a-z]) (?=.*[A-Z])(?=.*[@#*=])(?=[\\S]+$))";
	    if (str.matches(pattern)) {
	    	return true;
	    }
	    else {
	    	return false;
	    }
	}
	*/
	public boolean validatePassword(String str)
	{
		/** règles pour la validation des mots de passe
		 * (?=.*[0-9])     au moins un chiffre
		 * (?=.*[a-z])     au moins une lettre minuscule
		 * (?=.*[A-Z])     au moins une lettre majuscule
		 * (?=.*[@#*=])    au moins un caractère spécial
		 * (?=[\\S]+$)     aucun espace 
		 */
		
	    Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");
	    //Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-z]) (?=.*[A-Z])(?=.*[@#*=])(?=[\\S]+$))");
	    
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
	}
	
	
	public boolean validateDate(String str)
	{
		/**
		 * valide si la date est au bon format (multi-format).
		 */
		SimpleDateFormat[] possibleFormats = new SimpleDateFormat[] {
				new SimpleDateFormat("yyyy-MM-dd"),
				new SimpleDateFormat("yyyy,MM,dd"),
				new SimpleDateFormat("yyyyMMdd"),
				new SimpleDateFormat("dd-MM-yyyy"),
				new SimpleDateFormat("dd-MM-yy"),
				new SimpleDateFormat("dd/MM/yyyy"),
				new SimpleDateFormat("dd/MM/yy")};

		for (SimpleDateFormat format: possibleFormats)
		{
			format.setLenient(false);
		}

		int index = 0;
		while (index < possibleFormats.length) {
			try {
				possibleFormats[index++].parse(str);
				return true;
			} catch (ParseException ex) { /* Rien */ }
		}
		return false;

	}
	
	public boolean validateEmail(String str)
	{
		/**
		 * valide si l'adresse email est correcte.
		 */
		
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	 
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	public boolean validateBoolean(String str)
	{
		/**
		 * valide si le paramètre est un boolean (true ou false).
		 */
		return "true".equals(str) || "false".equals(str);
	}
	
	
	public boolean validateDecimal(String str)
	{
		/**
		 * valide si le paramètre est un décimal.
		 */
		try  
		{  
			double d = Double.parseDouble(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
		return true;  
	}

}