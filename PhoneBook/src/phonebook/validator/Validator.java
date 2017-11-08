package phonebook.validator;

import java.util.regex.Pattern;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

public class Validator {
	private void showError(Control ctl){ 
		ctl.getStyleClass().add("error");} 
	private void removeError(Control ctl){ 
		ctl.getStyleClass().remove("error"); 
		ctl.setTooltip(null); }
	
	protected boolean validateValue(Control ctl){ 
		removeError(ctl); 
		if(ctl instanceof TextField){ 
			if(((TextField)ctl).getText() != null && !((TextField)ctl).getText().equals("")) 
				return true; }
		else if(ctl instanceof ComboBox<?>){ 
			if(((ComboBox<?>)ctl).getValue() != null) 
				return true; } 
		showError(ctl); 
		return false; 
		}
	
	protected boolean validateNumber(Control ctl){ 
		removeError(ctl); 
		if(ctl instanceof TextField){ 
			//http://howtodoinjava.com/regex/java-regex-validate-international-phone-numbers/
			Pattern pattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
			String text=((TextField)ctl).getText();
			boolean broj=pattern.matcher(text).matches();
			if(((TextField)ctl).getText() != null  && !((TextField)ctl).getText().equals("") && broj==false) 
				return true; }		
		showError(ctl); 
		return false; 
		}

}
