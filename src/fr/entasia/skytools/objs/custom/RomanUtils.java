package fr.entasia.skytools.objs.custom;

public class RomanUtils {

	public static final String[] roman = new String[]{"I", "II", "III", "IV", "V"};

	public static String toRoman(int level){
		return roman[level-1];
	}

	public static int toInt(String s){
		for(int i=0;i<roman.length;i++){
			if(roman[i].equals(s))return i+1;
		}
		return 0;
	}
}
