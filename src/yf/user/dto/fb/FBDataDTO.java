package yf.user.dto.fb;

public class FBDataDTO {
	private boolean is_silhouette;
	private String url;
	private int height;
	private int width;
	
	public FBDataDTO() {
		super();
	}
	
	public FBDataDTO(boolean is_silhouette, String url, int height, int width) {
		super();
		this.is_silhouette = is_silhouette;
		this.url = url;
		this.height = height;
		this.width = width;
	}
	
	public boolean isIs_silhouette() {
		return is_silhouette;
	}
	public void setIs_silhouette(boolean is_silhouette) {
		this.is_silhouette = is_silhouette;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	


}
