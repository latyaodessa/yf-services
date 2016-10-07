package vk.parser.dto.elastic;

import java.util.List;

import vk.logic.entities.Post;
import vk.logic.entities.PostAudio;
import vk.logic.entities.PostPhoto;
import vk.logic.entities.PostVideo;

public class PostElasticDTO extends Post{
	
	private String md;
	private String ph;
	private String md_translit;
	private String ph_translit;
	
	
	public PostElasticDTO(String md, String ph, String md_translit, String pg_translit) {
		super();
		this.md = md;
		this.ph = ph;
		this.md_translit = md_translit;
		this.ph_translit = pg_translit;
	}
	
	public PostElasticDTO() {
		super();
	}

	public PostElasticDTO(Long id, String from_id, String date, String text, Long signer_id, int likes, int reposts,
			List<PostAudio> postAudio, List<PostPhoto> postPhoto, List<PostVideo> postVideo) {
		super(id, from_id, date, text, signer_id, likes, reposts, postAudio, postPhoto, postVideo);
	}

	public String getMd() {
		return md;
	}
	public void setMd(String md) {
		this.md = md;
	}
	public String getPh() {
		return ph;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public String getMd_translit() {
		return md_translit;
	}
	public void setMd_translit(String md_translit) {
		this.md_translit = md_translit;
	}
	public String getPh_translit() {
		return ph_translit;
	}
	public void setPh_translit(String ph_translit) {
		this.ph_translit = ph_translit;
	}
	
	

}
