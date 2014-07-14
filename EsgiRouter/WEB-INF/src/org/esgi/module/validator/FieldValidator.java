package org.esgi.module.validator;
import java.util.regex.*;
import java.text.*;

/* ================================================================ *
 *                        FIELD VALIDATOR                           * 
 * Cette classe contient les methodes qui valident les champs       *
 * (login, mdp, email, logueur max, longueur min, etc...)           *
 * 																	*
 * nombre de methodes : 11											*
 * 																	*
 * ================================================================ */
public class FieldValidator
{
	
	/* ================================================================ *
	 *                        VALIDATE EMPTY                            * 
	 *     valide que la chaine de caractere n'est pas vide ou NULL.    *
	 * ================================================================ */	 
	public boolean validateEmpty(String str)
	{
		if(str != null && !str.isEmpty())
		{ 
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/* ================================================================ *
	 *                        VALIDATE NUMBERS                          * 
	 *     valide que la chaine de caractere contient au moins 1 nbr    *
	 * ================================================================ */
	public boolean validateNumbers(String str)
	{
		if(str.matches(".*\\d.*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* ================================================================ *
	 *                        VALIDATE SPACES                           * 
	 *    valide que la chaine de caractere ne contient aucun espace.   *
	 * ================================================================ */
	public boolean validateSpaces(String str)
	{
		if (str.matches("^\\s*$"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	/* ================================================================ *
	 *                        VALIDATE LENGTH                           * 
	 *     valide que chaine = nombre de caracteres passes en param.    *
	 * ================================================================ */
	public boolean validateLength(String str)
	{
		String[] splitArray = null;
		splitArray = str.split(",");
		
		if (splitArray[0].length() == Integer.parseInt(splitArray[1]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* ================================================================ *
	 *                        VALIDATE MIN LENGTH                       * 
	 *     valide que la chaine de caractere est superieure au nb       *
	 *     de caracteres autorises.										*
	 * ================================================================ */
	public boolean validateMinLength(String str, int len)
	{		
		if (str.length() >= len)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* ================================================================ *
	 *                        VALIDATE MAX LENGTH                       * 
	 *     valide que la chaine de caractere est inferieur ou egal      *
	 *     au nb de caracteres autorises.								*
	 * ================================================================ */
	public boolean validateMaxLength(String str, int len)
	{	
		if (str.length() <= len)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/* ================================================================ *
	 *                        VALIDATE PASSWORD                         * 
	 *     regles de validation	:									    *
	 * (?=.*[0-9])     au moins un chiffre								*
	 * (?=.*[a-z])     au moins une lettre minuscule					*
	 * (?=.*[A-Z])     au moins une lettre majuscule					*
	 * (?=.*[@#*=])    au moins un caractere special					*
	 * (?=[\\S]+$)     aucun espace 									*
	 * {8,40}          au moins 8 caracteres							*
	 * ================================================================ */
	public boolean validatePassword(String str)
	{	
		String password_pattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
	    Pattern pattern = Pattern.compile(password_pattern);
	    
	    Matcher matcher = pattern.matcher(str);
	    return matcher.matches();
	}
	
	/* ================================================================ *
	 *                        VALIDATE DATE                             * 
	 *     permet de valider qu'un champ correspond a une date          *
	 *     multi-format!												*
	 * ================================================================ */	
	public boolean validateDate(String str)
	{
		SimpleDateFormat[] possibleFormats = new SimpleDateFormat[]
		{
				new SimpleDateFormat("yyyy-MM-dd"),
				new SimpleDateFormat("yyyy,MM,dd"),
				new SimpleDateFormat("yyyyMMdd"),
				new SimpleDateFormat("dd-MM-yyyy"),
				new SimpleDateFormat("dd-MM-yy"),
				new SimpleDateFormat("dd/MM/yyyy"),
				new SimpleDateFormat("dd/MM/yy")
		};

		for (SimpleDateFormat format: possibleFormats)
		{
			format.setLenient(false);
		}
		int index = 0;
		while (index < possibleFormats.length)
		{
			try
			{
				possibleFormats[index++].parse(str);
				return true;
			} catch (ParseException ex) { /* Rien */ }
		}
		return false;
	}
	
	/* ================================================================ *
	 *                        VALIDATE EMAIL                            * 
	 *     permet de valider si une adresse email est au bon format     *
	 * ================================================================ */	
	public boolean validateEmail(String str)
	{
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	/* ================================================================ *
	 *                        VALIDATE BOOLEAN                          * 
	 *     permet de valider qu'une varaible est de type boolean        *
	 *     true OU false												*
	 * ================================================================ */
	public boolean validateBoolean(String str)
	{
		return "true".equals(str) || "false".equals(str);
	}
	
	/* ================================================================ *
	 *                        VALIDATE DECIMAL                          * 
	 *     permet de valider si une variable est de type decimal	    *
	 * ================================================================ */
	public boolean validateDecimal(String str)
	{
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