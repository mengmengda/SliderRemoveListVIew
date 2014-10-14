package com.alphabet;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AlphabetAdapter extends BaseAdapter{
	private List<? extends Alphabet> list;
	public static String[] alphabetList = new String[] {
			"A","B","C","D","E","F","G","H","I",
			"J","K","L","M","N","O","P","Q","R",
			"S","T","U","V","W","X","Y","Z","#" 
			};
	private Map<String, Integer> alphabetMap = new HashMap<String, Integer>();
	
	public AlphabetAdapter(List<? extends Alphabet> list){
		this.list = list;
		initData();
	}
	public AlphabetAdapter(){}
	public void setList(List<? extends Alphabet> list){
		this.list = list;
		initData();
	}
	public Map<String, Integer> getAlphabetMap(){
		return alphabetMap;
	}
	private void initData() {
		alphabetMap.clear();
		String alphabetTemp = "";
		String alphabetNext = "";
		for (int index = 0; index < list.size(); index++) {
			if (!isAlphabet(list.get(index).getAlphabet())) {
				alphabetMap.put("#", index);
				break;
			}
			alphabetNext = list.get(index).getAlphabet().substring(0, 1);
			if (!alphabetTemp.equalsIgnoreCase(alphabetNext)) {
				if (isAlphabet(alphabetTemp)) {
					alphabetMap.put(alphabetTemp + "_Next", index);
				}
				alphabetMap.put(alphabetNext, index);
				alphabetTemp = alphabetNext;
			}
		}

		int alphabetTempIndex = 0;
		for (int index = 0, j = 0; index < alphabetList.length; index++) {
			if (alphabetMap.get(alphabetList[index]) != null) {
				alphabetTempIndex = alphabetMap.get(alphabetList[index]);
				while (j <= index) {
					alphabetMap.put(alphabetList[j], alphabetTempIndex);
					j++;
				}

			}

		}
	}

	/**
	 * change the title visable/invisable  and show alphabet 
	 */
	protected void titleChange(TextView titleTextView,int position){
		String alphabet = list.get(position).getAlphabet();
		if (isAlphabet(alphabet) && position == alphabetMap.get(alphabet)) {
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText(list.get(position).getAlphabet());
		} else if (!isAlphabet(alphabet) && position == alphabetMap.get("#")) {
			titleTextView.setVisibility(View.VISIBLE);
			titleTextView.setText("#");
		} else {
			titleTextView.setVisibility(View.GONE);
		}
	}

	@Override
	public int getCount() {
		if(list==null)return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	private boolean isAlphabet(String alphabet) {
		if (TextUtils.isEmpty(alphabet)) {
			return false;
		}
		alphabet = alphabet.toUpperCase(Locale.ENGLISH);
		char abc = alphabet.charAt(0);
		if (abc < 'A' || abc > 'Z') {
			return false;
		} else {
			return true;
		}
	}
}
