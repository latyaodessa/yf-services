package vk.parser.workflow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.text.Transliterator;

import vk.parser.dto.elastic.PostElasticDTO;

public class PostRegexTextCleaner {
	

	private static final String ph = "(Ph.+?(?=\\n))|(Ph:.+?(?=\\n))";
	private static final String md = "(Md.+?(?=\\n))|(Md:.+?(?=\\n))";
	private static final String ph_tag = "Ph";
	private static final String md_tag = "Md";
	private static final String REGEX_CLEANER = "\\[.*\\||\\]|:";
	
	private static final String ART_TAG = "#art";

	
	public PostElasticDTO getCleanedText(PostElasticDTO dto){
		

		String ph_cleaned = executeRegex(dto.getText(), ph);
		String md_cleaned = executeRegex(dto.getText(), md);
		
		if(!StringUtils.isBlank(ph_cleaned)){
			ph_cleaned = removePhTag(ph_cleaned);
			dto.setPh(ph_cleaned);
			dto.setPh_translit(transliterate(ph_cleaned));
		}
		if(!StringUtils.isBlank(md_cleaned)){
			md_cleaned = removeMdTag(md_cleaned);
			dto.setMd(md_cleaned);
			dto.setMd_translit(transliterate(md_cleaned));
		}		
		
		return dto;
	}



	private String removePhTag(String text){
		return text.replaceFirst(ph_tag, "").replaceAll(REGEX_CLEANER, StringUtils.EMPTY).trim();
	}
	private String removeMdTag(String text){
		return text.replaceFirst(md_tag, "").replaceAll(REGEX_CLEANER, StringUtils.EMPTY).trim();
	}
	
	
	private String executeRegex(String text, String regex){
		Matcher matcher = Pattern.compile(regex).matcher(text);
		return matcher.find() ? matcher.group(1) : null;
	}
	
	
	private String transliterate(String text){
		 Transliterator transliterator = Transliterator.getInstance("Any-Latin; Latin-ASCII;");
		return transliterator.transliterate(text);
	}
	

}
