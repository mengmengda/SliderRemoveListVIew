package com.alphabet;

import java.util.Locale;

import android.text.TextUtils;

public abstract class Alphabet implements Comparable<Alphabet>{
	public abstract String getAlphabetStr();
	//'{' ascii code is large than 'z' 
	public String getAlphabet() {
		if(TextUtils.isEmpty(getAlphabetStr()))return "{";
        String key = getAlphabetStr().substring(0, 1).toUpperCase(Locale.CHINA);
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "{";
    }

	public int compareTo(Alphabet another) {
		char alph =   getAlphabet().charAt(0);
		char otherAlph =  another.getAlphabet().charAt(0);
		if(alph>otherAlph)return 1;
		if(alph==otherAlph)return 0;
		return -1;
	}
}
