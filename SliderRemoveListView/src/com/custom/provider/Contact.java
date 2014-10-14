package com.custom.provider;

import com.alphabet.Alphabet;

public class Contact extends Alphabet{
	public String id;
	public String name;
	public int photoId;
	public String phoneNum;
	public String sortkey;

	@Override
	public String getAlphabetStr() {
		return sortkey;
	}
	

	
	
}
