package yf.post.parser.workflow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.text.Transliterator;

import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;

public class PostRegexTextCleaner {
	

	private static final String ph = "(Ph.+?(?=\\n))|(Ph:.+?(?=\\n))";
	private static final String md = "(Md.+?(?=\\n))|(Md:.+?(?=\\n))";
	private static final String ph_tag = "Ph";
	private static final String md_tag = "Md";
	private static final String REGEX_CLEANER = "\\[.*\\||\\]|:";
	private static final String REGEX_WEBSITE_LAST_POSITION = "\\nhttp.*";
	private static final String REGEX_TAG_REMOVE = "#.*\\n";
	private static final String REGEX_SPEC_CHARACTER_CLEANER = "[^a-zA-Z ]";


	
	public PostElasticDTO getCleanedText(Post entity, PostElasticDTO dto){
		

		String ph_cleaned = executeRegex(entity.getText(), ph);
		String md_cleaned = executeRegex(entity.getText(), md);
		
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
		
		dto.setText(entity.getText());

		return dto;
	}
	
	public String getCleanedPh(final String text){
		String ph_cleaned = executeRegex(text, ph);
		if(!StringUtils.isBlank(ph_cleaned)){
			ph_cleaned = removePhTag(ph_cleaned);
			return ph_cleaned;
		}
		return StringUtils.EMPTY;

	}
	
	public String getCleanedMd(final String text) {
		String md_cleaned = executeRegex(text, md);
		if(!StringUtils.isBlank(md_cleaned)){
			md_cleaned = removeMdTag(md_cleaned);
			return md_cleaned;
		}	
		return null;
	}



	private String removePhTag(String text){
		return text.replaceFirst(ph_tag, "").replaceAll(REGEX_CLEANER, StringUtils.EMPTY).trim();
	}
	private String removeMdTag(String text){
		return text.replaceFirst(md_tag, "").replaceAll(REGEX_CLEANER, StringUtils.EMPTY).trim();
	}
	private String generalCleanText(String text){

		return text.replaceAll(REGEX_TAG_REMOVE, "")
					.replaceAll(REGEX_WEBSITE_LAST_POSITION, "")
					.replaceAll(REGEX_SPEC_CHARACTER_CLEANER, "");
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
